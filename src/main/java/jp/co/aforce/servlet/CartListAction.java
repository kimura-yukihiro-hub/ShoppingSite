package jp.co.aforce.servlet;

import java.util.ArrayList;
import java.util.List;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import jp.co.aforce.beans.CartItem;
import jp.co.aforce.beans.User;
import jp.co.aforce.tool.Action;

//買い物かご（カート）内の一覧表示、JSP連動型会員ランク割引、および軽減税率（8%）の計算を管理するアクションクラス

public class CartListAction extends Action {

	public CartListAction() {
		// カート一覧は未ログイン状態でも閲覧可能にする
		this.requireLogin = false;
	}

	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {

		try {
			HttpSession session = request.getSession();

			// 1. セッションから買い物かご（リスト）を取得。なければ空のリストを生成
			@SuppressWarnings("unchecked")
			List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");
			if (cart == null) {
				cart = new ArrayList<>();
			}

			// 2. 割引前定価の合計（税抜）を集計
			int rawSubtotal = 0;

			for (CartItem cartItem : cart) {
				rawSubtotal += cartItem.getItem().getPrice() * cartItem.getQuantity();
			}

			// 3. 【修正】会員ランクに応じた割引率の決定
			double discountRate = 0.0;
			String rankName = "一般会員 (ランク1)";

			User loginUser = (User) session.getAttribute("loginUser");
			if (loginUser != null) {
				int rank = loginUser.getMeatRank();
				if (rank == 2) {
					discountRate = 0.02;
					rankName = "ゴールド会員 (ランク2)";
				} else if (rank == 3) {
					discountRate = 0.03;
					rankName = "プラチナ会員 (ランク3)";
				} else if (rank == 4) {
					discountRate = 0.05;
					rankName = "VIP会員 (ランク4)";
				} else if (rank == 5) {
					rankName = "最高級管理者 (ランク5)";
				}
			} else {
				rankName = "ゲストユーザー";
			}

			// 4. 【修正】割引後合計を計算し、端数処理を適用
			// rawSubtotal から割引額を引き算する方式に変更します
			int discountAmount = (int) (rawSubtotal * discountRate);
			int totalWithoutTax = rawSubtotal - discountAmount;

			int tax = (int) (totalWithoutTax * 0.08);
			int grandTotal = totalWithoutTax + tax;

			// 5. cart-list.jsp のレシート内訳側へ、一分の隙もない計算データを引き渡す
			request.setAttribute("cart", cart);
			request.setAttribute("subtotalWithoutTax", rawSubtotal); // 割引前の定価合計
			request.setAttribute("discountRatePercent", discountRate); // 「2」「3」「5」などの数値表示用
			request.setAttribute("rankName", rankName); // 適用された会員ランク名
			request.setAttribute("discountAmount", discountAmount); // 割引された差額
			request.setAttribute("totalWithoutTax", totalWithoutTax); // 割引後の税抜合計
			request.setAttribute("tax", tax); // 8%の消費税額
			request.setAttribute("grandTotal", grandTotal); // 最終税込金額

			// 6. 買い物かご一覧画面のJSPファイル名を返す
			return "cart-list.jsp";

		} catch (Exception e) {
			e.printStackTrace();
			request.setAttribute("errorTitle", "精算計算エラー");
			request.setAttribute("errorMessage", "軽減税率および会員優待価格の計算中に予期せぬエラーが発生しました。");
			request.setAttribute("errorBackUrl", "ItemList.action");
			request.setAttribute("errorBtnText", "商品一覧へ戻る");
			return "login-error.jsp";
		}
	}
}