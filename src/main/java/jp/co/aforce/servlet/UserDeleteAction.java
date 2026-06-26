package jp.co.aforce.servlet;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import jp.co.aforce.tool.Action;

public class UserDeleteAction extends Action {

	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {

		try {
			// 退会確認画面（user-delete.jsp）へフォワードするためファイル名を返す
			return "user-delete.jsp";

		} catch (Exception e) {
			// 万が一、画面遷移中にサーバー側で予期せぬ例外が発生した場合はここでキャッチ
			e.printStackTrace();
			request.setAttribute("errorTitle", "退会手続きエラー");
			request.setAttribute("errorMessage", "退会確認画面の読み込み中に予期せぬエラーが発生しました。お手数ですがマイページからやり直してください。");
			request.setAttribute("errorBackUrl", "UserMenu.action"); // マイページ(メニュー)に戻す設定
			request.setAttribute("errorBtnText", "マイページへ戻る");
			return "login-error.jsp";
		}
	}
}
