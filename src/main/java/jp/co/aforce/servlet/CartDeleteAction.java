package jp.co.aforce.servlet;

import java.util.List;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import jp.co.aforce.beans.CartItem;
import jp.co.aforce.tool.Action;


 //買い物かご（カート）から特定の商品を削除するアクションクラス
 
public class CartDeleteAction extends Action {

	public CartDeleteAction() {
		// カート内削除は非ログイン（ゲスト）状態でも行えるため、検門スルーに設定
		this.requireLogin = false;
	}

	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {

		try {
			// 1. カート画面の「削除」ボタンから送信された削除対象の商品IDを取得
			int itemId = Integer.parseInt(request.getParameter("itemId"));

			HttpSession session = request.getSession();

			// 2. セッションから現在の買い物かご(リスト)を取得
			@SuppressWarnings("unchecked")
			List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");

			if (cart != null) {
				// リストの中身をループで回して、IDが一致する商品を特定して削除する
				for (int i = 0; i < cart.size(); i++) {
					if (cart.get(i).getItem().getItemId() == itemId) {
						cart.remove(i); // 一致したお肉をリストから削除
						break; // 削除が完了したらループを終了
					}
				}

				// 3. 最新の買い物かごの状態をセッションに上書き保存
				session.setAttribute("cart", cart);
			}

			// 4. 二重送信防止のため、金額計算を行うカート一覧表示アクションへリダイレクト
			return "redirect:CartList.action";

		} catch (Exception e) {
			e.printStackTrace();
			request.setAttribute("errorTitle", "カート削除エラー");
			request.setAttribute("errorMessage", "買い物かごからの商品削除中に予期せぬ問題が発生しました。カートの状態が正しく更新されなかった可能性があります。");
			request.setAttribute("errorBackUrl", "CartList.action");
			request.setAttribute("errorBtnText", "買い物かごへ戻る");
			return "login-error.jsp";
		}
	}
}
