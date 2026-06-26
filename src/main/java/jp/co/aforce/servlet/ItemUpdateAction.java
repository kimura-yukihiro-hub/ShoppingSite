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
import jp.co.aforce.dao.LotDAO;
import jp.co.aforce.tool.Action;

public class ItemUpdateAction extends Action {

	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		try {
			// 1. 各パラメータをPart経由で安全に取得
			String itemIdStr = getPartString(request, "itemId");
			String itemName = getPartString(request, "itemName");
			String kanaName = getPartString(request, "kanaName");
			String priceStr = getPartString(request, "price");
			String stockStr = getPartString(request, "stock");
			String meatStatus = getPartString(request, "meatStatus");
			String meatType = getPartString(request, "meatType");
			String category = getPartString(request, "category");
			String description = getPartString(request, "description");
			String currentImagePath = getPartString(request, "currentImagePath");

			// 2. 画像の更新処理
			String imagePath = (currentImagePath != null && !currentImagePath.isEmpty()) ? currentImagePath
					: "no-image.jpg";
			Part filePart = request.getPart("imageFile");
			try {
				if (filePart != null && filePart.getSize() > 0) {
					if (filePart.getSize() > 2 * 1024 * 1024) {
						request.setAttribute("errorTitle", "画像アップロードエラー");
						request.setAttribute("errorMessage", "ファイルサイズは2MB以内でアップロードしてください。");
						request.setAttribute("errorBackUrl", "AdminItemList.action");
						request.setAttribute("errorBtnText", "管理一覧へ戻る");
						return "login-error.jsp";
					}
					String submittedFileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
					if (submittedFileName != null && !submittedFileName.isEmpty()) {
						String uploadPath = request.getServletContext().getRealPath("/img");
						File uploadDir = new File(uploadPath);
						if (!uploadDir.exists())
							uploadDir.mkdirs();
						filePart.write(uploadPath + File.separator + submittedFileName);
						imagePath = submittedFileName;
					}
				}
			} catch (Exception imgException) {
				System.out.println("【警告】画像の保存に失敗しましたが処理を継続します。");
			}

			// 3. 必須項目のバリデーション
			if (isInvalid(itemName, "noSpace") ||
					isInvalid(kanaName, "katakana") ||
					isInvalid(priceStr, "numeric") ||
					isInvalid(stockStr, "numeric") ||
					isInvalid(category, "noSpace")) {
				request.setAttribute("errorTitle", "入力エラー");
				request.setAttribute("errorMessage", "必須項目が正しく入力されていません。");
				request.setAttribute("errorBackUrl", "AdminItemList.action");
				request.setAttribute("errorBtnText", "管理一覧へ戻る");
				return "login-error.jsp";
			}

			// 4. 型変換とチェック
			int itemId, price, stock, meatStatusInt;
			try {
				itemId = Integer.parseInt(itemIdStr.trim());
				price = Integer.parseInt(priceStr.trim());
				stock = Integer.parseInt(stockStr.trim());
				meatStatusInt = Integer.parseInt(meatStatus.trim());
			} catch (NumberFormatException e) {
				request.setAttribute("errorTitle", "入力値エラー");
				request.setAttribute("errorMessage", "価格や在庫数には正しい数値を入力してください。");
				request.setAttribute("errorBackUrl", "AdminItemList.action");
				request.setAttribute("errorBtnText", "管理一覧へ戻る");
				return "login-error.jsp";
			}

			// 5. DAOによる更新処理
			ItemDAO itemDao = new ItemDAO();
			LotDAO lotDAO = new LotDAO();

			boolean isSuccess = itemDao.updateItem(itemId, itemName, kanaName, price, stock, meatStatusInt, meatType,
					category, description, imagePath) > 0;

			if (isSuccess) {
				int currentStock = lotDAO.getAvailableStock(itemId);
				String batchId = "B-" + System.currentTimeMillis();

				if (stock > currentStock) {
					for (int i = 0; i < (stock - currentStock); i++) {
						lotDAO.insertLot(itemId, itemId + "-" + System.nanoTime() + "-" + i,
								java.time.LocalDateTime.now(), meatStatusInt, batchId,
								(meatStatusInt == 1 ? java.time.LocalDateTime.now() : null));
					}
				} else if (stock < currentStock) {
					lotDAO.reduceLots(itemId, (currentStock - stock));
				}
				return "redirect:AdminItemList.action";
			} else {
				request.setAttribute("errorTitle", "更新エラー");
				request.setAttribute("errorMessage", "データベースへの更新処理に失敗しました。");
				request.setAttribute("errorBackUrl", "AdminItemList.action");
				request.setAttribute("errorBtnText", "管理一覧へ戻る");
				return "login-error.jsp";
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "login-error.jsp";
		}
	}

	// パラメータ取得用ヘルパー
	private String getPartString(HttpServletRequest request, String name) throws Exception {
		Part part = request.getPart(name);
		if (part == null)
			return "";
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(part.getInputStream(), "UTF-8"))) {
			return reader.lines().collect(Collectors.joining("\n")).trim();
		}
	}

	// バリデーション判定
	private boolean isInvalid(String str, String type) {
		if (str == null)
			return true;
		String cleanStr = str.replaceAll("[\\r\\n\\s\\u3000]", "");
		if (cleanStr.isEmpty())
			return true;

		switch (type) {
		case "noSpace":
			return str.matches(".*[\\s\\u3000].*");
		case "katakana":
			return !cleanStr.matches("^[\\u30A1-\\u30F6ー]+$");
		case "numeric":
			return !cleanStr.matches("^[0-9]+$");
		default:
			return false;
		}
	}
}