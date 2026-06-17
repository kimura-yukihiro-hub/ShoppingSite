package jp.co.aforce.servlet;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import jp.co.aforce.beans.User;
import jp.co.aforce.dao.UserDAO;
import jp.co.aforce.tool.Action;


 //新規管理者の登録処理を実行するアクションクラス
 
public class AdminUserAddExecuteAction extends Action {

	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {

		// 1. フォームからの入力値を受け取る
		String memberId = request.getParameter("memberId");
		String password = request.getParameter("password");
		String passwordConfirm = request.getParameter("passwordConfirm");
		String lastName = request.getParameter("lastName");
		String firstName = request.getParameter("firstName");
		String mailAddress = request.getParameter("mailAddress");
		String postCode = request.getParameter("postCode");
		String address = request.getParameter("address");
		String currentAdminPassword = request.getParameter("currentAdminPassword");

		// 2. エラー時にJSP側で値を保持（キープ）するための仕込み
		request.setAttribute("keep_memberId", memberId);
		request.setAttribute("keep_lastName", lastName);
		request.setAttribute("keep_firstName", firstName);
		request.setAttribute("keep_mailAddress", mailAddress);
		request.setAttribute("keep_postCode", postCode);
		request.setAttribute("keep_address", address);

		// 3. 【親クラスの保証】ここへ到達した時点でログイン済みは確定している
		//    操作中の管理者（自分自身）の情報をセッションから取得
		HttpSession session = request.getSession(false);
		User loginAdmin = (User) session.getAttribute("loginUser");

		// 4. サーバーサイド・バリデーションチェック（未入力チェック）
		if (memberId == null || memberId.trim().isEmpty() ||
				password == null || password.trim().isEmpty() ||
				passwordConfirm == null || passwordConfirm.trim().isEmpty() ||
				lastName == null || lastName.trim().isEmpty() ||
				firstName == null || firstName.trim().isEmpty() ||
				mailAddress == null || mailAddress.trim().isEmpty() ||
				address == null || address.trim().isEmpty() ||
				currentAdminPassword == null || currentAdminPassword.trim().isEmpty()) {

			// メッセージの内容を正しい未入力警告に修正
			request.setAttribute("errorMessage", "⚠️ 未入力の必須項目があります。すべての項目を正しく入力してください。");
			return "admin-user-add.jsp";
		}

		// パスワード一致チェック
		if (!password.equals(passwordConfirm)) {
			request.setAttribute("errorMessage", "⚠️ 新規パスワードと確認用パスワードが一致しません。");
			return "admin-user-add.jsp";
		}

		// 操作中の管理者（自分自身）のパスワード確認（本人確認）
		if (!loginAdmin.getPassword().equals(currentAdminPassword)) {
			request.setAttribute("errorMessage", "⚠️ あなたの現在のパスワード（本人確認）が正しくありません。");
			return "admin-user-add.jsp";
		}

		try {
			// 5. UserDAOの実体化
			UserDAO dao = new UserDAO();

			// 重複チェックメソッド（checkDuplicate）を呼び出し
			if (dao.checkDuplicate(memberId)) {
				request.setAttribute("errorMessage", "⚠️ このログインIDはすでに登録されています。別のIDを指定してください。");
				return "admin-user-add.jsp";
			}

			// 6. 新規管理者用のUserインスタンス（Bean）を生成してデータをセット
			User newUser = new User();
			newUser.setMemberId(memberId);
			newUser.setPassword(password);
			newUser.setLastName(lastName);
			newUser.setFirstName(firstName);
			newUser.setAddress(address);
			newUser.setMailAddress(mailAddress);
			newUser.setMeatRank(5); // 💡最高級管理者の「肉ランク5」を確定

			// 7. データベースへの登録実行
			int count = dao.insert(newUser);

			// 8. 実行結果に応じた画面遷移
			if (count > 0) {
				request.setAttribute("successMessage", "🎉 新しい管理者を正常に登録しました。");
				return "redirect:AdminMenu.action";
			} else {
				request.setAttribute("errorMessage", "⚠️ データベースの登録に失敗しました。時間をおいて再度お試しください。");
				return "admin-user-add.jsp";
			}

		} catch (Exception e) {
			e.printStackTrace();
			request.setAttribute("errorTitle", "管理者登録エラー");
			request.setAttribute("errorMessage", "新しい管理者の追加処理中に、データベースで予期せぬエラーが発生しました。");
			request.setAttribute("errorBackUrl", "AdminAddForm.action");
			request.setAttribute("errorBtnText", "管理者登録画面へ戻る");
			return "login-error.jsp";
		}
	}
}