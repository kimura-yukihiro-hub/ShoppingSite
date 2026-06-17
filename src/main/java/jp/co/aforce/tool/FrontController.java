package jp.co.aforce.tool;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("*.action")
@jakarta.servlet.annotation.MultipartConfig(fileSizeThreshold = 1024 * 1024 * 2, // 2MB
		maxFileSize = 1024 * 1024 * 10, // 10MB
		maxRequestSize = 1024 * 1024 * 50 // 50MB
)
public class FrontController extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		try {
			// リクエストおよびレスポンスの文字化けを防止するエンコーディングの確定
			request.setCharacterEncoding("UTF-8");
			response.setCharacterEncoding("UTF-8");

			String path = request.getServletPath();

			// 1. 管理者専用ページのガードレール
			HttpSession session = request.getSession(false);
			if (session != null && session.getAttribute("loginUser") != null) {
				jp.co.aforce.beans.User loginUser = (jp.co.aforce.beans.User) session.getAttribute("loginUser");

				// URLパスが "/admin" から始まる特権エリアへの不正進入をブロック
				if (path.toLowerCase().startsWith("/admin")) {
					if (loginUser.getMeatRank() != 5) {
						request.setAttribute("errorTitle", "アクセス権限がありません");
						request.setAttribute("errorMessage", "このページは最高級管理者専用です。一般ユーザーのアカウントではアクセスできません。");
						request.setAttribute("errorBackUrl", "UserMenu.action");
						request.setAttribute("errorBtnText", "マイページへ戻る");
						request.getRequestDispatcher("/views/login-error.jsp").forward(request, response);
						return;
					}
				}
			}

			// 2. URLをクラス名（Action名）にパースして特定する分岐処理
			String actionName = path.substring(1).replace(".action", "");
			String className;

			if (actionName.equalsIgnoreCase("itemdetail")) {
				className = "ItemDetailAction";
			} else if (actionName.equalsIgnoreCase("itemlist")) {
				className = "ItemListAction";
			} else if (actionName.equalsIgnoreCase("login")) {
				className = "LoginAction";
			} else if (actionName.equalsIgnoreCase("itemdelete")) {
				className = "ItemDeleteAction";
			} else if (actionName.equalsIgnoreCase("itemedit")) {
				className = "ItemEditAction";
			} else if (actionName.equalsIgnoreCase("itemupdate")) {
				className = "ItemUpdateAction";
			} else if (actionName.equalsIgnoreCase("adminsales")) {
				className = "AdminSalesAction";
			} else {
				className = actionName.substring(0, 1).toUpperCase() + actionName.substring(1) + "Action";
			}

			// 3. Javaのリフレクション（動的生成）を使って、文字列から対象のActionクラスを実体化
			Action action = (Action) Class.forName("jp.co.aforce.servlet." + className)
					.getDeclaredConstructor()
					.newInstance();

			// 4. 親クラス（Action）に定義された自動検門所メソッド「start」を起動！
			String url = action.start(request, response);

			// 5. Actionクラスから返ってきた戻り値の文字列に応じて、フォワードとリダイレクトを鮮やかに切り替え
			if (url.startsWith("redirect:")) {
				// 重複送信（ブラウザのリロードによる二重決済や二重登録）を防ぐためのリダイレクト
				response.sendRedirect(request.getContextPath() + "/" + url.replace("redirect:", ""));
			} else {
				// 通常の画面表示用フォワード（共通して /views/ フォルダ内のJSPへ案内）
				request.getRequestDispatcher("/views/" + url).forward(request, response);
			}

		} catch (ClassNotFoundException e) {
			// URLに対応するActionクラス（Javaファイル）が物理的に見つからなかった場合の404系エラーガード
			e.printStackTrace();
			request.setAttribute("errorTitle", "ページが見つかりません");
			request.setAttribute("errorMessage", "リクエストされたURLに対応するアクション（" + e.getMessage() + "）が存在しないか、現在メンテナンス中です。");
			request.setAttribute("errorBackUrl", "login-in.jsp");
			request.setAttribute("errorBtnText", "トップ画面へ戻る");
			forwardToErrorPage(request, response);

		} catch (Exception e) {
			// それ以外の未知のシステムエラーや致命的なクラッシュはここで一網打尽
			e.printStackTrace();
			request.setAttribute("errorTitle", "システムエラー");
			request.setAttribute("errorMessage", "大変申し訳ありません。リクエストの転送処理中にコントローラー層で予期せぬ重大な例外が発生しました。");
			request.setAttribute("errorBackUrl", "login-in.jsp");
			request.setAttribute("errorBtnText", "トップ画面へ戻る");
			forwardToErrorPage(request, response);
		}
	}

	//エラー画面（login-error.jsp）へのフォワードを安全に実行する内部共通補助メソッド

	private void forwardToErrorPage(HttpServletRequest request, HttpServletResponse response) {
		try {
			request.getRequestDispatcher("/views/login-error.jsp").forward(request, response);
		} catch (Exception ex) {
			ex.printStackTrace(); // 万が一JSPそのものが消失していた場合の最終ログ出力
		}
	}
}