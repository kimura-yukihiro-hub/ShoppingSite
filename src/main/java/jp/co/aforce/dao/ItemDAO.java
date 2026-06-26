package jp.co.aforce.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import jp.co.aforce.beans.Item;

public class ItemDAO extends DAO {

	// 共通のマッピング処理
	private void mapResultSetToItem(ResultSet rs, Item item) throws Exception {
		item.setItemId(rs.getInt("item_id"));
		item.setItemName(rs.getString("item_name"));
		item.setKanaName(rs.getString("kana_name"));
		item.setPrice(rs.getInt("price"));
		item.setMeatType(rs.getString("meat_type"));
		item.setCategory(rs.getString("category"));
		item.setDescription(rs.getString("description"));
		item.setImagePath(rs.getString("image_path"));
	}

	// 商品の一覧・検索・絞り込み・並び替えを網羅する検索メソッド
	public List<Item> search(String keyword, String meatType, String category, String sortType, int meatRank)
			throws Exception {
		List<Item> list = new ArrayList<>();
		StringBuilder sql = new StringBuilder("SELECT * FROM items WHERE 1=1");
		List<Object> params = new ArrayList<>();

		if (keyword != null && !keyword.trim().isEmpty()) {
			sql.append(" AND item_name LIKE ?");
			params.add("%" + keyword.trim() + "%");
		}
		if (meatType != null && !meatType.trim().isEmpty()) {
			sql.append(" AND meat_type = ?");
			params.add(meatType.trim());
		}
		if (category != null && !category.trim().isEmpty()) {
			sql.append(" AND category = ?");
			params.add(category.trim());
		}

		// 並び替えロジック
		sortType = (sortType == null) ? "default" : sortType;
		if ("default".equals(sortType)) {
			if (meatRank >= 4)
				sql.append(" ORDER BY price DESC");
			else if (meatRank >= 3)
				sql.append(" ORDER BY item_id DESC");
			else
				sql.append(" ORDER BY price ASC");
		} else {
			switch (sortType) {
			case "price_asc":
				sql.append(" ORDER BY price ASC");
				break;
			case "price_desc":
				sql.append(" ORDER BY price DESC");
				break;
			case "name_asc":
				sql.append(" ORDER BY kana_name ASC");
				break;
			case "name_desc":
				sql.append(" ORDER BY kana_name DESC");
				break;
			case "new_arrival":
			default:
				sql.append(" ORDER BY item_id DESC");
				break;
			}
		}

		try (Connection con = this.getConnection(); PreparedStatement st = con.prepareStatement(sql.toString())) {
			for (int i = 0; i < params.size(); i++)
				st.setObject(i + 1, params.get(i));
			try (ResultSet rs = st.executeQuery()) {
				while (rs.next()) {
					Item item = new Item();
					mapResultSetToItem(rs, item);
					item.setMeatStatus(rs.getInt("meat_status"));
					item.setStock(rs.getInt("stock"));
					list.add(item);
				}
			}
		}
		return list;
	}

	//商品IDを指定して、該当するお肉のデータを1件だけ取得するメソッド
	public Item findById(int itemId) throws Exception {
		String sql = "SELECT * FROM items WHERE item_id = ?";
		try (Connection con = this.getConnection(); PreparedStatement st = con.prepareStatement(sql)) {
			st.setInt(1, itemId);
			try (ResultSet rs = st.executeQuery()) {
				if (rs.next()) {
					Item item = new Item();
					mapResultSetToItem(rs, item);
					item.setStock(rs.getInt("stock"));
					item.setMeatStatus(rs.getInt("meat_status"));
					return item;
				}
			}
		}
		return null;
	}

	//あたらしい商品をitemsテーブルに登録するメソッド

	public boolean insertItem(String itemName, String kanaName, int price, int stock, int meatStatus, String meatType,
			String category, String description, String imagePath) throws Exception {
		String sql = "INSERT INTO items (item_name, kana_name, price, stock, meat_status, meat_type, category, description, image_path) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
		try (Connection con = this.getConnection(); PreparedStatement st = con.prepareStatement(sql)) {
			st.setString(1, itemName);
			st.setString(2, kanaName);
			st.setInt(3, price);
			st.setInt(4, stock);
			st.setInt(5, meatStatus);
			st.setString(6, meatType);
			st.setString(7, category);
			st.setString(8, description);
			st.setString(9, imagePath);
			return st.executeUpdate() > 0;
		}
	}

	// 既存の商品情報を更新（UPDATE）するメソッド
	public int updateItem(int itemId, String itemName, String kanaName, int price, int stock, int meatStatus,
			String meatType, String category, String description, String imagePath) throws Exception {
		String sql = "UPDATE items SET item_name = ?, kana_name = ?, price = ?, stock = ?, meat_status = ?, meat_type = ?, category = ?, description = ?, image_path = ? WHERE item_id = ?";
		try (Connection con = this.getConnection(); PreparedStatement st = con.prepareStatement(sql)) {
			st.setString(1, itemName);
			st.setString(2, kanaName);
			st.setInt(3, price);
			st.setInt(4, stock);
			st.setInt(5, meatStatus);
			st.setString(6, meatType);
			st.setString(7, category);
			st.setString(8, description);
			st.setString(9, imagePath);
			st.setInt(10, itemId);
			return st.executeUpdate();
		}
	}

	// 商品情報を削除（DELETE）するメソッド
	public int deleteItem(int itemId) throws Exception {
		String sql = "delete from items where item_id = ?";
		int count = 0;

		try (Connection con = this.getConnection();
				PreparedStatement st = con.prepareStatement(sql)) {

			st.setInt(1, itemId);
			count = st.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}

		return count;
	}

	// 登録された最新の商品IDを取得するメソッド
	public int getLastInsertedId() throws Exception {
		String sql = "select last_insert_id()";
		try (Connection con = getConnection();
				PreparedStatement st = con.prepareStatement(sql);
				ResultSet rs = st.executeQuery()) {
			if (rs.next()) {
				return rs.getInt(1);
			}
		}
		return -1;
	}

	public List<Item> searchWithStatus(String keyword, String meatType, String category, String sortType)
			throws Exception {
		List<Item> list = new ArrayList<>();
		List<Object> params = new ArrayList<>();
		StringBuilder sql = new StringBuilder(
				"SELECT i.*, l.meat_status, COUNT(l.lot_id) as current_stock " +
						"FROM items i JOIN lots l ON i.item_id = l.item_id WHERE l.meat_status < 3");

		if (keyword != null && !keyword.trim().isEmpty()) {
			sql.append(" AND i.item_name LIKE ?");
			params.add("%" + keyword.trim() + "%");
		}
		if (meatType != null && !meatType.trim().isEmpty()) {
			sql.append(" AND i.meat_type = ?");
			params.add(meatType.trim());
		}
		if (category != null && !category.trim().isEmpty()) {
			sql.append(" AND i.category = ?");
			params.add(category.trim());
		}

		sql.append(" GROUP BY i.item_id, l.meat_status ORDER BY ");
		sortType = (sortType == null) ? "default" : sortType;

		switch (sortType) {
		case "price_asc":
			sql.append("i.price ASC");
			break;
		case "price_desc":
			sql.append("i.price DESC");
			break;
		case "name_asc":
			sql.append("i.kana_name ASC");
			break; 
		case "name_desc":
			sql.append("i.kana_name DESC");
			break; 
		case "new_arrival":
			sql.append("i.item_id DESC");
			break;
		default:
			sql.append("i.item_id ASC");
			break;
		}

		try (Connection con = this.getConnection(); PreparedStatement st = con.prepareStatement(sql.toString())) {
			for (int i = 0; i < params.size(); i++)
				st.setObject(i + 1, params.get(i));
			try (ResultSet rs = st.executeQuery()) {
				while (rs.next()) {
					Item item = new Item();
					mapResultSetToItem(rs, item);
					item.setMeatStatus(rs.getInt("meat_status"));
					item.setStock(rs.getInt("current_stock"));
					list.add(item);
				}
			}
		}
		return list;
	}

}
