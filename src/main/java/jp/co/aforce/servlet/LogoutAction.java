package jp.co.aforce.servlet;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import jp.co.aforce.tool.Action;

public class LogoutAction extends Action {
	public String execute(
			HttpServletRequest request, HttpServletResponse response
			) throws Exception {
		//現在のユーザーのセッションを取得
		HttpSession session = request.getSession();
		if (session != null) {
			//セッションの部屋を無効化
			session.invalidate();
		}
		
		return "login-in.jsp";
	}

}
