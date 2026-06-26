package jp.co.aforce.tool;

import java.util.List;

import jp.co.aforce.beans.Item;
import jp.co.aforce.dao.ItemDAO;
import jp.co.aforce.dao.LotDAO;

public class DataMigrationTask {
	public void migrateItemsToLots() {
		ItemDAO itemDAO = new ItemDAO();
		LotDAO lotDAO = new LotDAO();

		try {
			// 1. 全商品を取得
			List<Item> items = itemDAO.search(null, null, null, null, 0);

			for (Item item : items) {
				int stock = item.getStock();
				System.out.println("移行開始: " + item.getItemName() + " (在庫数: " + stock + ")");

				// ★移行用にバッチIDを生成（商品単位でまとめる例）
				String batchId = "MIG-" + item.getItemId() + "-" + System.currentTimeMillis();

				// 2. 在庫数分だけロットを生成
				for (int i = 0; i < stock; i++) {
					String serialNumber = String.format("SER-%03d-%d-%03d",
							item.getItemId(), System.currentTimeMillis() % 100000, i);

					// 3. LOTテーブルへINSERT（引数を6つに合わせる）
					// 最後の null は aging_start_date です
					lotDAO.insertLot(item.getItemId(), serialNumber, java.time.LocalDateTime.now(), 0, batchId, null);
				}
			}
			System.out.println("すべての移行処理が完了しました。");

		} catch (Exception e) {
			System.err.println("移行処理中にエラーが発生しました。");
			e.printStackTrace();
		}
	}
}