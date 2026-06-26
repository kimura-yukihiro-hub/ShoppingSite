package jp.co.aforce.servlet;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import jp.co.aforce.beans.User;
import jp.co.aforce.dao.UserDAO;
import jp.co.aforce.tool.Action;

public class LoginAction extends Action {

	public LoginAction() {
		this.requireLogin = false;
	}

	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {

		try {

			//JSPのフォームから送信された「会員番号」とパスワード」を取得
			String memberId = request.getParameter("memberId");
			String password = request.getParameter("password");

			if (memberId == null || password == null || memberId.trim().isEmpty() || password.trim().isEmpty()) {
				request.setAttribute("errorTitle", "入力エラー");
				request.setAttribute("errorMessage", "会員番号とパスワードを入力してください。");
				request.setAttribute("errorBackUrl", "views/login-in.jsp");
				request.setAttribute("errorBtnText", "ログイン画面へ戻る");
				return "login-error.jsp";
			}

			if (!memberId.matches("^[a-zA-Z0-9\\-]+$") || !password.matches("^[a-zA-Z0-9\\-_#\\$%]+$")) {
				// もしJavaScriptをすり抜けて不正な文字が届いたら、即座にエラー画面へ突き返す
				request.setAttribute("errorTitle", "入力内容エラー");
				request.setAttribute("errorMessage", "使用できない文字が含まれています。");
				request.setAttribute("errorBackUrl", "views/login-in.jsp");
				request.setAttribute("errorBtnText", "ログイン画面へ戻る");
				return "login-error.jsp";
			}
			//UserDAOを呼び出して一致する会員を検索
			UserDAO dao = new UserDAO();
			User loginUser = dao.search(memberId, password);

			//検索結果に応じた画面遷移のコントロール
			if (loginUser != null) {

				//二重ログインのブロック処理
				if (jp.co.aforce.listener.LoginUserSessionListener.isLoggedIn(memberId)) {
					// すでに別の場所でログインされている場合は、専用のエラー画面へ移動させる
					request.setAttribute("errorTitle", "二重ログイン禁止");
					request.setAttribute("errorMessage", "このアカウントはすでに別の端末でログイン中です。");
					request.setAttribute("errorBackUrl", "views/login-in.jsp");
					request.setAttribute("errorBtnText", "ログイン画面へ戻る");
					return "login-error.jsp";
				}

				// 購入合計金額をDBから取得してBeanにセット
				long totalAmount = dao.getTotalPurchaseAmount(loginUser.getMemberId());
				loginUser.setTotalPurchaseAmount(totalAmount);

				// ログイン成功の時
				HttpSession session = request.getSession();
				session.setAttribute("loginUser", loginUser);
				// ログインしたまま画面を30分放置すると、自動的にログインの記憶が消滅する安全装置が発動する
				session.setMaxInactiveInterval(30 * 60);

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
				request.setAttribute("errorTitle", "ログイン失敗");
				request.setAttribute("errorMessage", "会員番号もしくはパスワードが違います。");
				request.setAttribute("errorBackUrl", "views/login-in.jsp");
				request.setAttribute("errorBtnText", "ログイン画面へ戻る");
				return "login-error.jsp";
			}
		} catch (Exception e) {
			e.printStackTrace();
			request.setAttribute("errorTitle", "ログインシステムエラー");
			request.setAttribute("errorMessage", "認証処理中に、サーバーまたはデータベースで予期せぬ重大なエラーが発生しました。時間をおいて再度お試しください。");
			request.setAttribute("errorBackUrl", "views/login-in.jsp");
			request.setAttribute("errorBtnText", "ログイン画面へ戻る");
			return "login-error.jsp";
		}
	}
}
