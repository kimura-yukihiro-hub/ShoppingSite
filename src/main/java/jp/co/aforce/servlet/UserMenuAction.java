package jp.co.aforce.servlet;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import jp.co.aforce.beans.User;
import jp.co.aforce.dao.UserDAO;
import jp.co.aforce.tool.Action;

public class UserMenuAction extends Action {

	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {

		try {
			HttpSession session = request.getSession(false);

			if (session != null && session.getAttribute("loginUser") != null) {
				User loginUser = (User) session.getAttribute("loginUser");

				// 管理者権限のチェック
				if (loginUser.getMeatRank() == 5) {
					return "redirect:AdminMenu.action";
				}

				// 1. DBから最新の合計金額を取得し、Beanとセッションを更新
				UserDAO dao = new UserDAO();
				long latestTotal = dao.getTotalPurchaseAmount(loginUser.getMemberId());
				loginUser.setTotalPurchaseAmount(latestTotal);
				session.setAttribute("loginUser", loginUser);

				String rankUpMsg = (String) session.getAttribute("rankUpMessage");

				// 2. ランクアップ・降格判定
				// ランクアップ判定
				if (latestTotal >= loginUser.getNextRankGoal() && loginUser.getMeatRank() < 4) {
					int nextRank = loginUser.getMeatRank() + 1;
					dao.updateRank(loginUser.getMemberId(), nextRank);
					loginUser.setMeatRank(nextRank);
					session.setAttribute("loginUser", loginUser);
					request.setAttribute("rankUpMessage",
							"🎉 おめでとうございます！ランクが「" + loginUser.getMeatRankName() + "」に昇格しました！");
				}

				// メッセージをrequestにセット（JSP表示用）
				if (rankUpMsg != null) {
					request.setAttribute("rankUpMessage", rankUpMsg);
					// セッションはもう不要なので削除してクリーンにする
					session.removeAttribute("rankUpMessage");
				}

				// ランク降格判定
				if (loginUser.getMeatRank() > 1 && loginUser.getMeatRank() < 5
						&& loginUser.getLastPurchaseDate() != null) {
					long diff = System.currentTimeMillis() - loginUser.getLastPurchaseDate().getTime();
					long days = diff / (1000 * 60 * 60 * 24);

					if (days >= 180) {
						int newRank = loginUser.getMeatRank() - 1;
						dao.updateRank(loginUser.getMemberId(), newRank);
						loginUser.setMeatRank(newRank);
						session.setAttribute("loginUser", loginUser);
						request.setAttribute("rankDownMessage",
								"長期間のご利用がないため、ランクが「" + loginUser.getMeatRankName() + "」に調整されました。");
					}
				}

				// 3. 表示用データのセット
				long currentGoal = loginUser.getNextRankGoal();
				request.setAttribute("rankProgressPercent", loginUser.getProgressPercent());

				if (loginUser.getMeatRank() >= 4) {
					request.setAttribute("remainingMessage", "（最高ランクです）");
				} else {
					long remaining = Math.max(0, currentGoal - latestTotal);
					request.setAttribute("remainingMessage", "次のランクまであと " + remaining + " 円");
				}
			}
			return "user-menu.jsp";
		} catch (Exception e) {
			e.printStackTrace();
			request.setAttribute("errorTitle", "マイページ表示エラー");
			request.setAttribute("errorBackUrl", "ItemList.action");
			request.setAttribute("errorBtnText", "商品一覧へ戻る");
			request.setAttribute("errorMessage", "マイページの情報を取得中にシステムエラーが発生しました。");
			return "login-error.jsp";
		}
	}

}