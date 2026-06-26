package jp.co.aforce.servlet;

import java.util.List;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import jp.co.aforce.beans.CartItem;
import jp.co.aforce.beans.User; // ユーザー情報用
import jp.co.aforce.dao.LotDAO;
import jp.co.aforce.tool.Action;

public class PurchasePreviewAction extends Action {

	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		HttpSession session = request.getSession();

		@SuppressWarnings("unchecked")
		List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");
		User user = (User) session.getAttribute("loginUser"); // ログインユーザー情報を取得

		if (cart == null || cart.isEmpty()) {
			return "redirect:CartList.action";
		}

		try {
			// 在庫の再確認
			LotDAO lotDAO = new LotDAO();
			for (CartItem cartItem : cart) {
				int currentAvailable = lotDAO.getAvailableStock(cartItem.getItem().getItemId());

				if (cartItem.getQuantity() > currentAvailable) {
					request.setAttribute("errorTitle", "在庫状況が変更されました");
					request.setAttribute("errorMessage", "申し訳ございません。商品「" + cartItem.getItem().getItemName() +
							"」の在庫が不足したため、購入を続けることができません。<br>カートの内容を更新してください。");
					request.setAttribute("errorBackUrl", "CartList.action");
					request.setAttribute("errorBtnText", "カートに戻る");
					return "login-error.jsp";
				}

			}

			// カートから合計を算出
			int subtotalBeforeDiscount = 0;
			for (CartItem cartItem : cart) {
				subtotalBeforeDiscount += cartItem.getSubtotal();
			}

			// UserBeanのmeatRankから割引率を取得するメソッドをUserクラスに作成済みと想定
			double discountRate = (user != null) ? user.getDiscountRate() : 0.0;
			int discountAmount = (int) (subtotalBeforeDiscount * discountRate);
			int totalAfterDiscount = subtotalBeforeDiscount - discountAmount;

			// 税計算
			int tax = (int) (totalAfterDiscount * 0.08);
			int grandTotal = totalAfterDiscount + tax;

			// JSPへ渡す属性名に注意！
			request.setAttribute("subtotalBeforeDiscount", subtotalBeforeDiscount);
			request.setAttribute("discountAmount", discountAmount);
			request.setAttribute("totalAfterDiscount", totalAfterDiscount);
			request.setAttribute("tax", tax);
			request.setAttribute("grandTotal", grandTotal);

			return "purchase-preview.jsp";

		} catch (Exception e) {
			e.printStackTrace();
			request.setAttribute("errorTitle", "決済処理エラー");
	        request.setAttribute("errorMessage", "決済情報の取得中にエラーが発生しました。");
	        request.setAttribute("errorBackUrl", "CartList.action");
	        request.setAttribute("errorBtnText", "カートに戻る");
			return "login-error.jsp";
		}
	}
}