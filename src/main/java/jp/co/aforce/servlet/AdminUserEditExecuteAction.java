package jp.co.aforce.servlet;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import jp.co.aforce.beans.User;
import jp.co.aforce.dao.UserDAO;
import jp.co.aforce.tool.Action;

public class AdminUserEditExecuteAction extends Action {

	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// 1. 管理者権限のチェック
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
			// 2. パラメータ取得と変換
			String memberId = request.getParameter("memberId");
			String rankStr = request.getParameter("meatRank");
			String newPassword = request.getParameter("password");
			int meatRank = Integer.parseInt(rankStr);
			if (meatRank < 1 || meatRank > 4) {
				throw new Exception("不正なランク値が送信されました。");
			}

			// 4. DB更新処理
			UserDAO dao = new UserDAO();
			int rankResult = dao.updateRank(memberId, meatRank);
			
			// パスワード更新を実行（入力がある場合）
			int passResult = 1; // パスワード更新がない場合は成功扱い
			if (newPassword != null && !newPassword.trim().isEmpty()) {
				passResult = dao.updatePassword(Integer.parseInt(memberId),newPassword);
			}

			if (rankResult > 0 && passResult > 0) {
				// 更新成功時は一覧へ戻る
				return "redirect:AdminUserList.action";
			} else {
				throw new Exception("更新対象が見つかりません。");
			}
		} catch (Exception e) {
			e.printStackTrace();
			request.setAttribute("errorTitle", "編集エラー");
			request.setAttribute("errorMessage", "会員情報の更新に失敗しました。");
			request.setAttribute("errorBackUrl", "AdminList.action");
			request.setAttribute("errorBtnText", "一覧へ戻る");
			return "login-error.jsp";
		}
	}
}
