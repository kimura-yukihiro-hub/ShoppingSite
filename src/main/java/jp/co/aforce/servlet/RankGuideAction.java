package jp.co.aforce.servlet;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import jp.co.aforce.beans.User;
import jp.co.aforce.dao.UserDAO;
import jp.co.aforce.tool.Action;

public class RankGuideAction extends Action {

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {

		try {
			HttpSession session = request.getSession(false);
			if (session == null || session.getAttribute("loginUser") == null) {
				return "login.jsp";
			}

			User loginUser = (User) session.getAttribute("loginUser");

			// 1. 最新の購入実績をDBから取得し、UserBeanを更新
			UserDAO dao = new UserDAO();
			long latestTotal = dao.getTotalPurchaseAmount(loginUser.getMemberId());
			loginUser.setTotalPurchaseAmount(latestTotal);
			session.setAttribute("loginUser", loginUser);

			// 2. ランク特典ページ用データの準備
			long currentGoal = loginUser.getNextRankGoal();

			// ゲージのパーセント計算
			request.setAttribute("rankProgressPercent", loginUser.getProgressPercent());

			// 次のランクまでのメッセージ生成
			if (loginUser.getMeatRank() >= 4) {
				request.setAttribute("remainingMessage", "（最高ランクです）");
			} else {
				long remaining = Math.max(0, currentGoal - latestTotal);
				request.setAttribute("remainingMessage", "次のランクまであと " + remaining + " 円");
			}

			// 3. 画面表示へ
			return "rank-guide.jsp";

		} catch (Exception e) {
			e.printStackTrace();
			request.setAttribute("errorTitle", "特典情報表示エラー");
			request.setAttribute("errorBackUrl", "UserMenu.action");
			request.setAttribute("errorBtnText", "マイページへ戻る");
			request.setAttribute("errorMessage", "特典情報の読み込み中にシステムエラーが発生しました。");
			return "login-error.jsp";
		}
	}
}