package jp.co.aforce.servlet;

import java.util.ArrayList;
import java.util.List;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import jp.co.aforce.beans.CartItem;
import jp.co.aforce.beans.Item;
import jp.co.aforce.dao.ItemDAO;
import jp.co.aforce.tool.Action;

//商品を買い物かご（カート）に追加するアクションクラス

public class CartAddAction extends Action {

	public CartAddAction() {
		// カートへの追加はログインしていないゲストユーザーでも許可するため、検門をスルーに設定
		this.requireLogin = false;
	}

	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {

		try {
			// 1. 詳細画面のフォームから送信された「商品ID」と「選択された数量」を取得
			int itemId = Integer.parseInt(request.getParameter("itemId"));
			int quantity = Integer.parseInt(request.getParameter("quantity"));

			// 数量が0以下という不正な値で送られてきた場合の最低限の安全ガード
			if (quantity <= 0) {
				quantity = 1;
			}

			// 2. ItemDAOのfindByIdを呼び出して、最新の商品情報をDBから取得
			ItemDAO dao = new ItemDAO();
			Item item = dao.findById(itemId);

			if (item != null) {
				HttpSession session = request.getSession();

				// セッションから現在の買い物かご（リスト）を取得。なければ新しく作る
				@SuppressWarnings("unchecked")
				List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");
				if (cart == null) {
					cart = new ArrayList<>();
				}

				// すでに同じお肉がカートに入っているかチェックするフラグ
				boolean isExist = false;

				for (CartItem cartItem : cart) {
					if (cartItem.getItem().getItemId() == itemId) {
						// すでにある場合は、数量を合算(加算)する
						int newQuantity = cartItem.getQuantity() + quantity;

						// もし合算した数量が在庫数を上回る場合は在庫上限で止める
						if (newQuantity > item.getStock()) {
							newQuantity = item.getStock();
						}

						cartItem.setQuantity(newQuantity);
						isExist = true;
						break;
					}
				}

				// カートにまだ入っていない新しい肉であれば、新規にリストへ追加
				if (!isExist) {
					// 新規追加時も、リクエストされた数量が在庫数を万が一超えていたら在庫上限に丸める
					if (quantity > item.getStock()) {
						quantity = item.getStock();
					}

					CartItem newItem = new CartItem();
					newItem.setItem(item);
					newItem.setQuantity(quantity);

					cart.add(newItem);
				}

				// 買い物かごの最新状態をセッションに上書き保存
				session.setAttribute("cart", cart);
			}

			// 3. 二重送信防止(PRGパターン)のため、カート一覧表示アクションへ「リダイレクト」
			return "redirect:CartList.action";

		} catch (Exception e) {
			e.printStackTrace();
			request.setAttribute("errorTitle", "カート追加エラー");
			request.setAttribute("errorMessage", "買い物かごへの商品追加中に問題が発生しました。商品の在庫状況、または通信状態を確認してください。");
			request.setAttribute("errorBackUrl", "ItemList.action");
			request.setAttribute("errorBtnText", "商品一覧へ戻る");
			return "login-error.jsp";
		}
	}
}