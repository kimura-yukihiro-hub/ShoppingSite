package jp.co.aforce.servlet;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import jp.co.aforce.beans.User;
import jp.co.aforce.dao.UserDAO;
import jp.co.aforce.tool.Action;

public class UserAddAction extends Action {

	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {

		try {

			//1. JSPのフォームから送信された入力値を取得
			String lastName = request.getParameter("lastName");
			String firstName = request.getParameter("firstName");
			String zipCode = request.getParameter("zipCode");
			String address = request.getParameter("address");
			String memberId = request.getParameter("memberId");
			String password = request.getParameter("password");
			String mailAddress = request.getParameter("mailAddress");

			//2. サーバーサイドでのバリデーション（二重チェック）
			String errorMessage = null;

			if (memberId == null || password == null || mailAddress == null ||
					lastName == null || firstName == null || address == null ||
					memberId.isEmpty() || password.isEmpty() || mailAddress.isEmpty() ||
					lastName.isEmpty() || firstName.isEmpty() || address.isEmpty()) {

				errorMessage = "必須項目が入力されていません。";
			}
			// 「半角・全角スペース」が万が一混入していた場合をシャットアウト
			else if (memberId.contains(" ") || memberId.contains("　") ||
					password.contains(" ") || password.contains("　") ||
					mailAddress.contains(" ") || mailAddress.contains("　") ||
					lastName.contains(" ") || lastName.contains("　") ||
					firstName.contains(" ") || firstName.contains("　")) {

				errorMessage = "入力項目にスペース（空白）は使用できません。";
			}
			// パスワードの4文字以上制限のサーバーサイド二重チェック
			else if (password.length() < 4) {
				errorMessage = "パスワードは4文字以上で入力してください。";
			}
			// 会員番号の正規表現チェック（半角英数字とハイフンのみ）
			else if (!memberId.matches("^[a-zA-Z0-9\\-]+$")) {
				errorMessage = "会員番号は半角英数字とハイフン（-）のみ使用できます。";
			}
			// パスワードの文字種制限（半角英数字と指定記号: - _ . # $ %）
			else if (!password.matches("^[a-zA-Z0-9\\-_#\\$%]+$")) {
				errorMessage = "パスワードに使用できない文字が含まれています。";
			}
			// メールアドレスの簡易的な形式チェック（JSPのすり抜け防止）
			else if (!mailAddress.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")) {
				errorMessage = "メールアドレスの形式が正しくありません。";
			}
			//郵便番号が入力されている場合のみ、形式チェックを行う（任意項目のため）
			else if (zipCode != null && !zipCode.isEmpty()) {
				// 半角数字7桁、またはハイフンあり（3桁-4桁）の正規表現チェック
				if (!zipCode.matches("^\\d{3}-?\\d{4}$")) {
					errorMessage = "郵便番号の形式が正しくありません（例: 1000001 または 100-0001）。";
				}
			}

			// バリデーションエラーがあった場合は、それぞれの具体的な原因（errorMessage）をそのまま画面へ渡す
			if (errorMessage != null) {
				request.setAttribute("errorTitle", "登録できませんでした");
				request.setAttribute("errorBackUrl", "views/user-add.jsp"); // 新規登録JSPへ戻す
				request.setAttribute("errorBtnText", "登録画面へ戻る");
				request.setAttribute("errorMessage", errorMessage); // 💡具体的なエラーメッセージをセット
				return "login-error.jsp";
			}

			//データベースの会員番号重複チェック
			UserDAO dao = new UserDAO();
			if (dao.checkDuplicate(memberId)) {
				request.setAttribute("errorTitle", "登録できませんでした");
				request.setAttribute("errorBackUrl", "views/user-add.jsp"); // 新規登録JSPへ直接戻す
				request.setAttribute("errorBtnText", "登録画面へ戻る");
				// すでに同じ会員番号（ID）が使われている場合は、エラーメッセージをセットして突き返す
				request.setAttribute("errorMessage", "入力された会員番号「" + memberId + "」はすでに使用されています。");
				return "login-error.jsp";
			}

			//3. 取得したデータをUser.java(Beans)に詰め込む
			User newUser = new User();
			newUser.setLastName(lastName);
			newUser.setFirstName(firstName);
			newUser.setZipCode(zipCode);
			newUser.setAddress(address);
			newUser.setMailAddress(mailAddress);
			newUser.setMemberId(memberId);
			newUser.setPassword(password);
			newUser.setMeatRank(1); // 会員ランクの初期設定は1に固定

			//4. 次の確認画面JSPで表示できるように、リクエスト属性にBeanを格納
			request.setAttribute("newUser", newUser);

			//5. 登録確認画面のJSP名を返す
			return "user-add-check.jsp";

		} catch (Exception e) {
			e.printStackTrace();
			request.setAttribute("errorTitle", "会員登録システムエラー");
			request.setAttribute("errorBackUrl", "views/user-add.jsp");
			request.setAttribute("errorBtnText", "登録画面へ戻る");
			request.setAttribute("errorMessage", "会員情報の検証中に、サーバーまたはデータベースで予期せぬ重大なエラーが発生しました。");
			return "login-error.jsp";
		}
	}
}
