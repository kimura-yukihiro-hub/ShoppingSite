package jp.co.aforce.beans;

public class PurchaseStat implements java.io.Serializable {
	private int meatRank; // 会員のランク(1～４）5は管理者
	private int uniqueUsers; // そのランクの購入者数(人数)
	private int totalQuantity; //そのランクのお肉のそう購入点数(パック数)
	private int totalSales; //そのランクの総売り上げ金額(円)

	public PurchaseStat() {
	}

	public String getMeatRankName() {
		switch (this.meatRank) {
		case 1:
			return "一般会員";
		case 2:
			return "ゴールド会員";
		case 3:
			return "プラチナ会員";
		case 4:
			return "VIP会員";
		case 5:
			return "管理者";
		default:
			return "ゲスト・その他";
		}
	}

	// Getter / Setter
	public int getMeatRank() {
		return meatRank;
	}

	public void setMeatRank(int meatRank) {
		this.meatRank = meatRank;
	}

	public int getUniqueUsers() {
		return uniqueUsers;
	}

	public void setUniqueUsers(int uniqueUsers) {
		this.uniqueUsers = uniqueUsers;
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
}
