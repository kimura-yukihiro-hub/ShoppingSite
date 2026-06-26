package jp.co.aforce.servlet;

import java.util.List;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import jp.co.aforce.beans.Lot;
import jp.co.aforce.dao.LotDAO;
import jp.co.aforce.tool.Action;

public class LotDetailAction extends Action {

	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {

		try {
			// パラメータから batchId を取得
			String batchId = request.getParameter("batchId");

			if (batchId == null || batchId.isEmpty()) {
				throw new Exception("バッチIDが指定されていません。");
			}

			// ロット詳細情報の取得
			LotDAO lotDAO = new LotDAO();
			List<Lot> details = lotDAO.getLotDetailsByBatchId(batchId);

			// 取得した詳細リストをJSPへ渡す
			request.setAttribute("lotDetails", details);

			// モーダル表示用の断片JSPへフォワード
			return "lot-detail-fragment.jsp";

		} catch (Exception e) {
			e.printStackTrace();
			request.setAttribute("errorTitle", "データ取得エラー");
			request.setAttribute("errorMessage", "詳細情報の読み込み中にエラーが発生しました。");
			request.setAttribute("errorBackUrl", "javascript:closeModal()");
			request.setAttribute("errorBtnText", "閉じる");
			return "login-error.jsp";
		}
	}

}
