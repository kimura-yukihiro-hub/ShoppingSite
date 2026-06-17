package jp.co.aforce.beans;

public class TypeStat implements java.io.Serializable {
	private String meatType; // 肉の種類 (牛・豚・鶏)
	private int totalQuantity; // 総購入パック数
	private int totalSales; // 合計売上金額

	public TypeStat() {
	}

	// 各プロパティのGetter / Setter
	public String getMeatType() {
		return meatType;
	}

	public void setMeatType(String meatType) {
		this.meatType = meatType;
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
		return "TypeStat [肉の種類=" + meatType + " -> 総数量=" + totalQuantity + "パック, 合計売上=" + totalSales + "円]";
	}
}
