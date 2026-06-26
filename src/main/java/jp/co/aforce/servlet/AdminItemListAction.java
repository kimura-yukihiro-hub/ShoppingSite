package jp.co.aforce.servlet;

import java.util.List;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import jp.co.aforce.beans.Item;
import jp.co.aforce.dao.ItemDAO;
import jp.co.aforce.dao.LotDAO;
import jp.co.aforce.tool.Action;

//管理者専用の商品一覧画面（admin-item-list.jsp）への表示遷移を管理するアクションクラス

public class AdminItemListAction extends Action {

	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {

		try {
			// 1. ItemDAOを実体化
			ItemDAO itemDao = new ItemDAO();

			// 2. 検索条件をすべて null に指定して、データベースから登録されている全商品リストを取得
			List<Item> adminItemList = itemDao.search(null, null, null, null, 0);

			// 万が一、データが取れなかった場合（nullチェック）
			if (adminItemList == null) {
				request.setAttribute("errorTitle", "データ取得エラー");
				request.setAttribute("errorMessage", "商品一覧データの取得に失敗しました。");
				request.setAttribute("errorBackUrl", "AdminMenu.action");
				request.setAttribute("errorBtnText", "管理者メニューへ戻る");
				return "login-error.jsp";
			}

			// 各商品の在庫をロット情報から再計算して上書きする
			LotDAO lotDAO = new LotDAO();
			for (Item item : adminItemList) {
				// ステータス0（生）と1（熟成）を別々にカウント
				int rawStock = lotDAO.getStockByStatus(item.getItemId(), 0);
				int agedStock = lotDAO.getStockByStatus(item.getItemId(), 1);

				// アイテムオブジェクトに値を保持させる（またはリクエストに個別セット）
				item.setStock(rawStock + agedStock); // 合計は合計で持っておく
				item.setRawStock(rawStock); // 生の数
				item.setAgedStock(agedStock); // 熟成の数
			}

			// 3. 取得した全商品リストを、リクエスト属性「adminItemList」という名前で格納
			request.setAttribute("adminItemList", adminItemList);

			// 4. 正常なら、管理者専用の商品一覧画面のJSPファイル名だけを返す
			return "admin-item-list.jsp";

		} catch (Exception e) {
			// 例外が発生したら、ログを出力してリクエストに「独自のメッセージ」を詰め込みます。
			e.printStackTrace();
			request.setAttribute("errorTitle", "システムエラー");
			request.setAttribute("errorMessage", "管理者用商品データの取得中に問題が発生しました。データベースの状態を確認してください。");
			request.setAttribute("errorBackUrl", "AdminMenu.action");
			request.setAttribute("errorBtnText", "管理者メニューへ戻る");

			return "login-error.jsp";
		}
	}
}