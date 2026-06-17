package jp.co.aforce.servlet;

import java.util.List;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import jp.co.aforce.beans.CartItem;
import jp.co.aforce.beans.User;
import jp.co.aforce.dao.PurchaseDAO;
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

			// 2. 購入直前の最終在庫チェック
			for (CartItem cartItem : cart) {
				int itemId = cartItem.getItem().getItemId();
				int currentStock = purchaseDao.getCurrentStock(itemId);

				if (currentStock < cartItem.getQuantity()) {
					request.setAttribute("errorTitle", "注文を確定できませんでした");
					request.setAttribute("errorBackUrl", "CartList.action");
					request.setAttribute("errorBtnText", "カートへ戻る");
					request.setAttribute("errorMessage",
							"申し訳ありません。「" + cartItem.getItem().getItemName() + "」は直前で在庫不足となりました。数量を調整してください。");
					return "login-error.jsp";
				}
			}

			int grandTotal = 0;
			for (CartItem cartItem : cart) {
				grandTotal += cartItem.getSubtotalWithTax();
			}

			int successTotalWithoutTax = (int) (grandTotal / 1.08);
			int successTax = grandTotal - successTotalWithoutTax;

			// 3. 在庫の減算 ＆ 購入履歴のデータベース保存処理
			purchaseDao.insertPurchase(loginUser, cart);

			request.setAttribute("successCart", cart);
			request.setAttribute("successGrandTotal", grandTotal);
			request.setAttribute("successTotalWithoutTax", successTotalWithoutTax);
			request.setAttribute("successTax", successTax);

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