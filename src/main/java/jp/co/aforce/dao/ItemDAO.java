package jp.co.aforce.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import jp.co.aforce.beans.Item;

public class ItemDAO extends DAO {

	// 商品の一覧・検索・絞り込み・並び替えを網羅する検索メソッド
	public List<Item> search(String keyword, String meatType, String category, String sortType) throws Exception {
		List<Item> list = new ArrayList<>();

		StringBuilder sql = new StringBuilder("select * from items where 1=1");
		List<Object> params = new ArrayList<>();

		//2.  キーワード検索
		if (keyword != null && !keyword.trim().isEmpty()) {
			sql.append(" and item_name like ?");
			params.add("%" + keyword.trim() + "%");
		}

		//3. 肉の種類の絞り込み
		if (meatType != null && !meatType.trim().isEmpty()) {
			sql.append(" and meat_type = ?");
			params.add(meatType.trim());
		}

		//4. 部位カテゴリ絞り込み
		if (category != null && !category.trim().isEmpty()) {
			sql.append(" and category = ?");
			params.add(category.trim());
		}

		//5. 並び替えの切り替え
		if (sortType != null) {
			switch (sortType) {
			case "price_asc":
				sql.append(" order by price asc"); //価格の安い順
				break;
			case "price_desc":
				sql.append(" order by price desc"); //価格の高い順
				break;
			case "name_asc":
				sql.append(" order by item_name asc"); //商品名順
				break;
			case "new_arrival":
			default:
				sql.append(" order by item_id desc"); //新着順
				break;
			}
		} else {
			sql.append(" order by item_id asc"); //初期表示: 商品IDの昇順
		}

		// 安全な自動クローズ
		try (Connection con = this.getConnection();
				PreparedStatement st = con.prepareStatement(sql.toString())) {

			// プレースホルダー(?)に値をセット
			for (int i = 0; i < params.size(); i++) {
				st.setObject(i + 1, params.get(i));
			}

			try (ResultSet rs = st.executeQuery()) {
				while (rs.next()) {
					Item item = new Item();
					item.setItemId(rs.getInt("item_id"));
					item.setItemName(rs.getString("item_name"));
					item.setMeatStatus(rs.getString("meat_status"));
					item.setPrice(rs.getInt("price"));
					item.setMeatType(rs.getString("meat_type"));
					item.setCategory(rs.getString("category"));
					item.setDescription(rs.getString("description"));
					item.setImagePath(rs.getString("image_path"));
					item.setStock(rs.getInt("stock"));
					list.add(item);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}

		return list;
	}

	//商品IDを指定して、該当するお肉のデータを1件だけ取得するメソッド
	public Item findById(int itemId) throws Exception {
		Item item = null;

		String sql = "select item_id, item_name, price, stock, meat_status, meat_type, category, description, image_path from items where item_id = ?";

		try (Connection con = this.getConnection();
				PreparedStatement st = con.prepareStatement(sql)) {

			st.setInt(1, itemId);

			try (ResultSet rs = st.executeQuery()) {
				if (rs.next()) {
					item = new Item();
					item.setItemId(rs.getInt("item_id"));
					item.setItemName(rs.getString("item_name"));
					item.setPrice(rs.getInt("price"));
					item.setStock(rs.getInt("stock"));
					item.setMeatStatus(rs.getString("meat_status"));
					item.setMeatType(rs.getString("meat_type"));
					item.setCategory(rs.getString("category"));
					item.setDescription(rs.getString("description"));
					item.setImagePath(rs.getString("image_path"));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}

		return item; // 見つからなかった場合は安全に null を返す
	}

	//あたらしい商品をitemsテーブルに登録するメソッド

	public boolean insertItem(String itemName, int price, int stock, String meatStatus, String meatType,
			String category, String description, String imagePath) throws Exception {
		String sql = "insert into items (item_name, price, stock, meat_status, meat_type, category, description, image_path) values (?, ?, ?, ?, ?, ?, ?, ?)";
		int rows = 0;

		try (Connection con = this.getConnection();
				PreparedStatement st = con.prepareStatement(sql)) {

			st.setString(1, itemName);
			st.setInt(2, price);
			st.setInt(3, stock);
			st.setString(4, meatStatus);
			st.setString(5, meatType);
			st.setString(6, category);
			st.setString(7, description);
			st.setString(8, imagePath);

			rows = st.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}

		return rows > 0;
	}

	// 既存の商品情報を更新（UPDATE）するメソッド
	public int updateItem(int itemId, String itemName, int price, int stock, String meatStatus, String meatType,
			String category, String description, String imagePath) throws Exception {

		String sql = "update items set item_name = ?, price = ?, stock = ?, meat_status = ?, meat_type = ?, category = ?, description = ?, image_path = ? where item_id = ?";
		int count = 0;

		try (Connection con = this.getConnection();
				PreparedStatement st = con.prepareStatement(sql)) {

			st.setString(1, itemName);
			st.setInt(2, price);
			st.setInt(3, stock);
			st.setString(4, meatStatus);
			st.setString(5, meatType);
			st.setString(6, category);
			st.setString(7, description);
			st.setString(8, imagePath);
			st.setInt(9, itemId); // WHERE句用の商品ID

			count = st.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}

		return count;
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
}
