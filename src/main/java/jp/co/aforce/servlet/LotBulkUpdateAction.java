package jp.co.aforce.servlet;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import jp.co.aforce.dao.LotDAO;
import jp.co.aforce.tool.Action;

public class LotBulkUpdateAction extends Action {

	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {

		// 1. パラメータの取得
		String[] batchIds = request.getParameterValues("batchIds");
		String newStatusStr = request.getParameter("newStatus");
		String itemIdStr = request.getParameter("itemId");

		if (batchIds == null || newStatusStr == null || itemIdStr == null) {
			return "redirect:ItemEdit.action?itemId=" + itemIdStr;
		}

		// 2. 処理の実行
		try {
			int newStatus = Integer.parseInt(newStatusStr);
			;
			LotDAO lotDAO = new LotDAO();

			for (String batchId : batchIds) {
				lotDAO.updateStatusByBatchId(batchId, newStatus);
			}

			// 成功時は編集画面へ
			return "redirect:ItemEdit.action?itemId=" + itemIdStr;

		} catch (Exception e) {
			e.printStackTrace();
			request.setAttribute("errorTitle", "更新失敗");
			request.setAttribute("errorMessage", "ロットの状態更新中にエラーが発生しました。");
			request.setAttribute("errorBackUrl", "ItemEdit.action?itemId=" + itemIdStr);
			request.setAttribute("errorBtnText", "編集画面に戻る");
			return "login-error.jsp";
		}
	}

}
