package jp.co.aforce.servlet;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import jp.co.aforce.tool.Action;

public class UserDeleteAction extends Action {

	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {

		//退会確認画面を安全に表示させるためだけの役割
		return "user-delete.jsp";
	}

}
