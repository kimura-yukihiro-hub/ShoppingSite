package jp.co.aforce.servlet;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import jp.co.aforce.beans.User;
import jp.co.aforce.dao.UserDAO;
import jp.co.aforce.tool.Action;

public class UserUpdateExecuteAction extends Action {

	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {

		//1. 変更確認画面(user-update-check.jsp)のhiddenフォームから送信された値を取得
		String memberId = request.getParameter("memberId");
		String lastName = request.getParameter("lastName");
		String firstName = request.getParameter("firstName");
		String address = request.getParameter("address");
		String mailAddress = request.getParameter("mailAddress");
		String password = request.getParameter("password");

		// 2. 現在のセッションから、変更前のログイン情報を取得する（会員ランクを維持するため）
		HttpSession session = request.getSession(false);
		int currentMeatRank = 1; //デフォルト値

		if (session != null && session.getAttribute("loginUser") != null) {
			User currentLoginUser = (User) session.getAttribute("loginUser");
			currentMeatRank = currentLoginUser.getMeatRank(); // 現在のランク（1〜5）を引き継ぐ
		}

		// 3. データベース更新用のUserインスタンス（Bean）を生成してデータをセット
		User user = new User();
		user.setMemberId(memberId);
		user.setLastName(lastName);
		user.setFirstName(firstName);
		user.setAddress(address);
		user.setMailAddress(mailAddress);
		user.setPassword(password);
		user.setMeatRank(currentMeatRank); // 引き継いだランクを格納

		// 4. UserDAOを実体化し、データベースの更新処理を実行
		UserDAO dao = new UserDAO();
		int count = dao.update(user);

		// 5. 実行結果に応じた画面遷移とセッションの更新
		if (count > 0) {
			//データベースの更新が成功したら、セッション内の情報も最新のデータに差し替える
			if (session != null) {
				session.setAttribute("loginUser", user);
			}

			//変更完了画面(JSP)へ遷移
			return "user-update-success.jsp";
		} else {
			//更新失敗時：エラー画面へ遷移
			request.setAttribute("errorMessage", "会員情報の変更に失敗しました。");
			return "login-error.jsp";
		}
	}

}
