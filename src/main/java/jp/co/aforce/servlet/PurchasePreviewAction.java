package jp.co.aforce.servlet;

import java.util.List;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import jp.co.aforce.beans.CartItem;
import jp.co.aforce.tool.Action;

public class PurchasePreviewAction extends Action {

	//プレビュー画面は「ログイン必須」のため、requireLogin = false は指定しない
	// これにより、未ログインのゲストがボタンを押した場合は自動的にログイン画面へ誘導

	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {

		HttpSession session = request.getSession();

		// 1. セッションから買い物かご（リスト）を取得
		@SuppressWarnings("unchecked")
		List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");

		// カートが空っぽの状態でURL直打ちアクセスされた場合はカート画面に戻す
		if (cart == null || cart.isEmpty()) {
			return "redirect:CartList.action";
		}

		try {
			int grandTotal = 0;
			for (CartItem cartItem : cart) {
				// 会員割引＆軽減税率8%が適用済みの税込小計を合算
				grandTotal += cartItem.getSubtotal();
			}

			// ★修正箇所：税込の総合計から「税抜金額」と「消費税(8%)」を逆算して計算
			int totalWithoutTax = (int) (grandTotal / 1.08);
			int tax = grandTotal - totalWithoutTax;

			// 2. 計算した金額データとカート情報を購入確認画面へ引き渡すために格納
			request.setAttribute("cart", cart);
			request.setAttribute("grandTotal", grandTotal);
			request.setAttribute("totalWithoutTax", totalWithoutTax); 
			request.setAttribute("tax", tax);

			// 3. 購入最終プレビュー画面のJSPファイル名を返す
			return "purchase-preview.jsp";

		} catch (Exception e) {
			e.printStackTrace();
			request.setAttribute("errorTitle", "注文確認エラー");
			request.setAttribute("errorBackUrl", "CartList.action");
			request.setAttribute("errorBtnText", "カートへ戻る");
			request.setAttribute("errorMessage", "注文確認画面の生成中に予期せぬエラーが発生しました。お手数ですがカート画面からやり直してください。");
			return "login-error.jsp";
		}
	}
}