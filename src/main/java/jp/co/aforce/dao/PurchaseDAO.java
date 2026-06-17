package jp.co.aforce.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

import jp.co.aforce.beans.CartItem;
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

		try {
			con = this.getConnection();

			// オートコミットをオフにして、すべてのSQLが成功した時だけ確定させる
			con.setAutoCommit(false);

			//1. 購入履歴テーブル
			String sqlPurchase = "insert into purchases (member_id, item_id, quantity, price, purchase_date) values (?, ?, ?, ?, NOW())";
			stPurchase = con.prepareStatement(sqlPurchase);

			//2. 商品テーブル(items)の在庫を引くSQL
			String sqlStock = "update items set stock = stock - ? where item_id = ?";
			stStock = con.prepareStatement(sqlStock);

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
}
