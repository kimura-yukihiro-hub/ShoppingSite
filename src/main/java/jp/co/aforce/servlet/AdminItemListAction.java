package jp.co.aforce.servlet;

import java.util.List;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import jp.co.aforce.beans.Item;
import jp.co.aforce.dao.ItemDAO;
import jp.co.aforce.tool.Action;


 //管理者専用の商品一覧画面（admin-item-list.jsp）への表示遷移を管理するアクションクラス

public class AdminItemListAction extends Action {

	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {

		try {
			// 1. ItemDAOを実体化
			ItemDAO itemDao = new ItemDAO();

			// 2. 検索条件をすべて null に指定して、データベースから登録されている全商品リストを取得
			List<Item> adminItemList = itemDao.search(null, null, null, null);

			// 3. 取得した全商品リストを、リクエスト属性「adminItemList」という名前で格納
			request.setAttribute("adminItemList", adminItemList);

			// 4. 正常なら、管理者専用の商品一覧画面のJSPファイル名だけを返す
			return "admin-item-list.jsp";

		} catch (Exception e) {
			// 例外が発生したら、ログを出力してリクエストに「独自のメッセージ」を詰め込みます。
			e.printStackTrace();

			request.setAttribute("errorTitle", "システムエラー");
			request.setAttribute("errorMessage", "管理者用商品データの取得中に問題が発生しました。データベースの状態を確認してください。");
			request.setAttribute("errorBackUrl", "AdminMenu.action");
			request.setAttribute("errorBtnText", "管理者メニューへ戻る");

			return "login-error.jsp";
		}
	}
}