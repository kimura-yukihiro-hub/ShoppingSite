package jp.co.aforce.beans;

public class CategoryStat implements java.io.Serializable {
	private String category; // 部位・カテゴリ名 (カルビ、ヒレなど)
	private int totalQuantity; // 総購入パック数
	private int totalSales; // 合計売上金額

	public CategoryStat() {
	}

	// 各プロパティのGetter / Setter
	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public int getTotalQuantity() {
		return totalQuantity;
	}

	public void setTotalQuantity(int totalQuantity) {
		this.totalQuantity = totalQuantity;
	}

	public int getTotalSales() {
		return totalSales;
	}

	public void setTotalSales(int totalSales) {
		this.totalSales = totalSales;
	}

	@Override
	public String toString() {
		return "CategoryStat [部位カテゴリ=" + category + " -> 総数量=" + totalQuantity + "パック, 合計売上=" + totalSales + "円]";
	}
}
