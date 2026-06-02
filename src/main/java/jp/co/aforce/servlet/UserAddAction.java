package jp.co.aforce.servlet;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import jp.co.aforce.beans.User;
import jp.co.aforce.dao.UserDAO;
import jp.co.aforce.tool.Action;

public class UserAddAction extends Action {

	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {

		//1. JSPのフォームから送信された入力値を取得
		String lastName = request.getParameter("lastName");
		String firstName = request.getParameter("firstName");
		String address = request.getParameter("address");
		String memberId = request.getParameter("memberId");
		String password = request.getParameter("password");
		String mailAddress = request.getParameter("mailAddress");

		//2. 入力値の簡易バリデーション(不正な文字入力をチェック)
		if (!memberId.matches("^[a-zA-Z0-9\\\\-]+$") || !password.matches("^[a-zA-Z0-9]+$")) {
			request.setAttribute("errorMessage", "不正な入力が検出されました。");
			return "login-error.jsp";
		}
		
		//データベースの会員番号重複チェック
		UserDAO dao = new UserDAO();
		if (dao.checkDuplicate(memberId)) {
			// すでに同じ会員番号（ID）が使われている場合は、エラーメッセージをセットして突き返す
			request.setAttribute("errorMessage", "入力された会員番号「" + memberId + "」はすでに使用されています。");
			return "login-error.jsp";
		}

		//3. 取得したデータをUser.java(Beans)に詰め込む
		User newUser = new User();
		newUser.setLastName(lastName);
		newUser.setFirstName(firstName);
		newUser.setAddress(address);
		newUser.setMailAddress(mailAddress);
		newUser.setMemberId(memberId);
		newUser.setPassword(password);
		newUser.setMeatRank(1); // 会員ランクの初期設定は1に固定

		//4. 次の確認画面JSPで表示できるように、リクエスト属性にBeanを格納
		request.setAttribute("newUser", newUser);

		//5. 登録確認画面のJSP名を返す
		return "user-add-check.jsp";

	}

}
