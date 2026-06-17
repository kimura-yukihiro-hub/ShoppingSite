package jp.co.aforce.servlet;

import java.util.List;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import jp.co.aforce.beans.User;
import jp.co.aforce.dao.UserDAO;
import jp.co.aforce.tool.Action;

public class AdminDeleteConfirmAction extends Action {

	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {

		try {
			// 1. 削除対象となるIDをリクエストパラメータから取得
			String targetId = request.getParameter("targetId");

			if (targetId == null || targetId.isEmpty()) {
				request.setAttribute("errorTitle", "削除処理エラー");
				request.setAttribute("errorMessage", "削除対象のIDが特定できませんでした。");
				request.setAttribute("errorBackUrl", "AdminMenu.action");
				request.setAttribute("errorBtnText", "管理者メニューへ戻る");
				return "login-error.jsp";
			}

			// 2. DAOを使って削除対象のユーザー情報を全件取得し、IDで絞り込む
			UserDAO dao = new UserDAO();
			List<User> userList = dao.searchAll();
			User targetUser = null;

			for (User u : userList) {
				if (u.getMemberId().equals(targetId)) {
					targetUser = u;
					break;
				}
			}

			// 削除対象が見つからない場合
			if (targetUser == null) {
				request.setAttribute("errorTitle", "エラー");
				request.setAttribute("errorMessage", "指定されたユーザーは見つかりませんでした。");
				request.setAttribute("errorBackUrl", "AdminList.action");
				request.setAttribute("errorBtnText", "一覧へ戻る");
				return "login-error.jsp";
			}

			// 3. JSPへ必要な情報を渡す
			request.setAttribute("targetId", targetId);
			request.setAttribute("targetUser", targetUser);

			// 4. 本人確認用のパスワード入力画面へ遷移
			return "admin-delete-confirm.jsp";

		} catch (Exception e) {
			e.printStackTrace();
			request.setAttribute("errorTitle", "システムエラー");
			request.setAttribute("errorMessage", "削除確認画面の表示中に予期せぬエラーが発生しました。");
			request.setAttribute("errorBackUrl", "AdminMenu.action");
			request.setAttribute("errorBtnText", "管理者メニューへ戻る");
			return "login-error.jsp";
		}
	}
}