package jp.co.aforce.servlet;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import jp.co.aforce.tool.Action;

//ユーザーのセッション（ログイン状態）を完全に破棄し、安全にログアウトを行うアクションクラス

public class LogoutAction extends Action {

	public LogoutAction() {
		this.requireLogin = false;
	}

	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {

		try {
			// 1. 現在のユーザーのセッションを取得（なければ null を返す安全設計）
			HttpSession session = request.getSession(false);

			if (session != null) {
				// 2. セッションを完全に無効化（カートの中身やログインUser情報を全消去）
				session.invalidate();
			}

			// 3. ログアウト完了後は、ログイン入力画面（login-in.jsp）へフォワード
			return "login-in.jsp";

		} catch (Exception e) {
			e.printStackTrace();
			request.setAttribute("errorTitle", "ログアウトエラー");
			request.setAttribute("errorMessage", "ログアウト処理中に、サーバーで予期せぬエラーが発生しました。すでにセッションが切れている可能性があります。");
			return "login-error.jsp";
		}
	}
}
