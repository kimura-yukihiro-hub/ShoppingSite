package jp.co.aforce.servlet;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import jp.co.aforce.dao.UserDAO;
import jp.co.aforce.tool.Action;

public class UserDeleteExecuteAction extends Action {

	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {

		//1. 退会確認画面(user-delete.jsp)のhiddenフォームから送信された会員番号を取得
		String memberId = request.getParameter("memberId");

		// 安全対策：もし何らかの理由で会員番号が空で届いたらエラー画面へ飛ばす
		if (memberId == null || memberId.isEmpty()) {
			request.setAttribute("errorMessage", "不正なアクセスが検出されました。");
			return "login-error.jsp";
		}

		// 2. UserDAOを実体化し、データベースから削除処理を実行
		UserDAO dao = new UserDAO();
		int count = dao.delete(memberId);

		// 3. 実行結果に応じた画面遷移とセッションの完全クリア
		if (count > 0) {

			//退会が成功したら、現在のログインセッションの部屋を完全に消滅させる(ログアウト状態にする)
			HttpSession session = request.getSession(false);
			if (session != null) {
				session.invalidate(); // セッションを無効化(クリア)する命令
			}

			//退会画面(JSP)へ遷移
			return "user-delete-success.jsp";

		} else {
			// 削除失敗時（対象のユーザーがすでに存在しない場合など）：エラー画面へ遷移
			request.setAttribute("errorMessage", "退会処理に失敗しました。<br>すでにアカウントが存在しない可能性があります。");
			return "login-error.jsp";
		}
	}

}
