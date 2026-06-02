package jp.co.aforce.servlet;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

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
			return "user-add-success.jsp";
		} else {
			request.setAttribute("errorMessage", "データベースへの会員登録に失敗しました。");
			return "login-error.jsp";
		}
	}
}