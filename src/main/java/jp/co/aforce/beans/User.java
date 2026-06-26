package jp.co.aforce.beans;

public class User implements java.io.Serializable {

	private String memberId; //会員番号
	private String password; //パスワード
	private String lastName; //名前_性
	private String firstName; //名前_名
	private String zipCode; //郵便番号
	private String address; //住所
	private String mailAddress; //メールアドレス
	private int meatRank; //会員ランク
	private long totalPurchaseAmount;
	private java.util.Date lastPurchaseDate;

	public User() {
	}

	// JSP画面や管理画面で、数値（1〜5）ではなく「ゴールド会員」のように綺麗な文字で出すための変換
	public String getMeatRankName() {
		switch (this.meatRank) {
		case 1:
			return "一般";
		case 2:
			return "ゴールド";
		case 3:
			return "プラチナ";
		case 4:
			return "VIP";
		case 5:
			return "管理者";
		default:
			return "ゲスト";
		}
	}

	public double getDiscountRate() {
		switch (this.meatRank) {
		case 2:
			return 0.02; // ゴールド会員 2%
		case 3:
			return 0.05; // プラチナ会員 5%
		case 4:
			return 0.10; // VIP会員 10%
		default:
			return 0.0; // 一般会員など
		}
	}

	public long getNextRankGoal() {
		switch (this.meatRank) {
		case 1:
			return 10000;
		case 2:
			return 30000;
		case 3:
			return 100000;
		default:
			return 100000; // VIP以上
		}
	}

	public int getProgressPercent() {
		long goal = getNextRankGoal();
		if (goal <= 0 || this.meatRank >= 4)
			return 100;
		return (int) Math.min(((double) this.totalPurchaseAmount / (double) goal) * 100.0, 100.0);
	}

	public String getMemberId() {
		return memberId;
	}

	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getMailAddress() {
		return mailAddress;
	}

	public void setMailAddress(String mailAddress) {
		this.mailAddress = mailAddress;
	}

	public int getMeatRank() {
		return meatRank;
	}

	public void setMeatRank(int meatRank) {
		this.meatRank = meatRank;
	}

	public long getTotalPurchaseAmount() {
		return totalPurchaseAmount;
	}

	public void setTotalPurchaseAmount(long totalPurchaseAmount) {
		this.totalPurchaseAmount = totalPurchaseAmount;
	}

	public java.util.Date getLastPurchaseDate() {
		return lastPurchaseDate;
	}

	public void setLastPurchaseDate(java.util.Date lastPurchaseDate) {
		this.lastPurchaseDate = lastPurchaseDate;
	}

	// 中身のデータがコンソールに綺麗に全出力されるようになる
	@Override
	public String toString() {
		return "UserBean [memberId=" + memberId + ", lastName=" + lastName + ", firstName=" + firstName
				+ ", mailAddress=" + mailAddress + ", meatRank=" + getMeatRankName() + "]";
	}
}
