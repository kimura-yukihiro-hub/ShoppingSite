package jp.co.aforce.servlet;

import java.util.ArrayList;
import java.util.List;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import jp.co.aforce.beans.CartItem;
import jp.co.aforce.beans.User;
import jp.co.aforce.dao.LotDAO;
import jp.co.aforce.tool.Action;
public class CartListAction extends Action {

	public CartListAction() {
		this.requireLogin = false;
	}

	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {

		try {
			HttpSession session = request.getSession();

			// 1. カート取得
			@SuppressWarnings("unchecked")
			List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");
			if (cart == null) {
				cart = new ArrayList<>();
			} else {
				// 各カート商品の現在のリアルタイム在庫数をチェックする
				LotDAO lotDAO = new LotDAO();
				for (CartItem cartItem : cart) {
					int available = lotDAO.getAvailableStock(cartItem.getItem().getItemId());
					// カートの数量が在庫を超えていたらフラグを立てる等の処理が可能
					request.setAttribute("stock_" + cartItem.getItem().getItemId(), available);
				}
			}

			// 2. 割引前定価の合計（税抜）集計
			int rawSubtotal = 0;
			for (CartItem cartItem : cart) {
				rawSubtotal += cartItem.getItem().getPrice() * cartItem.getQuantity();
			}

			// 3. 会員ランク情報の取得と計算
			double discountRate = 0.0;
			String rankName = "ゲスト";
			long targetAmount = 10000; // ゲストの初期値
			double progress = 0.0;

			User loginUser = (User) session.getAttribute("loginUser");

			if (loginUser != null) {
				// Beanのメソッドで一元管理
				targetAmount = loginUser.getNextRankGoal();
				discountRate = loginUser.getDiscountRate();
				rankName = loginUser.getMeatRankName();

				// 進捗計算
				long currentTotal = loginUser.getTotalPurchaseAmount();
				progress = (double) currentTotal / targetAmount * 100;
				if (progress > 100)
					progress = 100;
			}

			// 4. 金額計算
			int discountAmount = (int) (rawSubtotal * discountRate);
			int totalWithoutTax = rawSubtotal - discountAmount;
			int tax = (int) (totalWithoutTax * 0.08);
			int grandTotal = totalWithoutTax + tax;

			// 5. JSPへのデータ受け渡し
			request.setAttribute("cart", cart);
			request.setAttribute("subtotalWithoutTax", rawSubtotal);
			request.setAttribute("discountRatePercent", discountRate * 100); // 表示用(%)
			request.setAttribute("rankName", rankName);
			request.setAttribute("discountAmount", discountAmount);
			request.setAttribute("totalWithoutTax", totalWithoutTax);
			request.setAttribute("tax", tax);
			request.setAttribute("grandTotal", grandTotal);

			// ランク表示用
			long remaining = (loginUser != null) ? Math.max(0, targetAmount - loginUser.getTotalPurchaseAmount())
					: targetAmount;
			request.setAttribute("remainingAmount", remaining);
			request.setAttribute("rankProgressPercent", progress);

			return "cart-list.jsp";

		} catch (Exception e) {
			e.printStackTrace();
			request.setAttribute("errorTitle", "精算計算エラー");
			request.setAttribute("errorMessage", "計算中に予期せぬエラーが発生しました。");
			request.setAttribute("errorBackUrl", "ItemList.action");
			request.setAttribute("errorBtnText", "商品一覧へ戻る");
			return "login-error.jsp";
		}
	}
}