package jp.co.aforce.servlet;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import jp.co.aforce.beans.User;
import jp.co.aforce.tool.Action;

public class UserUpdateAction extends Action {

	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {

		//1.フォーム(user-update.jsp)から総ぢンされた変更後の値を取得
		String memberId = request.getParameter("memberId");
		String lastName = request.getParameter("lastName");
		String firstName = request.getParameter("firstName");
		String address = request.getParameter("address");
		String mailAddress = request.getParameter("mailAddress");
		String password = request.getParameter("password");
		String currentPassword = request.getParameter("currentPassword");

		//現在のパスワードを用いた本人確認チェック
		HttpSession session = request.getSession(false);
		if (session != null && session.getAttribute("loginUser") != null) {
			User loginUser = (User) session.getAttribute("loginUser");

			// セッションに記憶されている現在のパスワードと、今入力されたパスワードが一致しない場合
			if (!loginUser.getPassword().equals(currentPassword)) {
				request.setAttribute("errorTitle", "変更できませんでした");
				request.setAttribute("errorBackUrl", "UserUpdateForm.action"); // 修正入力画面へ直接引き戻す
				request.setAttribute("errorBtnText", "修正画面へ戻る");
				request.setAttribute("errorMessage", "「現在のパスワード」が正しくありません。本人確認に失敗しました。");
				return "login-error.jsp";
			}
		}

		//2. パスワードの安全なバリデーション
		if (password == null || !password.matches("^[a-zA-Z0-9\\-_\\.#\\$%]+$"))

		{
			request.setAttribute("errorTitle", "変更できませんでした");
			request.setAttribute("errorBackUrl", "UserMenu.action"); // 安全にマイページへ戻します
			request.setAttribute("errorBtnText", "マイページへ戻る");

			request.setAttribute("errorMessage", "不正な入力が検出されました。");
			return "login-error.jsp";
		}

		//3. 変更データを一時的に保存するUserインスタンス(Bean)を生成
		User updatedUser = new User();
		updatedUser.setMemberId(memberId);
		updatedUser.setLastName(lastName);
		updatedUser.setFirstName(firstName);
		updatedUser.setAddress(address);
		updatedUser.setMailAddress(mailAddress);
		updatedUser.setPassword(password);

		//会員ランクは既存の状態を維持するため、切歯音の現在の値を引き継ぐ

		//4. 辺区確認画面で表示できるようにリクエストを格納
		request.setAttribute("updatedUser", updatedUser);

		//5. 変更確認画面のJSP名を返す
		return "user-update-check.jsp";
	}
}