package jp.co.aforce.servlet;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import jp.co.aforce.beans.User;
import jp.co.aforce.tool.Action;

public class UserUpdateAction extends Action {

	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {

		try {

			//1.フォーム(user-update.jsp)から送信された変更後の値を取得
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
			} else {
				// 万が一セッションが切れていた場合の安全ガード
				request.setAttribute("errorTitle", "セッションエラー");
				request.setAttribute("errorMessage", "長期間操作がなかったため、セッションが切断されました。もう一度ログインしてください。");
				return "login-error.jsp";
			}

			//2. サーバーサイドでのバリデーション
			String errorMessage = null;

			if (password == null || mailAddress == null ||
					lastName == null || firstName == null || address == null ||
					password.isEmpty() || mailAddress.isEmpty() ||
					lastName.isEmpty() || firstName.isEmpty() || address.isEmpty()) {

				errorMessage = "必須項目が入力されていません。";
			}
			//「半角・全角スペース」が万が一混入していた場合をシャットアウト(住所だけ許容)
			else if (password.contains(" ") || password.contains("　") ||
					mailAddress.contains(" ") || mailAddress.contains("　") ||
					lastName.contains(" ") || lastName.contains("　") ||
					firstName.contains(" ") || firstName.contains("　")) {

				errorMessage = "入力項目にスペース(空白)は使用できません。";
			}
			//パスワードの4文字以上制限のサーバーサイド二重チェック
			else if (password.length() < 4) {
				errorMessage = "パスワードは4文字以上で入力してください。";
			}
			//パスワードの文字種制限（半角英数字と指定記号: - _ . # $ %）
			else if (!password.matches("^[a-zA-Z0-9\\-_#\\$%]+$")) {
				errorMessage = "新しいパスワードに使用できない文字が含まれています。";
			}
			//メールアドレスの簡易的な形式チェック（JSPのすり抜け防止）
			else if (!mailAddress.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")) {
				errorMessage = "メールアドレスの形式が正しくありません。";
			}

			//バリデーションエラーがあった場合は、安全にマイページまたはエラー画面へ引き戻す
			if (errorMessage != null) {
				request.setAttribute("errorTitle", "変更できませんでした");
				request.setAttribute("errorBackUrl", "UserMenu.action"); // 安全にマイページへ戻します
				request.setAttribute("errorBtnText", "マイページへ戻る");

				request.setAttribute("errorMessage", errorMessage);
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
			User currentUser = (User) session.getAttribute("loginUser");
			if (currentUser != null) {
				updatedUser.setMeatRank(currentUser.getMeatRank());
			}

			//4. 辺区確認画面で表示できるようにリクエストを格納
			request.setAttribute("updatedUser", updatedUser);

			//5. 変更確認画面のJSP名を返す
			return "user-update-check.jsp";
		} catch (Exception e) {
			e.printStackTrace();
			request.setAttribute("errorTitle", "会員情報変更エラー");
			request.setAttribute("errorBackUrl", "UserMenu.action");
			request.setAttribute("errorBtnText", "マイページへ戻る");
			request.setAttribute("errorMessage", "会員情報の検証処理中に、サーバー側で予期せぬ重大なエラーが発生しました。");
			return "login-error.jsp";
		}
	}
}