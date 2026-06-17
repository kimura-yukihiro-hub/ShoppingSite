package jp.co.aforce.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import jp.co.aforce.beans.CategoryStat;
import jp.co.aforce.beans.ProductStat;
import jp.co.aforce.beans.PurchaseStat;
import jp.co.aforce.beans.TypeStat;

public class SalesDAO extends DAO {

	// 期間指定用の補助メソッド：条件文を構築

	private String getWhereClause(String startDate, String endDate) {
		if (startDate != null && !startDate.isEmpty() && endDate != null && !endDate.isEmpty()) {
			return " where p.purchase_date between ? and ?";
		}
		return "";
	}

	// 期間指定用の補助メソッド：パラメータをセット

	private void setDateParameters(PreparedStatement st, String startDate, String endDate, int startIndex)
			throws Exception {
		if (startDate != null && !startDate.isEmpty() && endDate != null && !endDate.isEmpty()) {
			st.setString(startIndex, startDate);
			st.setString(startIndex + 1, endDate);
		}
	}

	// 会員ランク別の購入者傾向と売上統計を取得するメソッド

	public List<PurchaseStat> getPurchaseStatsByRank(String startDate, String endDate) throws Exception {
		List<PurchaseStat> list = new ArrayList<>();
		String where = getWhereClause(startDate, endDate);

		// 会員ランク(meat_rank)ごとに「何人買ったか」「合計何個買ったか」「売上総額」を自動集計するSQL文
		String sql = "SELECT u.meat_rank, COUNT(DISTINCT p.member_id) AS user_count, "
				+ "SUM(p.quantity) AS total_qty, SUM(p.quantity * p.price) AS total_sales "
				+ "FROM purchases p JOIN users u ON p.member_id = u.member_id "
				+ (where.isEmpty() ? " WHERE u.meat_rank <= 4 " : where + " AND u.meat_rank <= 4 ")
				+ "GROUP BY u.meat_rank ORDER BY u.meat_rank DESC";

		try (Connection con = this.getConnection(); PreparedStatement st = con.prepareStatement(sql)) {
			setDateParameters(st, startDate, endDate, 1);
			try (ResultSet rs = st.executeQuery()) {
				while (rs.next()) {
					PurchaseStat stat = new PurchaseStat();
					stat.setMeatRank(rs.getInt("meat_rank"));
					stat.setUniqueUsers(rs.getInt("user_count"));
					stat.setTotalQuantity(rs.getInt("total_qty"));
					stat.setTotalSales(rs.getInt("total_sales"));
					list.add(stat);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return list;
	}

	// お店全体の累計売上総額を取得するメソッド

	public int getTotalSalesAmount(String startDate, String endDate) throws Exception {
		String sql = "SELECT SUM(quantity * price) AS total FROM purchases p" + getWhereClause(startDate, endDate);

		try (Connection con = this.getConnection();
				PreparedStatement st = con.prepareStatement(sql)) {
			setDateParameters(st, startDate, endDate, 1);
			try (ResultSet rs = st.executeQuery()) {
				if (rs.next()) {
					return rs.getInt("total");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return 0;
	}

	// お肉の種類別（牛・豚・鶏）の販売傾向を取得するメソッド

	public List<TypeStat> getSalesStatsByType(String startDate, String endDate) throws Exception {
		List<TypeStat> list = new ArrayList<>();
		// purchases(p) と items(i) を結合して、肉の種類ごとに合計金額とパック数を集計
		String sql = "SELECT i.meat_type, SUM(p.quantity) AS total_qty, SUM(p.quantity * p.price) AS total_sales "
				+ "FROM purchases p JOIN items i ON p.item_id = i.item_id " + getWhereClause(startDate, endDate)
				+ "GROUP BY i.meat_type ORDER BY total_sales DESC";
		try (Connection con = this.getConnection(); PreparedStatement st = con.prepareStatement(sql)) {
			setDateParameters(st, startDate, endDate, 1);
			try (ResultSet rs = st.executeQuery()) {
				while (rs.next()) {
					TypeStat stat = new TypeStat();
					stat.setMeatType(rs.getString("meat_type"));
					stat.setTotalQuantity(rs.getInt("total_qty"));
					stat.setTotalSales(rs.getInt("total_sales"));
					list.add(stat);
				}
			}
		}
		return list;
	}

	// お肉の部位別（カテゴリ別）の人気ランキング（上位5件）を取得するメソッド

	public List<CategoryStat> getSalesStatsByCategory(String startDate, String endDate) throws Exception {
		List<CategoryStat> list = new ArrayList<>();
		// 部位ごとに集計し、売上が高い順に上位5件(LIMIT 5)を抽出
		String sql = "SELECT i.category, SUM(p.quantity) AS total_qty, SUM(p.quantity * p.price) AS total_sales "
				+ "FROM purchases p JOIN items i ON p.item_id = i.item_id " + getWhereClause(startDate, endDate)
				+ "GROUP BY i.category ORDER BY total_sales DESC LIMIT 5";
		try (Connection con = this.getConnection(); PreparedStatement st = con.prepareStatement(sql)) {
			setDateParameters(st, startDate, endDate, 1);
			try (ResultSet rs = st.executeQuery()) {
				while (rs.next()) {
					CategoryStat stat = new CategoryStat();
					stat.setCategory(rs.getString("category"));
					stat.setTotalQuantity(rs.getInt("total_qty"));
					stat.setTotalSales(rs.getInt("total_sales"));
					list.add(stat);
				}
			}
		}
		return list;
	}

	// 商品別の人気ランキング（上位10件）を取得するメソッド
	public List<ProductStat> getSalesStatsByProduct(String startDate, String endDate) throws Exception {
		List<ProductStat> list = new ArrayList<>();
		// 商品名ごとに集計し、売上が高い順に上位10件を抽出
		String sql = "SELECT i.item_name, SUM(p.quantity) AS total_qty, SUM(p.quantity * p.price) AS total_sales "
				+ "FROM purchases p JOIN items i ON p.item_id = i.item_id " + getWhereClause(startDate, endDate)
				+ "GROUP BY i.item_name ORDER BY total_sales DESC LIMIT 10";
		try (Connection con = this.getConnection(); PreparedStatement st = con.prepareStatement(sql)) {
			setDateParameters(st, startDate, endDate, 1);
			try (ResultSet rs = st.executeQuery()) {
				while (rs.next()) {
					ProductStat stat = new ProductStat();
					stat.setProductName(rs.getString("item_name"));
					stat.setTotalQuantity(rs.getInt("total_qty"));
					stat.setTotalSales(rs.getInt("total_sales"));
					list.add(stat);
				}
			}
		}
		return list;
	}
}
