package jp.co.aforce.servlet;

import java.util.List;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import jp.co.aforce.beans.User;
import jp.co.aforce.dao.UserDAO;
import jp.co.aforce.tool.Action;

public class AdminListAction extends Action {
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {

		try {
			// DAOをインスタンス化
			UserDAO dao = new UserDAO();

			// 管理者のみを抽出するメソッドを実行
			List<User> adminList = dao.searchAllAdmins();

			// JSPで表示するためにリクエストスコープにセット
			request.setAttribute("adminList", adminList);

			// 管理者一覧画面へフォワード
			return "admin-admin-list.jsp";
		} catch (Exception e) {
			e.printStackTrace();
			request.setAttribute("errorTitle", "システムエラー");
			request.setAttribute("errorMessage", "管理者一覧の取得中に予期せぬエラーが発生しました。");
			request.setAttribute("errorBackUrl", "AdminMenu.action");
			request.setAttribute("errorBtnText", "管理者メニューへ戻る");
			return "login-error.jsp";
		}
	}
}
