package jp.co.aforce.servlet;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import jp.co.aforce.beans.Item;
import jp.co.aforce.dao.ItemDAO;
import jp.co.aforce.tool.Action;

public class ItemDetailAction extends Action {

	// 商品詳細画面はログインしていないゲストユーザーでも閲覧できるようにガードを解除(false)
	public ItemDetailAction() {
		this.requireLogin = false;
	}

	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {

		try {

			//1. 一覧画面のリンクから送られてきた商品ID(itemId)を取得
			String idParam = request.getParameter("itemId");

			// 商品IDがそもそも届いていない場合(URL直打ちなどの不正アクセス)
			if (idParam == null || idParam.isEmpty()) {
				request.setAttribute("errorTitle", "不正なアクセスです");
				request.setAttribute("errorMessage", "商品情報を正しく取得できませんでした。<br>もう一度一覧から選択してください。");
				request.setAttribute("errorBackUrl", "ItemList.action");
				request.setAttribute("errorBtnText", "商品一覧へ戻る");
				return "login-error.jsp";
			}

			int itemId;
			try {
				itemId = Integer.parseInt(idParam);
			} catch (NumberFormatException e) {

				// 商品IDに文字などが混じっていた場合の安全ガード
				request.setAttribute("errorTitle", "商品が見つかりません");
				request.setAttribute("errorMessage", "無効な商品識別番号が指定されました。");
				request.setAttribute("errorBackUrl", "ItemList.action");
				request.setAttribute("errorBtnText", "商品一覧へ戻る");
				return "login-error.jsp";
			}

			//2. ItemDAOのfindByIdを呼び出して詳細データを取得
			ItemDAO dao = new ItemDAO();
			Item item = dao.findById(itemId);

			// データベースに該当する肉のIDが存在しなかった場合
			if (item == null) {
				request.setAttribute("errorTitle", "商品が存在しません");
				request.setAttribute("errorMessage", "指定されたお肉は売り切れか<br>すでに削除された可能性があります。");
				request.setAttribute("errorBackUrl", "ItemList.action");
				request.setAttribute("errorBtnText", "商品一覧へ戻る");
				return "login-error.jsp";
			}

			//3. 正常系：商品データをJSPへ引き渡すためにリクエスト属性に格納
			request.setAttribute("item", item);

			//4. 遷移先となる詳細画面のJSPファイル名を返す
			return "item-detail.jsp";
		} catch (Exception e) {
			//データベース通信エラー（SQL例外など）が起きた場合はここでキャッチ
			e.printStackTrace();
			request.setAttribute("errorTitle", "商品データ取得エラー");
			request.setAttribute("errorMessage", "お肉の詳細データを読み込む際にエラーが発生しました。サーバーまたはデータベースの接続状況を確認してください。");
			request.setAttribute("errorBackUrl", "ItemList.action");
			request.setAttribute("errorBtnText", "商品一覧へ戻る");
			return "login-error.jsp";

		}
	}
}
