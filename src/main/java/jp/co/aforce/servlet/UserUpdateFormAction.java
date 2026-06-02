package jp.co.aforce.servlet;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import jp.co.aforce.tool.Action;

public class UserUpdateFormAction extends Action {

	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {

		//変更入力画面を安全に表示させるためだけの役割
		return "user-update.jsp";
	}

}
