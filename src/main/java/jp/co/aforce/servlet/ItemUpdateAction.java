package jp.co.aforce.servlet;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.nio.file.Paths;
import java.util.stream.Collectors;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

import jp.co.aforce.beans.Item;
import jp.co.aforce.dao.ItemDAO;
import jp.co.aforce.tool.Action;

public class ItemUpdateAction extends Action {

	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {

		try {

			// 1. JSPの編集フォーム（multipart/form-data）から各入力をPart経由で安全に取得
			String itemIdStr = getPartString(request, "itemId");
			String itemName = getPartString(request, "itemName");
			String priceStr = getPartString(request, "price");
			String stockStr = getPartString(request, "stock");
			String meatStatus = getPartString(request, "meatStatus");
			String meatType = getPartString(request, "meatType");
			String category = getPartString(request, "category");
			String description = getPartString(request, "description");
			String currentImagePath = getPartString(request, "currentImagePath");

			// バリデーションエラーで画面に戻された際、入力内容をキープするための保存処理
			request.setAttribute("keep_itemName", itemName);
			request.setAttribute("keep_price", priceStr);
			request.setAttribute("keep_stock", stockStr);
			request.setAttribute("keep_meatStatus", meatStatus);
			request.setAttribute("keep_meatType", meatType);
			request.setAttribute("keep_category", category);
			request.setAttribute("keep_description", description);

			// エラー時にitem-edit.jspの「現在の画像」表示や、隠しパラメータのitemIdが消失するのを防ぐための補填
			Item fallbackItem = new Item();
			if (itemIdStr != null && !itemIdStr.trim().isEmpty()) {

				try {
					fallbackItem.setItemId(Integer.parseInt(itemIdStr.trim()));
				} catch (NumberFormatException e) {
					// 万が一変な値が来たら0などでガード
					fallbackItem.setItemId(0);
				}
			}
			fallbackItem.setImagePath(currentImagePath);
			request.setAttribute("item", fallbackItem);

			// 2. 画像の更新判定ロジック（要件：imgフォルダへの保存）
			// 新しい画像がドロップされなかった場合は、隠しパラメータから渡された「現在の画像名」を使用する
			String imagePath = (currentImagePath != null) ? currentImagePath : "no-image.jpg";

			try {
				Part filePart = request.getPart("imageFile");

				if (filePart != null && filePart.getSize() > 0) {
					String submittedFileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
					if (submittedFileName != null && !submittedFileName.trim().isEmpty()) {
						// 指定フォルダ 「/img」 の物理パスを取得
						String uploadPath = request.getServletContext().getRealPath("/img");
						File uploadDir = new File(uploadPath);
						if (!uploadDir.exists()) {
							uploadDir.mkdirs();
						}
						// 指定フォルダへ画像を物理書き込み
						filePart.write(uploadPath + File.separator + submittedFileName);
						imagePath = submittedFileName; // 新しいファイル名で上書き
					}
				}
			} catch (Exception imgException) {
				// 画像の物理保存で何かしらコケても、全体の処理は止めず既存画像のままで進めるセーフティガード
				System.out.println("【警告】画像の更新保存に失敗したため、既存の画像名で登録を続行します。");
				imgException.printStackTrace();
			}

			if (description == null) {
				description = "";
			}

			// 3. 必須項目のバリデーションチェック（空チェック）
			if (itemIdStr == null || itemName == null || priceStr == null || stockStr == null || category == null ||
					itemIdStr.trim().isEmpty() || itemName.trim().isEmpty() || priceStr.trim().isEmpty()
					|| stockStr.trim().isEmpty() || category.trim().isEmpty()) {

				request.setAttribute("errorMessage", "必須項目が入力されていません。");
				return "item-edit.jsp"; // 入力値を保持して編集画面に差し戻す
			}

			int itemId = Integer.parseInt(itemIdStr.trim());
			int price;
			int stock;

			// 数値項目の型変換チェック
			try {
				price = Integer.parseInt(priceStr.trim());
				stock = Integer.parseInt(stockStr.trim());
			} catch (NumberFormatException e) {
				request.setAttribute("errorMessage", "価格、在庫数には半角数値を入力してください。");
				return "item-edit.jsp";
			}

			// 4. ItemDAOを呼び出してデータベースへ上書き保存(UPDATE実行)
			ItemDAO itemDao = new ItemDAO();
			// 成功時(更新行数>=1)を true として判定するように変更
			boolean isSuccess = itemDao.updateItem(itemId, itemName, price, stock, meatStatus, meatType, category,
					description, imagePath) > 0;

			if (!isSuccess) {
				request.setAttribute("errorMessage", "データベースへの更新処理に失敗しました。時間をおいて再度お試しください。");
				return "item-edit.jsp";
			}

			// 5. 修正した結果がすぐに確認できる「管理一覧画面」リダイレクト
			return "redirect:AdminItemList.action";
		} catch (Exception e) {
			e.printStackTrace();
			request.setAttribute("errorTitle", "商品更新システムエラー");
			request.setAttribute("errorMessage", "商品のデータ更新処理中に、サーバーまたはデータベースで予期せぬ重大なエラーが発生しました。");
			request.setAttribute("errorBackUrl", "AdminItemList.action");
			request.setAttribute("errorBtnText", "管理一覧へ戻る");
			return "login-error.jsp";
		}
	}

	// multipart/form-data形式のリクエストから各Partのテキストデータを抽出する共通メソッド
	private String getPartString(HttpServletRequest request, String name) throws Exception {
		Part part = request.getPart(name);
		if (part == null) {
			return null;
		}
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(part.getInputStream(), "UTF-8"))) {
			return reader.lines().collect(Collectors.joining("\n"));
		}
	}

}
