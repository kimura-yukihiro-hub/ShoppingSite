package jp.co.aforce.tool;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * Servlet implementation class FrontController
 */
@WebServlet("*.action")
public class FrontController extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response); // doGetが呼ばれてもdoPostにバトンタッチする
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		try {
			String path = request.getServletPath();
			if (!path.equalsIgnoreCase("/Login.action") && !path.equalsIgnoreCase("/UserAdd.action")
					&& !path.equalsIgnoreCase("/UserAddExecute.action")) {

				// 現在のセッションの部屋を取得（存在しない場合は新しく作らない）
				HttpSession session = request.getSession(false);

				// セッション自体がない、またはセッションの中にログインユーザーの記憶（loginUser）がない場合
				if (session == null || session.getAttribute("loginUser") == null) {
					request.getRequestDispatcher("/views/login-in.jsp").forward(request, response);
					return;
				}

				//管理者画面の不正アクセス防止ガード(認可チェック)
				// 1. セッションからログインしているユーザーの情報をBeanとして取得
				jp.co.aforce.beans.User loginUser = (jp.co.aforce.beans.User) session.getAttribute("loginUser");

				//2. ブラウザが要求してきたURL（アクション名）が「/Admin」から始まっているか判定
				if (path.startsWith("/Admin")) {

					//3. もし管理者用URLなのに、肉ランクが「5(管理者)」でなければ即座にブロック
					if (loginUser.getMeatRank() != 5) {
						request.setAttribute("errorTitle", "アクセス権限がありません");
						request.setAttribute("errorMessage", "このページは管理者専用です。一般ユーザーはアクセスできません。");
						request.setAttribute("errorBackUrl", "UserMenu.action"); // ボタンを押したら安全に一般マイページへ戻す
						request.setAttribute("errorBtnText", "マイページへ戻る");

						// データベースや本来のActionクラスへ一切触れさせずにエラー画面へフォワード
						request.getRequestDispatcher("/views/login-error.jsp").forward(request, response);
						return; // ここで通信を強制終了させてハッカーをシャットアウト
					}
				}
			}
			String name = "jp.co.aforce.servlet." + path.substring(1).replace(".action", "Action");

			// リフレクションで、文字列から自動的にActionクラスを実体化
			Action action = (Action) Class.forName(name).getDeclaredConstructor().newInstance();

			// 各Actionのexecute()メソッドを呼び出す
			String url = action.execute(request, response);
			if (url != null) {
				if (url.startsWith("redirect:")) {
					// 「redirect:」の文字を切り取って、そのURLへリダイレクトさせる
					String redirectUrl = url.replace("redirect:", "");
					response.sendRedirect(request.getContextPath() + "/" + redirectUrl);
				} else {
					request.getRequestDispatcher("/views/" + url).forward(request, response);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			//自分では解決できない重大なエラー（例外）が発生したときに、それをTomcatに報告して、処理を安全に強制終了させる命令
			throw new ServletException(e);
		}
	}

}
