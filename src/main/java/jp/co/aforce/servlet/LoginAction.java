package jp.co.aforce.servlet;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import jp.co.aforce.beans.User;
import jp.co.aforce.dao.UserDAO;
import jp.co.aforce.tool.Action;

public class LoginAction extends Action {
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {

		//JSPのフォームから送信された「会員番号」とパスワード」を取得
		String memberId = request.getParameter("memberId");
		String password = request.getParameter("password");

		//UserDAOを呼び出して一致する会員を検索
		UserDAO dao = new UserDAO();
		User loginUser = dao.search(memberId, password);

		//検索結果に応じた画面遷移のコントロール
		if (loginUser != null) {

			//二重ログインのブロック処理
			if (jp.co.aforce.listener.LoginUserSessionListener.isLoggedIn(memberId)) {
				// すでに別の場所でログインされている場合は、専用のエラー画面へ移動させる
				request.setAttribute("errorMessage", "このアカウントはすでに<br>別の端末でログイン中です。");
				return "login-error.jsp";
			}

			//ログイン成功の時
			HttpSession session = request.getSession();
			session.setAttribute("loginUser", loginUser);
			// ログインしたまま画面を30分放置すると、自動的にログインの記憶が消滅する安全装置が発動する
			session.setMaxInactiveInterval(1 * 60);

			//会員ランク(MEAT_RANK)を取得
			int rank = loginUser.getMeatRank();

			if (rank == 5) {
				//管理者専用メニュー画面へ移動
				return "redirect:AdminMenu.action";
			} else {
				//ランクが1～4の会員は購入者用のメニュー画面へ移動
				return "redirect:UserMenu.action";
			}
		} else {
			//ログイン失敗の時
			//ユーザーが見つからなかった場合は、エラー専用画面へ移動
			request.setAttribute("errorMessage", "会員番号もしくはパスワードが違います");
			return "login-error.jsp";
		}
	}

}
