package jp.co.aforce.servlet;

import java.util.List;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import jp.co.aforce.beans.User;
import jp.co.aforce.dao.UserDAO;
import jp.co.aforce.tool.Action;

public class AdminUserListAction extends Action {

	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {

		try {
			// 1. UserDAOを実体化し、全ユーザーデータを取得
			UserDAO dao = new UserDAO();
			List<User> userList = dao.searchAll();

			// 2. 取得したリストをリクエストスコープにセット
			request.setAttribute("userList", userList);

			// 3. 管理者用顧客一覧画面へ遷移
			return "admin-user-list.jsp";

		} catch (Exception e) {
			e.printStackTrace();
			request.setAttribute("errorTitle", "システムエラー");
			request.setAttribute("errorMessage", "ユーザー一覧の取得中にエラーが発生しました。");
			request.setAttribute("errorBackUrl", "AdminMenu.action");
			request.setAttribute("errorBtnText", "管理者メニューへ戻る");
			return "login-error.jsp";
		}
	}
}
