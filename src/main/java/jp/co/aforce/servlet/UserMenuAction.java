package jp.co.aforce.servlet;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import jp.co.aforce.beans.User;
import jp.co.aforce.tool.Action;

public class UserMenuAction extends Action {

	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {

		try {
			// 1. 現在のセッションからログイン中のユーザー情報を安全に取得
			HttpSession session = request.getSession(false);

			if (session != null && session.getAttribute("loginUser") != null) {
				User loginUser = (User) session.getAttribute("loginUser");

				// 2. もし会員ランクが 5（管理者）だった場合は、一般マイページを見せずに強制リダイレクト
				if (loginUser.getMeatRank() == 5) {
					return "redirect:AdminMenu.action";
				}
			}

			// 3. 一般会員の場合は通常通り一般マイページ（user-menu.jsp）を表示
			return "user-menu.jsp";

		} catch (Exception e) {
			e.printStackTrace();
			request.setAttribute("errorTitle", "メニュー表示エラー");
			request.setAttribute("errorMessage", "マイページの読み込み中に予期せぬエラーが発生しました。一度ログアウトし、再度ログインをお試しください。");
			return "login-error.jsp";
		}
	}
}
