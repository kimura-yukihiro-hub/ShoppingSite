package jp.co.aforce.beans;

public class ProductStat implements java.io.Serializable {
	private String productName; // 商品名
	private int totalQuantity; // 総購入数
	private int totalSales; // 合計売上高

	public ProductStat() {
	}

	// Getter / Setter
	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
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
		return "ProductStat [商品名=" + productName +
				" -> 総数量=" + totalQuantity + "パック, 合計売上=" + totalSales + "円]";
	}
}