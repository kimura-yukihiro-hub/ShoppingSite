package jp.co.aforce.servlet;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import jp.co.aforce.beans.Item;
import jp.co.aforce.dao.ItemDAO;
import jp.co.aforce.tool.Action;

public class ItemEditAction extends Action {

	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {

		try {
			// 1. 画面（一覧など）から送られてきた「編集したい商品のID」を取得
			String itemIdStr = request.getParameter("itemId");

			// バリデーション：IDが空、または不正なアクセスの場合はエラー画面へ
			if (itemIdStr == null || itemIdStr.trim().isEmpty()) {
				request.setAttribute("errorTitle", "不正なアクセス");
				request.setAttribute("errorMessage", "編集対象の商品IDが指定されていません。");
				request.setAttribute("errorBackUrl", "AdminMenu.action");
				request.setAttribute("errorBtnText", "管理者メニューへ戻る");
				return "login-error.jsp";
			}

			// 数値への変換を実行
			int itemId;
			try {
				itemId = Integer.parseInt(itemIdStr.trim());
			} catch (NumberFormatException e) {
				// IDに数値以外（文字など）が紛れ込んでいた場合のクラッシュ防止対策
				request.setAttribute("errorTitle", "データエラー");
				request.setAttribute("errorMessage", "商品IDの解析に失敗しました。無効なパラメータです。");
				request.setAttribute("errorBackUrl", "AdminMenu.action");
				request.setAttribute("errorBtnText", "管理者メニューへ戻る");
				return "login-error.jsp";
			}

			// 2. ItemDAOを実体化し、IDを元にデータベースから商品を1件だけ検索
			ItemDAO itemDao = new ItemDAO();
			Item item = itemDao.findById(itemId);

			// 対象の商品データがデータベースに存在しなかった場合のエラーハンドリング
			if (item == null) {
				request.setAttribute("errorTitle", "商品が見つかりません");
				request.setAttribute("errorMessage", "指定された商品（ID: " + itemId + "）は存在しないか、既に削除された可能性があります。");
				request.setAttribute("errorBackUrl", "AdminMenu.action");
				request.setAttribute("errorBtnText", "管理者メニューへ戻る");
				return "login-error.jsp";
			}

			// 3. 取得した商品Beanデータをリクエスト属性「item」という名前で登録
			request.setAttribute("item", item);

			// 4. 編集画面（item-edit.jsp）へフォワードするためファイル名を返す
			return "item-edit.jsp";

		} catch (Exception e) {
			e.printStackTrace();
			request.setAttribute("errorTitle", "商品データ読込エラー");
			request.setAttribute("errorMessage", "編集対象の商品のデータを読み込む際に、データベースで予期せぬエラーが発生しました。");
			request.setAttribute("errorBackUrl", "AdminMenu.action");
			request.setAttribute("errorBtnText", "管理者メニューへ戻る");
			return "login-error.jsp";
		}
	}
}
