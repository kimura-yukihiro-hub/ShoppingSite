package jp.co.aforce.servlet;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import jp.co.aforce.tool.Action;


// 管理者アカウント追加登録画面（admin-user-add.jsp）への表示遷移を管理するアクションクラス

public class AdminAddFormAction extends Action {

	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		// 解説：ここへ到達した時点で、ログイン済みであることは
		//   親クラス（Action）の start() メソッドが100%保証してくれている
		//   そのため、このActionの本来の目的である「画面を返すだけ」の1行で完結します。

		return "admin-user-add.jsp";
	}
}