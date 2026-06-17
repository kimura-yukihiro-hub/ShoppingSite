package jp.co.aforce.tool;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public abstract class Action {

	/**
	 *1. ログインチェックが必要な画面かどうかを判定するフラグ
	 * 
	 * 初期値は「true（ログイン必須）」
	 * ログイン画面や商品一覧画面など、ログインしていなくても入れる画面（Action）側だけ、
	 * コンストラクタでこのフラグを「false」に書き換えることで、検門を自由にスルーさせることができる
	 */
	protected boolean requireLogin = true;

	//2. フロントコントローラーから一番最初に呼び出される「自動検門所」メソッド

	public String start(HttpServletRequest request, HttpServletResponse response) throws Exception {

		try {

			// 1. この画面が「ログイン必須（true）」であり、かつ「未ログイン（false）」だった場合の鉄壁ブロック
			if (this.requireLogin && !isLoggedIn(request)) {
				// 安全のために、未ログインの直打ちゲストは一律でログインエラー画面へ強制送還します
				request.setAttribute("errorTitle", "ログインしていません");
				request.setAttribute("errorMessage", "ログインセッションの有効期限が切れたか、ログインしていません。");
				request.setAttribute("errorBackUrl", "views/login-in.jsp");
				request.setAttribute("errorBtnText", "ログイン画面へ");
				return "login-error.jsp";
			}

			// 2. 検門を無事に通過、もしくはログイン不要な画面(false)であれば、
			// 各子クラスが実装している本来のビジネスロジック（execute）を安全に実行
			return execute(request, response);

		} catch (Exception e) {
			// 子クラスの execute() の中で catch 漏れした例外や、予期せぬ重大なシステムクラッシュはここで最終キャッチ！
			e.printStackTrace();
			request.setAttribute("errorTitle", "システムエラー");
			request.setAttribute("errorMessage", "リクエストの処理中に、サーバー内部で予期せぬ重大なエラーが発生しました。");
			request.setAttribute("errorBackUrl", "views/login-in.jsp");
			request.setAttribute("errorBtnText", "ログイン画面へ戻る");
			return "login-error.jsp";
		}
	}

	//各Actionが実際に固有のビジネスロジックを記述する抽象メソッド

	public abstract String execute(HttpServletRequest request, HttpServletResponse response) throws Exception;

	// セッションの中に「loginUser」というBeansが正しく入っているかを判定する内部便利関数

	private boolean isLoggedIn(HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		if (session == null) {
			return false;
		}
		Object loginUser = session.getAttribute("loginUser");
		return loginUser != null;
	}
}
