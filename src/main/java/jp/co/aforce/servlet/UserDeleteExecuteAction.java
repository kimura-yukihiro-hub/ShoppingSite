package jp.co.aforce.servlet;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import jp.co.aforce.beans.User;
import jp.co.aforce.dao.UserDAO;
import jp.co.aforce.tool.Action;

public class UserDeleteExecuteAction extends Action {

	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {

		try {
			// 1. フォームから送信された値を取得
			String memberId = request.getParameter("memberId");
			String inputPassword = request.getParameter("currentPassword"); // 画面で入力したPW

			HttpSession session = request.getSession(false);
			User loginUser = (session != null) ? (User) session.getAttribute("loginUser") : null;

			// セキュリティチェック：ログイン状態かつIDの一致を確認
			if (loginUser == null || !loginUser.getMemberId().equals(memberId) || inputPassword == null) {
				request.setAttribute("errorTitle", "退会できませんでした");
				request.setAttribute("errorMessage", "無効なリクエストです。");
				return "login-error.jsp";
			}

			// データベースのパスワードと入力されたパスワードが一致するか確認
			if (!loginUser.getPassword().equals(inputPassword)) {
				request.setAttribute("errorTitle", "退会手続きエラー");
				request.setAttribute("errorMessage", "パスワードが正しくありません。");
				request.setAttribute("errorBackUrl", "UserDeleteConfirm.action");
				request.setAttribute("errorBtnText", "確認画面に戻る");
				return "login-error.jsp";
			}

			// 2. 削除処理を実行
			UserDAO dao = new UserDAO();
			int count = dao.delete(memberId);

			if (count > 0) {
				session.invalidate(); // セッションを完全に削除
				return "user-delete-success.jsp";
			} else {
				request.setAttribute("errorTitle", "退会処理失敗");
				request.setAttribute("errorMessage", "退会処理に失敗しました。");
				return "login-error.jsp";
			}

		} catch (Exception e) {
			e.printStackTrace();
			request.setAttribute("errorTitle", "退会システムエラー");
			request.setAttribute("errorMessage", "データベース操作中にエラーが発生しました。");
			return "login-error.jsp";
		}
	}
}