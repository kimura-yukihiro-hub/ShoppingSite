package jp.co.aforce.servlet;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import jp.co.aforce.tool.Action;

public class AdminMenuAction extends Action {

	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {

		//ここに到達した時点で、FrontControllerによる「肉ランク5チェック」が完全に成功している
		// そのため、他人の不正侵入を一切心配することなく、管理者専用メニューのJSP名を返してフォワードさせる
		return "admin-menu.jsp";

	}
}