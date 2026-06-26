package jp.co.aforce.servlet;

import java.util.List;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import jp.co.aforce.beans.CartItem;
import jp.co.aforce.beans.User;
import jp.co.aforce.dao.LotDAO;
import jp.co.aforce.dao.PurchaseDAO;
import jp.co.aforce.dao.UserDAO;
import jp.co.aforce.tool.Action;

public class PurchaseExecuteAction extends Action {

	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {

		HttpSession session = request.getSession();

		// 1. セッションからログインユーザー情報とカートを取得
		User loginUser = (User) session.getAttribute("loginUser");
		@SuppressWarnings("unchecked")
		List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");

		if (loginUser == null || cart == null || cart.isEmpty()) {

			request.setAttribute("errorTitle", "注文を処理できませんでした");
			request.setAttribute("errorBackUrl", "CartList.action");
			request.setAttribute("errorBtnText", "カートへ戻る");
			request.setAttribute("errorMessage", "セッションタイムアウト、またはカートが空です。最初からやり直してください。");
			return "login-error.jsp";
		}

		try {
			PurchaseDAO purchaseDao = new PurchaseDAO();
			UserDAO userDao = new UserDAO();
			LotDAO lotDAO = new LotDAO();

			// 2. 購入直前の最終在庫チェック
			for (CartItem cartItem : cart) {
				int itemId = cartItem.getItem().getItemId();
				int currentStock = lotDAO.getAvailableStock(itemId);

				if (currentStock < cartItem.getQuantity()) {
					request.setAttribute("errorTitle", "注文を確定できませんでした");
					request.setAttribute("errorBackUrl", "CartList.action");
					request.setAttribute("errorBtnText", "カートへ戻る");
					request.setAttribute("errorMessage",
							"申し訳ありません。「" + cartItem.getItem().getItemName() + "」は直前で在庫不足となりました。数量を調整してください。");
					return "login-error.jsp";
				}
			}

			// ランク昇格判定のために購入前のランクを保持
			int oldRank = loginUser.getMeatRank();

			// 割引率を取得
			double discountRate = loginUser.getDiscountRate();

			// 割引前合計（税抜）を先に計算
			int subtotalBeforeDiscount = 0;
			for (CartItem cartItem : cart) {
				subtotalBeforeDiscount += cartItem.getSubtotal();
			}

			// 割引適用
			int discountAmount = (int) (subtotalBeforeDiscount * discountRate);
			int totalAfterDiscount = subtotalBeforeDiscount - discountAmount;

			// 税と総合計
			int successTax = (int) (totalAfterDiscount * 0.08);
			int grandTotal = totalAfterDiscount + successTax;

			// 3. 在庫の減算 ＆ 購入履歴のデータベース保存処理
			purchaseDao.insertPurchase(loginUser, cart);
			int lastPurchaseId = purchaseDao.getLastPurchaseId();
			userDao.updateLastPurchaseDate(loginUser.getMemberId());

			// ロットを実際に消費（ステータス変更）と、ロット番号の取得・セット
			for (CartItem cartItem : cart) {
				int itemId = cartItem.getItem().getItemId();

				// 紐付けとステータス更新
				lotDAO.bindPurchaseId(itemId, cartItem.getQuantity(), lastPurchaseId);

				// 割り当てられたロット番号をDBから取得してCartItemにセット
				String serialNumbers = lotDAO.bindPurchaseId(itemId, cartItem.getQuantity(), lastPurchaseId);
				cartItem.setSerialNumber(serialNumbers);
			}

			// ランク昇格判定：DBから最新のユーザー情報を取得して比較
			long currentTotal = userDao.getTotalPurchaseAmount(loginUser.getMemberId());
			int newRank = 1;
			if (currentTotal >= 100000) {
				newRank = 3;
			} else if (currentTotal >= 30000) {
				newRank = 2;
			}

			if (newRank > oldRank) {
				// 1. まずDBを更新
				userDao.updateRank(loginUser.getMemberId(), newRank);
				// 2. セッション用のBeanを更新（DBから再取得）
				User updatedUser = userDao.getUserById(loginUser.getMemberId());
				// 3. メッセージをセット
				session.setAttribute("rankUpMessage", "🎉 おめでとうございます！「" + updatedUser.getMeatRankName() + "」に昇格しました！");
				// 4. セッションのユーザー情報を更新
				session.setAttribute("loginUser", updatedUser);
			}

			request.setAttribute("successCart", cart);
			request.setAttribute("successGrandTotal", grandTotal);
			request.setAttribute("successTotalWithoutTax", totalAfterDiscount); // 割引後
			request.setAttribute("successTax", successTax);
			request.setAttribute("successDiscountAmount", discountAmount); // 割引額を渡す

			// 4. カートをクリア
			session.removeAttribute("cart");

			// 5. ご購入完了画面へフォワード
			return "purchase-success.jsp";

		} catch (Exception e) {
			e.printStackTrace();
			request.setAttribute("errorTitle", "注文確定システムエラー");
			request.setAttribute("errorBackUrl", "CartList.action");
			request.setAttribute("errorBtnText", "カートへ戻る");
			request.setAttribute("errorMessage", "注文の確定処理中に、サーバーまたはデータベースで重大な例外が発生しました。購入は確定していません。");
			return "login-error.jsp";
		}
	}
}