package jp.co.aforce.servlet;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import jp.co.aforce.tool.Action;

public class UserUpdateFormAction extends Action {

	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {

		try {

			//変更入力画面を安全に表示させるためだけの役割
			return "user-update.jsp";
		} catch (Exception e) {
			e.printStackTrace();
			request.setAttribute("errorTitle", "画面読み込みエラー");
			request.setAttribute("errorMessage", "会員情報変更画面の読み込み中に予期せぬエラーが発生しました。お手数ですがマイページからやり直してください。");
			request.setAttribute("errorBackUrl", "UserMenu.action"); // マイページに戻す設定
			request.setAttribute("errorBtnText", "マイページへ戻る");
			return "login-error.jsp";
		}
	}

}
