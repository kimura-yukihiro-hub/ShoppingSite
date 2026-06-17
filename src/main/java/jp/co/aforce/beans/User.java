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

	public User() {
	}

	// JSP画面や管理画面で、数値（1〜5）ではなく「ゴールド会員」のように綺麗な文字で出すための変換
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
			return "ゲスト会員";
		}
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

	// 中身のデータがコンソールに綺麗に全出力されるようになる
	@Override
	public String toString() {
		return "UserBean [memberId=" + memberId + ", lastName=" + lastName + ", firstName=" + firstName
				+ ", mailAddress=" + mailAddress + ", meatRank=" + getMeatRankName() + "]";
	}
}
