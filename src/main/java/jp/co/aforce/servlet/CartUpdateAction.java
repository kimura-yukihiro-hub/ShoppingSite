package jp.co.aforce.servlet;

import java.util.List;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import jp.co.aforce.beans.CartItem;
import jp.co.aforce.tool.Action;

//買い物かご（カート）内の特定商品の数量を変更（上書き）するアクションクラス

public class CartUpdateAction extends Action {

	public CartUpdateAction() {
		// カート内の数量変更は非ログイン（ゲスト）状態でも行えるため、検門スルーに設定
		this.requireLogin = false;
	}

	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {

		try {
			// パラメータのバリデーション
			String itemIdStr = request.getParameter("itemId");
			String quantityStr = request.getParameter("quantity");

			if (itemIdStr == null || !itemIdStr.matches("\\d+") ||
					quantityStr == null || !quantityStr.matches("\\d+")) {
				throw new IllegalArgumentException("不正なパラメータです");
			}
			
			// 1. カート画面のドロップダウンから送信された「商品ID」と「新しく選択された数量」を取得
			int itemId = Integer.parseInt(request.getParameter("itemId"));
			int quantity = Integer.parseInt(request.getParameter("quantity"));

			HttpSession session = request.getSession();

			// 2. セッションから現在の買い物かご(リスト)を取得
			@SuppressWarnings("unchecked")
			List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");

			if (cart != null) {
				// リストの中身をループで回して、IDが一致する商品の数量を書き換え
				for (CartItem cartItem : cart) {
					if (cartItem.getItem().getItemId() == itemId) {
						cartItem.setQuantity(quantity); // 数量を最新の値に上書き変更
						break; // 変更が完了したらループを終了
					}
				}

				// 3. 最新の買い物かごの状態をセッションに上書き保存
				session.setAttribute("cart", cart);
			}

			// 4. 金額の再計算を行うため、カート一覧表示アクションへリダイレクト
			return "redirect:CartList.action";

		} catch (Exception e) {
			e.printStackTrace();
			request.setAttribute("errorTitle", "数量変更エラー");
			request.setAttribute("errorMessage", "買い物かご内の数量変更処理中に予期せぬエラーが発生しました。");
			request.setAttribute("errorBackUrl", "CartList.action");
			request.setAttribute("errorBtnText", "買い物かごへ戻る");
			return "login-error.jsp";
		}
	}
}