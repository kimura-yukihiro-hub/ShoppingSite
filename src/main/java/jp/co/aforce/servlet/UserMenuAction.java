package jp.co.aforce.servlet;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import jp.co.aforce.tool.Action;

public class UserMenuAction extends Action {

	public String execute(HttpServletRequest request, HttpServletResponse response) {
		return "user-menu.jsp";
	}

}
