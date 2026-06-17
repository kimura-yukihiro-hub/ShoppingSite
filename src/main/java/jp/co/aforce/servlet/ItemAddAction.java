package jp.co.aforce.servlet;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.nio.file.Paths;
import java.util.stream.Collectors;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

import jp.co.aforce.dao.ItemDAO;
import jp.co.aforce.tool.Action;


// 管理者が新しい商品（お肉）を追加登録するアクションクラス
 
public class ItemAddAction extends Action {

	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {

		try {
			// 1. JSPの登録フォームから送信された入力値を「Part」経由で安全に取得
			String itemName = getPartString(request, "itemName");
			String priceStr = getPartString(request, "price");
			String stockStr = getPartString(request, "stock");
			String meatStatus = getPartString(request, "meatStatus");
			String meatType = getPartString(request, "meatType");
			String category = getPartString(request, "category");
			String description = getPartString(request, "description");

			// バリデーションエラー時に入力内容をキープするための保存処理
			request.setAttribute("keep_itemName", itemName);
			request.setAttribute("keep_price", priceStr);
			request.setAttribute("keep_stock", stockStr);
			request.setAttribute("keep_meatStatus", meatStatus);
			request.setAttribute("keep_meatType", meatType);
			request.setAttribute("keep_category", category);
			request.setAttribute("keep_description", description);

			// 2. サーバーサイドでの必須バリデーション（空チェック）
			if (itemName == null || priceStr == null || stockStr == null || category == null ||
					itemName.trim().isEmpty() || priceStr.trim().isEmpty() || stockStr.trim().isEmpty()
					|| category.trim().isEmpty()) {

				// 入力ミスの場合は、値を保持したまま元の登録画面へ戻してメッセージを表示する
				request.setAttribute("errorMessage", "⚠️ 必須項目（商品名・価格・在庫・部位カテゴリ）が入力されていません。");
				return "item-add.jsp";
			}

			// 受け取った文字データの数値をint型に変換
			int price = 0;
			int stock = 0;
			try {
				price = Integer.parseInt(priceStr.trim());
				stock = Integer.parseInt(stockStr.trim());
			} catch (NumberFormatException e) {
				request.setAttribute("errorMessage", "⚠️ 価格、または在庫数に不正な文字が含まれています。半角数字で入力してください。");
				return "item-add.jsp";
			}

			// 商品説明が空欄だった場合は空文字を設定（データベースのエラー防止）
			if (description == null || description.trim().isEmpty()) {
				description = "";
			}

			// --- 画像ファイルのアップロード処理（imgフォルダへ保存） ---
			String imagePath = "no-image.jpg"; // デフォルトのファイル名

			try {
				Part filePart = request.getPart("imageFile");

				if (filePart != null && filePart.getSize() > 0) {
					String submittedFileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();

					if (submittedFileName != null && !submittedFileName.trim().isEmpty()) {
						String uploadPath = request.getServletContext().getRealPath("/img");

						if (uploadPath == null) {
							uploadPath = request.getServletContext().getRealPath("/") + File.separator + "img";
						}

						File uploadDir = new File(uploadPath);
						if (!uploadDir.exists()) {
							uploadDir.mkdirs();
						}

						String fullPath = uploadPath + File.separator + submittedFileName;
						filePart.write(fullPath);

						imagePath = submittedFileName; // 物理保存成功時にファイル名を上書き
					}
				}
			} catch (Exception imgException) {
				// 画像保存で失敗してもシステムはクラッシュさせず、no-image扱いで登録を続行するセーフティネット
				System.out.println("【警告】画像の物理保存に失敗したため、no-image.jpg で登録を続行します。");
				imgException.printStackTrace();
				imagePath = "no-image.jpg";
			}

			// 3. ItemDAOを呼び出してデータベースへ保存
			ItemDAO itemDao = new ItemDAO();
			boolean isSuccess = itemDao.insertItem(itemName, price, stock, meatStatus, meatType, category, description,
					imagePath);

			if (!isSuccess) {
				// DAO側で偽が返ってきた（登録失敗した）場合の個別エラーメッセージ
				request.setAttribute("errorTitle", "商品登録失敗");
				request.setAttribute("errorMessage", "データベースへの登録処理が拒否されました。重複データがないか確認してください。");
				request.setAttribute("errorBackUrl", "AdminMenu.action");
				request.setAttribute("errorBtnText", "管理者メニューへ戻る");
				return "login-error.jsp";
			}

			// 4. 登録が成功したら二重送信防止のため、管理者メニュー（AdminMenu）へ安全にリダイレクト
			return "redirect:AdminMenu.action";

		} catch (Exception e) {
			// 💡こだわりの「独自エラーメッセージ」をセットしてシステムエラー画面へ
			e.printStackTrace();
			request.setAttribute("errorTitle", "商品登録システムエラー");
			request.setAttribute("errorMessage", "新規商品の追加処理中に、サーバーまたはデータベースで予期せぬ重大なエラーが発生しました。");
			request.setAttribute("errorBackUrl", "AdminMenu.action");
			request.setAttribute("errorBtnText", "管理者メニューへ戻る");
			return "login-error.jsp";
		}
	}

	/**
	 * multipart/form-data形式のリクエストから、指定したパラメータの文字列を安全に抽出するヘルパーメソッド
	 */
	private String getPartString(HttpServletRequest request, String name) throws Exception {
		try {
			Part part = request.getPart(name);
			if (part == null) {
				return "";
			}
			try (BufferedReader reader = new BufferedReader(new InputStreamReader(part.getInputStream(), "UTF-8"))) {
				String value = reader.lines().collect(Collectors.joining("\n"));
				return (value != null) ? value.trim() : "";
			}
		} catch (Exception e) {
			return "";
		}
	}
}