package jp.co.aforce.beans;

public class User implements java.io.Serializable {

	private String memberId; 	//会員番号
	private String password; 	//パスワード
	private String lastName; 	//名前_性
	private String firstName; 	//名前_名
	private String address; 	//住所
	private String mailAddress; //メールアドレス
	private int meatRank; 		//会員ランク

	public User() {

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
}
