package jp.co.aforce.servlet;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import jp.co.aforce.dao.ItemDAO;
import jp.co.aforce.dao.LotDAO;
import jp.co.aforce.tool.Action;

public class ItemDeleteAction extends Action {

	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {

		String itemIdStr = null;

		try {
			// 1. FrontControllerの@MultipartConfigによる誤作動を防ぐセーフティガード
			itemIdStr = request.getParameter("itemId");

			// 万が一、フォーム等からマルチパートとして届いていた場合の予備ルート
			if (itemIdStr == null || itemIdStr.trim().isEmpty()) {
				jakarta.servlet.http.Part part = request.getPart("itemId");
				if (part != null) {
					try (java.io.BufferedReader reader = new java.io.BufferedReader(
							new java.io.InputStreamReader(part.getInputStream(), "UTF-8"))) {
						itemIdStr = reader.lines().collect(java.util.stream.Collectors.joining("\n"));
					}
				}
			}
		} catch (Exception e) {
			// マルチパートの解析自体でエラーが起きた場合は、通常のパラメータ取得に強制切り替え
			itemIdStr = request.getParameter("itemId");
		}

		// 不正アクセス・パラメータ不足のバリデーションチェック
		if (itemIdStr == null || itemIdStr.trim().isEmpty()) {
			request.setAttribute("errorTitle", "削除できませんでした");
			request.setAttribute("errorMessage", "削除対象の商品IDが指定されていないか、無効なリクエストです。");
			request.setAttribute("errorBackUrl", "AdminItemList.action"); // 管理一覧に戻すように修正
			request.setAttribute("errorBtnText", "管理一覧へ戻る");
			return "login-error.jsp";
		}

		try {
			int itemId = Integer.parseInt(itemIdStr.trim());

			// 2. ItemDAOを呼び出してDELETE文を実行
			ItemDAO itemDao = new ItemDAO();
			LotDAO lotDAO = new LotDAO();
			
			// ロットを先に削除してから商品を削除する
			lotDAO.deleteLotsByItemId(itemId);
			boolean isSuccess = itemDao.deleteItem(itemId) > 0;

			// データベースからの削除が失敗した場合
			if (!isSuccess) {
				request.setAttribute("errorTitle", "削除エラー");
				request.setAttribute("errorMessage", "指定された商品（ID: " + itemId + "）の削除に失敗しました。既に削除されている可能性があります。");
				request.setAttribute("errorBackUrl", "AdminItemList.action");
				request.setAttribute("errorBtnText", "管理一覧へ戻る");
				return "login-error.jsp";
			}

			// 3. 削除が完全に成功したら、商品管理一覧画面（AdminItemList）へ安全にリダイレクト
			return "redirect:AdminItemList.action";

		} catch (NumberFormatException e) {
			request.setAttribute("errorTitle", "データエラー");
			request.setAttribute("errorMessage", "商品IDの解析に失敗しました。無効なパラメータです。");
			request.setAttribute("errorBackUrl", "AdminItemList.action");
			request.setAttribute("errorBtnText", "管理一覧へ戻る");
			return "login-error.jsp";
		}
	}
}
