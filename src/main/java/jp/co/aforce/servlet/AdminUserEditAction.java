package jp.co.aforce.servlet;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import jp.co.aforce.beans.User;
import jp.co.aforce.dao.UserDAO;
import jp.co.aforce.tool.Action;

public class AdminUserEditAction extends Action {

	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {

		// 1. セッションチェック（管理者権限ガード）
		HttpSession session = request.getSession();
		User loginUser = (User) session.getAttribute("loginUser");
		if (loginUser == null || loginUser.getMeatRank() != 5) {
			request.setAttribute("errorTitle", "権限エラー");
			request.setAttribute("errorMessage", "管理者権限が必要です。");
			request.setAttribute("errorBackUrl", "AdminList.action");
			request.setAttribute("errorBtnText", "一覧へ戻る");
			return "login-error.jsp";
		}

		try {
			// 2. 編集対象のIDを取得
			String targetId = request.getParameter("targetId");

			if (targetId == null || targetId.isEmpty()) {
				throw new Exception("対象のIDが指定されていません。");
			}

			// 3. DAOを使用して対象ユーザーを取得
			UserDAO dao = new UserDAO();
			// ※本来は dao.searchById(targetId) が望ましいです
			User targetUser = null;
			for (User u : dao.searchAll()) {
				if (u.getMemberId().equals(targetId)) {
					targetUser = u;
					break;
				}
			}

			if (targetUser == null) {
				request.setAttribute("errorTitle", "データエラー");
				request.setAttribute("errorMessage", "指定されたユーザーが見つかりません。");
				request.setAttribute("errorBackUrl", "AdminList.action");
				request.setAttribute("errorBtnText", "一覧へ戻る");
				return "login-error.jsp";
			}

			// 4. JSPにユーザー情報を渡す
			request.setAttribute("targetUser", targetUser);
			return "admin-user-edit.jsp";

		} catch (Exception e) {
			e.printStackTrace();
			request.setAttribute("errorTitle", "システムエラー");
			request.setAttribute("errorMessage", "編集画面の表示中にエラーが発生しました。");
			request.setAttribute("errorBackUrl", "AdminList.action");
			request.setAttribute("errorBtnText", "一覧へ戻る");
			return "login-error.jsp";
		}
	}
}