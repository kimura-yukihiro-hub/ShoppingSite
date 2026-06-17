package jp.co.aforce.servlet;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import jp.co.aforce.beans.User;
import jp.co.aforce.dao.UserDAO;
import jp.co.aforce.tool.Action;

public class AdminUserDeleteAction extends Action {

	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// 1. セッションからログイン中のユーザー情報を取得
		HttpSession session = request.getSession();
		User loginUser = (User) session.getAttribute("loginUser");

		// 2. ランク5（管理者）かどうかをチェック（権限ガード）
		if (loginUser == null || loginUser.getMeatRank() != 5) {
			request.setAttribute("errorTitle", "権限エラー");
			request.setAttribute("errorMessage", "管理者権限が必要です。");
			request.setAttribute("errorBackUrl", "ItemList.action");
			request.setAttribute("errorBtnText", "トップへ戻る");
			return "login-error.jsp";
		}

		try {
			// 3. パラメータから削除対象のIDと入力されたパスワードを取得
			String targetId = request.getParameter("targetId");
			String password = request.getParameter("password");

			// 4. パスワード認証：ログイン中のユーザーIDと入力されたパスワードで検索
			UserDAO dao = new UserDAO();
			User authenticatedUser = dao.search(loginUser.getMemberId(), password);

			// パスワードが一致しない（＝検索結果がnull）場合は削除させない
			if (authenticatedUser == null) {
				request.setAttribute("errorTitle", "認証失敗");
				request.setAttribute("errorMessage", "パスワードが正しくありません。");
				request.setAttribute("errorBackUrl", "AdminList.action");
				request.setAttribute("errorBtnText", "一覧へ戻る");
				return "login-error.jsp";
			}

			// 5. 本人確認が通ったら削除処理を実行

			User targetUser = null;
			for (User u : dao.searchAll()) {
				if (u.getMemberId().equals(targetId)) {
					targetUser = u;
					break;
				}
			}

			int count = dao.delete(targetId);

			// 成功・失敗の判定処理
			if (count > 0) {
				// 削除成功時のリダイレクト先を判定
				if (targetUser != null && targetUser.getMeatRank() == 5) {
					// 管理者(ランク5)を削除した場合は管理者一覧へ
					response.sendRedirect("AdminList.action");
				} else {
					// 一般会員(ランク5以外)を削除した場合は会員一覧へ
					response.sendRedirect("AdminUserList.action"); 
				}
				return null;
			} else {
				// 削除失敗：エラーメッセージを表示
				request.setAttribute("errorTitle", "削除失敗");
				request.setAttribute("errorMessage", "削除処理に失敗しました。該当データが存在しません。");
				request.setAttribute("errorBackUrl", "AdminList.action");
				request.setAttribute("errorBtnText", "一覧へ戻る");
				return "login-error.jsp";
			}
		} catch (Exception e) {
			e.printStackTrace();
			request.setAttribute("errorTitle", "システムエラー");
			request.setAttribute("errorMessage", "削除処理中にサーバーでエラーが発生しました。");
			request.setAttribute("errorBackUrl", "AdminUserList.action");
			request.setAttribute("errorBtnText", "一覧へ戻る");
			return "login-error.jsp";
		}
	}
}
