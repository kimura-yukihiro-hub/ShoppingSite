package jp.co.aforce.servlet;

import java.util.List;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import jp.co.aforce.beans.Item;
import jp.co.aforce.dao.ItemDAO;
import jp.co.aforce.tool.Action;

public class ItemListAction extends Action {

	public ItemListAction() {
		this.requireLogin = false;
	}

	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {

		try {

			//1. 値を取得し、初回起動時（null）の場合は一律で空文字""に安全に変換する
			String keyword = request.getParameter("keyword");
			if (keyword == null)
				keyword = "";

			String meatType = request.getParameter("meatType");
			if (meatType == null)
				meatType = "";

			String category = request.getParameter("category");
			if (category == null)
				category = "";

			String sortType = request.getParameter("sortType");
			if (sortType == null)
				sortType = "default"; // 初回はおすすめ順に固定

			//2. 検索条件を画面(JSP)側へ安全な値にして送り返す（これでJSPが誤作動しなくなります）
			request.setAttribute("keyword", keyword);
			request.setAttribute("meatType", meatType);
			request.setAttribute("category", category);
			request.setAttribute("sortType", sortType);

			//3. ItemDAO を呼び出してお肉のデータを検索・取得
			ItemDAO dao = new ItemDAO();
			List<Item> itemList = dao.search(keyword, meatType, category, sortType);

			//4. 取得したお肉のリストをJSPへ引き渡すために格納
			request.setAttribute("itemList", itemList);

			//5. 肉が並ぶ商品一覧画面のJSPファイル名を返す
			return "item-list.jsp";
		} catch (Exception e) {
			e.printStackTrace();
			request.setAttribute("errorTitle", "商品一覧読込エラー");
			request.setAttribute("errorMessage",
					"お肉の一覧データを取得する際に、データベースで予期せぬエラーが発生しました。条件を変更して再度お試しいただくか、時間をおいてアクセスしてください。");
			request.setAttribute("errorBackUrl", "item-list.jsp");
			request.setAttribute("errorBtnText", "トップページへ戻る");
			return "login-error.jsp";
		}
	}
}
