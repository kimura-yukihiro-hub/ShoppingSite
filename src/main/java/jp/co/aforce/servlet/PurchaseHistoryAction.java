package jp.co.aforce.servlet;

import java.util.List;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import jp.co.aforce.beans.Purchase;
import jp.co.aforce.beans.User;
import jp.co.aforce.dao.LotDAO;
import jp.co.aforce.dao.PurchaseDAO;
import jp.co.aforce.tool.Action;

public class PurchaseHistoryAction extends Action {

	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {

		try {
			// 1. セッションから本人情報を取得
			HttpSession session = request.getSession(false);
			User loginUser = (User) session.getAttribute("loginUser");

			// 2. DAOを使って本人の履歴を取得
			PurchaseDAO dao = new PurchaseDAO();
			List<Purchase> history = dao.getPurchaseHistory(loginUser.getMemberId());
			
			// ロットDAOを使って、取得した履歴一つひとつに「ロット情報」を紐づける
			LotDAO lotDAO = new LotDAO();
			for (Purchase p : history) {
				// 購入IDに紐づくシリアル番号を検索してセットする
				String serial = lotDAO.getSerialByPurchaseId(p.getPurchaseId());
				p.setSerialNumber(serial);
			}

			// 3. JSPへリストを渡す
			request.setAttribute("history", history);

			// 4. 購入履歴表示画面へ
			return "purchase-history.jsp";

		} catch (Exception e) {
			e.printStackTrace();
			request.setAttribute("errorTitle", "購入履歴表示エラー");
			request.setAttribute("errorMessage", "現在、購入履歴を読み込むことができませんでした。");
			request.setAttribute("errorBackUrl", "UserMenu.action");
			request.setAttribute("errorBtnText", "マイページへ戻る");

			return "login-error.jsp";
		}
	}

}
