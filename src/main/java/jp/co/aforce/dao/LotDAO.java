package jp.co.aforce.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jp.co.aforce.beans.Lot;

public class LotDAO extends DAO {
	// 1. 新規ロットの登録(互換性のため残す)
	public void insertLot(int itemId, String serialNumber, LocalDateTime arrivalDate, int meatStatus, String batchId,
			LocalDateTime agingStartDate) throws Exception {
		String sql = "insert into lots (item_id, serial_number, arrival_date, meat_status, batch_id, aging_start_date) "
				+
				"values (?, ?, ?, ?, ?, ?)";
		try (Connection con = getConnection();
				PreparedStatement st = con.prepareStatement(sql)) {
			st.setInt(1, itemId);
			st.setString(2, serialNumber);
			st.setTimestamp(3, Timestamp.valueOf(arrivalDate));
			st.setInt(4, meatStatus);
			st.setString(5, batchId);
			if (agingStartDate != null) {
				st.setTimestamp(6, Timestamp.valueOf(agingStartDate));
			} else {
				st.setNull(6, java.sql.Types.TIMESTAMP);
			}
			st.executeUpdate();
		}
	}

	// // 1. 新規ロットの登録
	public void insertLot(int itemId, String serialNumber, LocalDateTime arrivalDate, int meatStatus,
			LocalDateTime agingStartDate) throws Exception {
		// SQLに aging_start_date を追加
		String sql = "INSERT INTO lots (item_id, serial_number, arrival_date, meat_status, aging_start_date) VALUES (?, ?, ?, ?, ?)";

		try (Connection con = getConnection();
				PreparedStatement st = con.prepareStatement(sql)) {

			st.setInt(1, itemId);
			st.setString(2, serialNumber);
			st.setTimestamp(3, Timestamp.valueOf(arrivalDate));
			st.setInt(4, meatStatus);

			// 熟成開始日が null の場合は SQL にも NULL をセットする
			if (agingStartDate != null) {
				st.setTimestamp(5, Timestamp.valueOf(agingStartDate));
			} else {
				st.setNull(5, java.sql.Types.TIMESTAMP);
			}

			st.executeUpdate();
		}
	}

	// 2. 販売可能な状態（生:0 または 熟成:1）を明示的に指定する
	public int getAvailableStock(int itemId) throws Exception {
		String sql = "SELECT COUNT(*) FROM lots WHERE item_id = ? AND meat_status IN (0, 1)";
		try (Connection con = getConnection(); PreparedStatement st = con.prepareStatement(sql)) {
			st.setInt(1, itemId);
			try (ResultSet rs = st.executeQuery()) {
				if (rs.next())
					return rs.getInt(1);
			}
		}
		return 0;
	}

	// 3. すべての有効なロットを取得するメソッド
	public List<Lot> getAllActiveLots() throws Exception {
		List<Lot> list = new ArrayList<>();
		String sql = "SELECT * FROM lots WHERE meat_status < 2";

		try (Connection con = getConnection();
				PreparedStatement st = con.prepareStatement(sql);
				ResultSet rs = st.executeQuery()) {

			while (rs.next()) {
				Lot lot = new Lot();
				lot.setLotId(rs.getInt("lot_id"));
				lot.setItemId(rs.getInt("item_id"));
				lot.setSerialNumber(rs.getString("serial_number"));
				lot.setArrivalDate(rs.getTimestamp("arrival_date").toLocalDateTime());
				lot.setMeatStatus(rs.getInt("meat_status"));
				list.add(lot);
			}
		}
		return list;
	}

	// 4. ロット個別のステータスを更新するメソッド
	// LotDAO.java の updateSpecificLotStatus メソッド
	public void updateSpecificLotStatus(int lotId, int status) throws Exception {
		String sql = "UPDATE lots SET meat_status = ?, aging_start_date = ? WHERE lot_id = ?";
		try (Connection con = getConnection();
				PreparedStatement st = con.prepareStatement(sql)) {
			st.setInt(1, status);
			if (status == 1) {
				st.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
			} else {
				st.setNull(2, java.sql.Types.TIMESTAMP);
			}
			st.setInt(3, lotId);

			int count = st.executeUpdate();

			if (count == 0) {
			}
		}
	}

	// 5. 指定した数量分のロットを「販売済み（3）」に更新するメソッド
	public void consumeLots(int itemId, int quantity) throws Exception {
		// 古いロットから優先的に消費する（ORDER BY arrival_date ASC）
		String sql = "UPDATE lots SET meat_status = 3 WHERE lot_id IN (" +
				"SELECT lot_id FROM (SELECT lot_id FROM lots WHERE item_id = ? AND meat_status = 0 " +
				"ORDER BY arrival_date ASC LIMIT ?) AS temp)";

		try (Connection con = getConnection();
				PreparedStatement st = con.prepareStatement(sql)) {
			st.setInt(1, itemId);
			st.setInt(2, quantity);
			st.executeUpdate();
		}
	}

	// 6. 特定商品IDのロットを全削除するメソッド
	public void deleteLotsByItemId(int itemId) throws Exception {
		String sql = "delete from lots where item_id = ?";
		try (Connection con = getConnection();
				PreparedStatement st = con.prepareStatement(sql)) {
			st.setInt(1, itemId);
			st.executeUpdate();
		}
	}

	// 7. 指定された数だけ、古いロットから順に削除するメソッド
	public void reduceLots(int itemId, int count) throws Exception {
		// 古いロットから削除するためのSQL
		String sql = "DELETE FROM lots WHERE lot_id IN (" +
				"SELECT lot_id FROM (SELECT lot_id FROM lots WHERE item_id = ? ORDER BY arrival_date ASC LIMIT ?) AS temp)";
		try (Connection con = this.getConnection();
				PreparedStatement st = con.prepareStatement(sql)) {
			st.setInt(1, itemId);
			st.setInt(2, count);
			st.executeUpdate();
		}
	}

	// 7. 購入IDに紐づくシリアル番号を取得するメソッド
	public String getSerialByPurchaseId(int purchaseId) throws Exception {
		// 購入IDに紐づくシリアル番号を取得するロジック
		String sql = "SELECT serial_number FROM lots WHERE purchase_id = ?";
		try (Connection con = getConnection();
				PreparedStatement st = con.prepareStatement(sql)) {
			st.setInt(1, purchaseId);
			try (ResultSet rs = st.executeQuery()) {
				if (rs.next())
					return rs.getString(1);
			}
		}
		return "未発行";
	}

	// 8. 販売済みにする際に購入IDを記録するメソッド
	public String bindPurchaseId(int itemId, int quantity, int purchaseId) throws Exception {
		// 1. 更新処理
		String updateSql = "UPDATE lots SET purchase_id = ?, meat_status = 3 " +
				"WHERE lot_id IN (" +
				"SELECT lot_id FROM (SELECT lot_id FROM lots WHERE item_id = ? AND meat_status = 0 " +
				"ORDER BY arrival_date ASC LIMIT ?) AS temp)";

		// 2. 更新対象のシリアル番号を取得するSQL
		String selectSql = "SELECT serial_number FROM lots WHERE purchase_id = ? AND item_id = ?";

		try (Connection con = getConnection()) {
			// 更新実行
			try (PreparedStatement st = con.prepareStatement(updateSql)) {
				st.setInt(1, purchaseId);
				st.setInt(2, itemId);
				st.setInt(3, quantity);
				st.executeUpdate();
			}

			// 取得実行
			try (PreparedStatement st = con.prepareStatement(selectSql)) {
				st.setInt(1, purchaseId);
				st.setInt(2, itemId);
				try (ResultSet rs = st.executeQuery()) {
					List<String> serials = new ArrayList<>();
					while (rs.next()) {
						serials.add(rs.getString("serial_number"));
					}
					return String.join(", ", serials);
				}
			}
		}
	}

	// 9. 指定した注文IDと商品IDに紐付いたシリアル番号をすべて取得するメソッド
	public String getSerialNumbers(int itemId, int purchaseId) throws Exception {
		String sql = "SELECT serial_number FROM lots WHERE item_id = ? AND purchase_id = ?";
		List<String> serials = new ArrayList<>();
		try (Connection con = getConnection(); PreparedStatement st = con.prepareStatement(sql)) {
			st.setInt(1, itemId);
			st.setInt(2, purchaseId);
			try (ResultSet rs = st.executeQuery()) {
				while (rs.next()) {
					serials.add(rs.getString("serial_number"));
				}
			}
		}
		// 取得した複数のロット番号をカンマ区切りの文字列にする
		return String.join(", ", serials);
	}

	// 10. 特定の batch_id に属する「未販売ロット」の鮮度と熟成開始日を一括更新するメソッド
	public void updateStatusByBatchId(String batchId, int status) throws Exception {
		String sql = "UPDATE lots SET meat_status = ?, aging_start_date = ? WHERE batch_id = ?";
		try (Connection con = getConnection(); PreparedStatement st = con.prepareStatement(sql)) {
			st.setInt(1, status);
			if (status == 1) {
				st.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
			} else {
				st.setNull(2, java.sql.Types.TIMESTAMP);
			}
			st.setString(3, batchId);
			st.executeUpdate();
		}
	}

	// 11. 特定のロットを廃棄(ステータス９)にする
	public void disposeLot(int lotId) throws Exception {
		updateSpecificLotStatus(lotId, 9);
	}

	// 12. 指定したステータスのロット数だけをカウントする（集計用）
	public int getStockByStatus(int itemId, int status) throws Exception {
		String sql = "SELECT COUNT(*) FROM lots WHERE item_id = ? AND meat_status = ?";
		try (Connection con = getConnection(); PreparedStatement st = con.prepareStatement(sql)) {
			st.setInt(1, itemId);
			st.setInt(2, status);
			try (ResultSet rs = st.executeQuery()) {
				if (rs.next())
					return rs.getInt(1);
			}
		}
		return 0;
	}

	// 13. 特定の商品IDに紐づくロットを全件取得するメソッド
	public List<Lot> getLotsByItemId(int itemId) throws Exception {
		List<Lot> list = new ArrayList<>();
		String sql = "SELECT * FROM lots WHERE item_id = ? ORDER BY arrival_date ASC";

		try (Connection con = getConnection();
				PreparedStatement st = con.prepareStatement(sql)) {
			st.setInt(1, itemId);
			try (ResultSet rs = st.executeQuery()) {
				while (rs.next()) {
					Lot lot = new Lot();
					lot.setLotId(rs.getInt("lot_id"));
					lot.setItemId(rs.getInt("item_id"));
					lot.setSerialNumber(rs.getString("serial_number"));

					if (rs.getTimestamp("arrival_date") != null) {
						lot.setArrivalDate(rs.getTimestamp("arrival_date").toLocalDateTime());
					}

					// 修正後の該当箇所
					if (rs.getTimestamp("aging_start_date") != null) {
						lot.setAgingStartDate(rs.getTimestamp("aging_start_date").toLocalDateTime());
					}

					lot.setMeatStatus(rs.getInt("meat_status"));

					lot.setStock(1);

					list.add(lot);
				}
			}
		}
		return list;
	}

	// 14. batch_id ごとに集計したリストを取得するメソッド
	public List<Lot> getLotSummariesByItemId(int itemId) throws Exception {
		List<Lot> list = new ArrayList<>();
		// batch_id でグループ化して集計するSQL
		String sql = "SELECT batch_id, item_id, MIN(meat_status) as meat_status, COUNT(*) as stock, MIN(arrival_date) as arrival_date "
				+
				"FROM lots WHERE item_id = ? AND meat_status < 3 " +
				"GROUP BY batch_id, item_id ORDER BY arrival_date DESC";

		try (Connection con = getConnection();
				PreparedStatement st = con.prepareStatement(sql)) {
			st.setInt(1, itemId);
			try (ResultSet rs = st.executeQuery()) {
				while (rs.next()) {
					Lot lot = new Lot();
					lot.setBatchId(rs.getString("batch_id"));
					lot.setItemId(rs.getInt("item_id"));
					lot.setMeatStatus(rs.getInt("meat_status"));
					lot.setStock(rs.getInt("stock"));
					if (rs.getTimestamp("arrival_date") != null) {
						lot.setArrivalDate(rs.getTimestamp("arrival_date").toLocalDateTime());
					}
					list.add(lot);
				}
			}
		}
		return list;
	}

	// 15. 特定の batch_id に属する詳細ロット一覧を取得
	public List<Lot> getLotDetailsByBatchId(String batchId) throws Exception {
		List<Lot> list = new ArrayList<>();
		String sql = "SELECT * FROM lots WHERE batch_id = ? AND meat_status < 3 ORDER BY arrival_date ASC";

		try (Connection con = getConnection();
				PreparedStatement st = con.prepareStatement(sql)) {
			st.setString(1, batchId);
			try (ResultSet rs = st.executeQuery()) {
				while (rs.next()) {
					Lot lot = new Lot();
					lot.setLotId(rs.getInt("lot_id"));
					lot.setSerialNumber(rs.getString("serial_number"));
					lot.setMeatStatus(rs.getInt("meat_status"));
					if (rs.getTimestamp("aging_start_date") != null) {
						lot.setAgingStartDate(rs.getTimestamp("aging_start_date").toLocalDateTime());
					}
					list.add(lot);
				}
			}
		}
		return list;
	}

	// 16. 混在チェック用のメソッド
	public boolean isMixedStatus(String batchId) throws Exception {
		String sql = "SELECT COUNT(DISTINCT meat_status) FROM lots WHERE batch_id = ? AND meat_status IN (0, 1)";
		try (Connection con = getConnection();
				PreparedStatement st = con.prepareStatement(sql)) {
			st.setString(1, batchId);
			try (ResultSet rs = st.executeQuery()) {
				if (rs.next()) {
					// meat_status が 0 と 1 の両方が存在すれば COUNT は 2 になる
					return rs.getInt(1) > 1;
				}
			}
		}
		return false;
	}
}
