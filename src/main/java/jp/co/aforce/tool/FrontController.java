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
			if (!path.equals("/Login.action")) {

				// 現在のセッションの部屋を取得（存在しない場合は新しく作らない）
				HttpSession session = request.getSession(false);

				// セッション自体がない、またはセッションの中にログインユーザーの記憶（loginUser）がない場合
				if (session == null || session.getAttribute("loginUser") == null) {
					request.getRequestDispatcher("/views/login-in.jsp").forward(request, response);
					return;
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
