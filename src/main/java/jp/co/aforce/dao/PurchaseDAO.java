package jp.co.aforce.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import jp.co.aforce.beans.CartItem;
import jp.co.aforce.beans.Purchase;
import jp.co.aforce.beans.User;

public class PurchaseDAO extends DAO {

	//商品の最新の在庫数をデータベースから直接取得する

	public int getCurrentStock(int itemId) throws Exception {
		int stock = 0;
		// 商品テーブルから現在の在庫数を取得するSQL
		String sql = "select stock from items where item_id = ?";

		try (Connection con = this.getConnection();
				PreparedStatement st = con.prepareStatement(sql)) {

			st.setInt(1, itemId);

			try (ResultSet rs = st.executeQuery()) {
				if (rs.next()) {
					stock = rs.getInt("stock");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}

		return stock;
	}
	// 購入履歴の保存と、商品の在庫減算（マイナス）を一括で処理する

	public boolean insertPurchase(User user, List<CartItem> cart) throws Exception {
		Connection con = null;
		PreparedStatement stPurchase = null;
		PreparedStatement stStock = null;
		PreparedStatement stTotal = null;

		try {
			con = this.getConnection();

			// オートコミットをオフにして、すべてのSQLが成功した時だけ確定させる
			con.setAutoCommit(false);

			// 1. 購入履歴テーブル
			String sqlPurchase = "insert into purchases (member_id, item_id, quantity, price, purchase_date) values (?, ?, ?, ?, NOW())";
			stPurchase = con.prepareStatement(sqlPurchase);

			// 2. 商品テーブル(items)の在庫を引くSQL
			String sqlStock = "update items set stock = stock - ? where item_id = ?";
			stStock = con.prepareStatement(sqlStock);

			// 3. 計金額を加算するSQL
			String sqlTotal = "update users set total_purchase_amount = total_purchase_amount + ? where member_id = ?";
			stTotal = con.prepareStatement(sqlTotal);

			// カートに入っている商品の数だけ、ループでSQLを実行する
			for (CartItem cartItem : cart) {

				// 購入履歴のバインド設定
				stPurchase.setString(1, user.getMemberId());
				stPurchase.setInt(2, cartItem.getItem().getItemId());
				stPurchase.setInt(3, cartItem.getQuantity());
				stPurchase.setInt(4, cartItem.getItem().getPrice()); // 価格（税抜）
				stPurchase.executeUpdate();

				// 在庫減算のバインド設定
				stStock.setInt(1, cartItem.getQuantity());
				stStock.setInt(2, cartItem.getItem().getItemId());
				stStock.executeUpdate();

				// 累計金額の加算
				stTotal.setInt(1, cartItem.getSubtotal());
				stTotal.setString(2, user.getMemberId());
				stTotal.executeUpdate();
			}

			// すべての処理が成功したら、DBに反映する
			con.commit();
			return true;

		} catch (Exception e) {
			// 途中でエラーが起きた場合は、すべての処理を無かったことにする
			if (con != null) {
				try {
					con.rollback();
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
			e.printStackTrace();
			throw e;

		} finally {
			if (stPurchase != null) {
				try {
					stPurchase.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (stStock != null) {
				try {
					stStock.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (stTotal != null) {
				try {
					stTotal.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (con != null) {
				try {
					con.setAutoCommit(true);
					con.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	// ユーザーの購入履歴を全件取得するメソッド
	public List<Purchase> getPurchaseHistory(String memberId) throws Exception {
		List<Purchase> list = new ArrayList<>();
		String sql = "SELECT p.*, i.item_name FROM purchases p " +
				"JOIN items i ON p.item_id = i.item_id " +
				"WHERE p.member_id = ? ORDER BY p.purchase_date DESC";

		try (Connection con = this.getConnection();
				PreparedStatement st = con.prepareStatement(sql)) {
			st.setString(1, memberId);
			try (ResultSet rs = st.executeQuery()) {
				while (rs.next()) {
					Purchase p = new Purchase();
					p.setPurchaseId(rs.getInt("purchase_id"));
					p.setMemberId(rs.getString("member_id"));
					p.setItemId(rs.getInt("item_id"));
					p.setItemName(rs.getString("item_name"));
					p.setQuantity(rs.getInt("quantity"));
					p.setPrice(rs.getInt("price"));
					p.setPurchaseDate(rs.getTimestamp("purchase_date"));
					list.add(p);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return list;
	}

	// 直近で挿入された購入IDを取得するメソッド
	public int getLastPurchaseId() throws Exception {
		String sql = "SELECT LAST_INSERT_ID()";
		try (Connection con = getConnection();
				PreparedStatement st = con.prepareStatement(sql);
				ResultSet rs = st.executeQuery()) {
			if (rs.next()) {
				return rs.getInt(1);
			}
		}
		return 0;
	}
}
