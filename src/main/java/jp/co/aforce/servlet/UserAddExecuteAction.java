package jp.co.aforce.servlet;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import jp.co.aforce.beans.User;
import jp.co.aforce.dao.UserDAO;
import jp.co.aforce.tool.Action;

public class UserAddExecuteAction extends Action {

	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {

		//1. 確認画面のhiddenフォームから送信された値を取得
		String lastName = request.getParameter("lastName");
		String firstName = request.getParameter("firstName");
		String address = request.getParameter("address");
		String memberId = request.getParameter("memberId");
		String password = request.getParameter("password");
		String mailAddress = request.getParameter("mailAddress");
		//会員ランク(初期値1)を数値に変換して取得
		int meatRank = Integer.parseInt(request.getParameter("meatRank"));

		UserDAO dao = new UserDAO();

		//2. 登録用のUserインスタンス(Bean)を生成してデータをセット
		User user = new User();
		user.setLastName(lastName);
		user.setFirstName(firstName);
		user.setAddress(address);
		user.setMailAddress(mailAddress);
		user.setMemberId(memberId);
		user.setPassword(password);
		user.setMeatRank(meatRank);

		int count = 0;
		try {
			// 3. データベースへの挿入処理を実行
			count = dao.insert(user);
		} catch (java.sql.SQLIntegrityConstraintViolationException e) {
			request.setAttribute("errorMessage", "入力された会員番号「" + memberId + "」はすでに使用されています。");
			return "login-error.jsp";
		}

		// 4. 実行結果に応じた画面遷移のコントロール
		if (count > 0) {
			//セッション固定攻撃対策】古いセッションIDを完全に抹殺
			HttpSession oldSession = request.getSession(false);
			if (oldSession != null) {
				oldSession.invalidate(); 
			}
			
			//安全な全自動ログイン】新しいセッションIDを生成し、データを同期
			HttpSession newSession = request.getSession(true);
			newSession.setAttribute("loginUser", user); // マイページの「ようこそ〇〇さん！」へ直通

			// 自動ログイン状態で登録完了画面へ
			return "user-add-success.jsp"; // 完了画面JSP名（JSP側で10秒後にマイページへリフレッシュさせます）
		} else {
			request.setAttribute("errorMessage", "データベースへの会員登録に失敗しました。");
			return "login-error.jsp";
		}
	}
}