package jp.co.aforce.servlet;

import java.util.ArrayList;
import java.util.List;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import jp.co.aforce.beans.Item;
import jp.co.aforce.beans.User;
import jp.co.aforce.dao.ItemDAO;
import jp.co.aforce.dao.LotDAO;
import jp.co.aforce.tool.Action;

public class ItemListAction extends Action {

	public ItemListAction() {
		this.requireLogin = false;
	}

	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {

		try {
			// リセットフラグの確認（全商品表示用）
			if ("true".equals(request.getParameter("reset"))) {
				request.getSession().removeAttribute("keyword");
				request.getSession().removeAttribute("meatType");
				request.getSession().removeAttribute("category");
				request.getSession().removeAttribute("sortType");
			}

			// 1. パラメータ取得（既存の処理）
			String keyword = request.getParameter("keyword");
			String meatType = request.getParameter("meatType");
			String category = request.getParameter("category");
			String sortType = request.getParameter("sortType");

			// resetがtrueならセッションからは取得しないというロジック
			if (!"true".equals(request.getParameter("reset"))) {

				// セッションから以前の条件を取得
				String sessionKeyword = (String) request.getSession().getAttribute("keyword");
				String sessionMeatType = (String) request.getSession().getAttribute("meatType");
				String sessionCategory = (String) request.getSession().getAttribute("category");
				String sessionSortType = (String) request.getSession().getAttribute("sortType");

				// パラメータが空（null/空文字）なら、セッションから値を復元する
				if (keyword == null || keyword.isEmpty())
					keyword = (sessionKeyword != null) ? sessionKeyword : "";
				if (meatType == null || meatType.isEmpty())
					meatType = (sessionMeatType != null) ? sessionMeatType : "";
				if (category == null || category.isEmpty())
					category = (sessionCategory != null) ? sessionCategory : "";
				if (sortType == null || sortType.isEmpty())
					sortType = (sessionSortType != null) ? sessionSortType : "default";
			} else {
				// リセット時は明示的に初期値を入れる
				if (keyword == null)
					keyword = "";
				if (meatType == null)
					meatType = "";
				if (category == null)
					category = "";
				if (sortType == null)
					sortType = "default";
			}

			if (!"default".equals(sortType) &&
					!"price_asc".equals(sortType) &&
					!"price_desc".equals(sortType) &&
					!"name_asc".equals(sortType) &&
					!"name_desc".equals(sortType) &&
					!"new_arrival".equals(sortType)) {

				sortType = "default";
			}

			// 新しい検索条件（あるいは復元された条件）をセッションに保存し直す
			request.getSession().setAttribute("keyword", keyword);
			request.getSession().setAttribute("meatType", meatType);
			request.getSession().setAttribute("category", category);
			request.getSession().setAttribute("sortType", sortType);

			// 画面表示用にセット
			request.setAttribute("keyword", keyword);
			request.setAttribute("meatType", meatType);
			request.setAttribute("category", category);
			request.setAttribute("sortType", sortType);

			// ログイン中のランクを取得（パーソナライズ用）
			int meatRank = 0;
			User loginUser = (User) request.getSession().getAttribute("loginUser");
			if (loginUser != null) {
				meatRank = loginUser.getMeatRank();
			}

			// 2. 商品リスト取得
			ItemDAO dao = new ItemDAO();
			List<Item> originalItems = dao.search(keyword, meatType, category, sortType, meatRank);
			List<Item> displayList = new ArrayList<>(); // 表示用のリスト

			LotDAO lotDAO = new LotDAO();

			// 3. 商品×状態のペアを作成してリスト化
			for (Item item : originalItems) {
				// 「生(0)」のカードを作成
				int raw = lotDAO.getStockByStatus(item.getItemId(), 0);
				if (raw > 0) {
					Item rawItem = copyItem(item);
					rawItem.setMeatStatus(0);
					rawItem.setStock(raw);
					displayList.add(rawItem);
				}

				// 「熟成(1)」のカードを作成
				int aged = lotDAO.getStockByStatus(item.getItemId(), 1);
				if (aged > 0) {
					Item agedItem = copyItem(item);
					agedItem.setMeatStatus(1);
					agedItem.setStock(aged);
					displayList.add(agedItem);
				}
			}

			request.setAttribute("itemList", displayList);
			return "item-list.jsp";

		} catch (Exception e) {
			e.printStackTrace();
			request.setAttribute("errorTitle", "商品一覧表示エラー");
			request.setAttribute("errorMessage", "商品データの取得中に問題が発生しました。");
			request.setAttribute("errorBackUrl", "ItemList.action?reset=true");
			request.setAttribute("errorBtnText", "一覧へ戻る");
			return "login-error.jsp";
		}
	}

	// Itemの内容をコピーするヘルパーメソッド
	private Item copyItem(Item item) {
		Item newItem = new Item();
		newItem.setItemId(item.getItemId());
		newItem.setItemName(item.getItemName());
		newItem.setPrice(item.getPrice());
		newItem.setMeatType(item.getMeatType());
		newItem.setCategory(item.getCategory());
		newItem.setDescription(item.getDescription());
		newItem.setImagePath(item.getImagePath());
		return newItem;
	}
}