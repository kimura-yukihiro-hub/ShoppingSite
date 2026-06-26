package jp.co.aforce.servlet;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import jp.co.aforce.tool.Action;

// 新規商品登録画面（item-add.jsp）への表示遷移を管理するアクションクラス
public class AdminViewAction extends Action {

	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// ログインチェック（共通の仕組みがあればそれを利用してください）
		HttpSession session = request.getSession();
		if (session.getAttribute("loginUser") == null) {
			return "redirect:Login.action";
		}
		
		return "item-add.jsp";
	}
}