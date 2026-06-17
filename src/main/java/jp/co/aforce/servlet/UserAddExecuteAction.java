package jp.co.aforce.servlet;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import jp.co.aforce.beans.User;
import jp.co.aforce.dao.UserDAO;
import jp.co.aforce.tool.Action;

public class UserAddExecuteAction extends Action {

	public UserAddExecuteAction() {
		this.requireLogin = false;
	}

	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {

		try {
			// 1. 確認画面のhiddenフォームから送信された値を取得
			String lastName = request.getParameter("lastName");
			String firstName = request.getParameter("firstName");
			String address = request.getParameter("address");
			String memberId = request.getParameter("memberId");
			String password = request.getParameter("password");
			String mailAddress = request.getParameter("mailAddress");
			String meatRankStr = request.getParameter("meatRank");

			// 万が一、不正アクセスでパラメータが消失していた場合の安全ガード
			if (memberId == null || password == null || memberId.trim().isEmpty()) {
				request.setAttribute("errorTitle", "登録セッションエラー");
				request.setAttribute("errorMessage", "登録データの送信中に情報の欠落が検出されました。恐れ入りますが最初からやり直してください。");
				return "login-error.jsp";
			}

			// 会員ランク(初期値1)を数値に変換して取得
			int meatRank = 1;
			if (meatRankStr != null && !meatRankStr.trim().isEmpty()) {
				try {
					meatRank = Integer.parseInt(meatRankStr.trim());
				} catch (NumberFormatException e) {
					meatRank = 1;
				}
			}

			// 2. 登録用のUserインスタンス(Bean)を生成してデータをセット
			User user = new User();
			user.setLastName(lastName);
			user.setFirstName(firstName);
			user.setAddress(address);
			user.setMailAddress(mailAddress);
			user.setMemberId(memberId);
			user.setPassword(password);
			user.setMeatRank(meatRank);

			UserDAO dao = new UserDAO();
			int count = 0;

			try {
				// 3. データベースへの挿入処理を実行
				count = dao.insert(user);
			} catch (java.sql.SQLIntegrityConstraintViolationException e) {
				// 一意制約違反（重複）は個別の親切メッセージで弾く
				request.setAttribute("errorTitle", "登録できませんでした");
				request.setAttribute("errorMessage", "入力された会員番号「" + memberId + "」は、タッチの差で別のユーザーに登録されたか、すでに使用されています。");
				return "login-error.jsp";
			}

			// 4. 実行結果に応じた画面遷移のコントロール
			if (count > 0) {
				// 古いセッションIDを消す
				HttpSession oldSession = request.getSession(false);
				if (oldSession != null) {
					oldSession.invalidate();
				}

				// 新しいセッションIDを生成し、データを同期
				HttpSession newSession = request.getSession(true);
				newSession.setAttribute("loginUser", user); // マイページの「ようこそ〇〇さん！」へ直通
				newSession.setMaxInactiveInterval(30 * 60); // ログイン状態を30分に設定

				// 自動ログイン状態で登録完了画面へフォワード
				return "user-add-success.jsp";

			} else {
				// INSERTの結果が0件だった場合の例外ケース
				request.setAttribute("errorTitle", "登録失敗");
				request.setAttribute("errorMessage", "データベースへの会員登録に失敗しました。入力された文字が長すぎる可能性があります。");
				return "login-error.jsp";
			}

		} catch (Exception e) {
			e.printStackTrace();
			request.setAttribute("errorTitle", "会員登録システムエラー");
			request.setAttribute("errorMessage", "会員情報のデータベース保存処理中に、サーバー側で予期せぬ重大なエラーが発生しました。");
			return "login-error.jsp";
		}
	}
}