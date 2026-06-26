package jp.co.aforce.servlet;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import jp.co.aforce.dao.LotDAO;
import jp.co.aforce.tool.Action;

public class LotUpdateAction extends Action {

	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {

		try {
			// 1. JSPから送られてきたパラメータを取得
			// どのロットか(lotId)と、どのステータスにするか(newStatus)
			String lotIdStr = request.getParameter("lotId");
			String newStatusStr = request.getParameter("newStatus");
			String itemIdStr = request.getParameter("itemId");

			int lotId = Integer.parseInt(lotIdStr);
			int newStatus = Integer.parseInt(newStatusStr);
			int itemId = Integer.parseInt(itemIdStr);

			// 2. LotDAOを使用して、指定したロットの状態を個別更新
			LotDAO lotDAO = new LotDAO();
			lotDAO.updateSpecificLotStatus(lotId, newStatus);

			// 3. 処理完了後、PRGパターンに基づき「リダイレクト」で商品編集画面へ戻る
			return "redirect:ItemEdit.action?itemId=" + itemId;

		} catch (Exception e) {
			e.printStackTrace();
			request.setAttribute("errorTitle", "ロット更新エラー");
			request.setAttribute("errorMessage", "ロットの状態更新中に問題が発生しました。データベースの状態を確認してください。");
			request.setAttribute("errorBackUrl", "ItemEdit.action?itemId=" + request.getParameter("itemId"));
			request.setAttribute("errorBtnText", "編集画面へ戻る");
			return "login-error.jsp";
		}
	}
}
