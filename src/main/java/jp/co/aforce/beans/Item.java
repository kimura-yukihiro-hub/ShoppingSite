package jp.co.aforce.beans;

public class Item implements java.io.Serializable {

	//データベース（itemsテーブル）の列と100%完全同期させたプロパティ群
	private int itemId; //商品ID
	private String itemName; //商品名
	private int price; //価格(本体価格・税抜)
	private String meatType; //肉の種類
	private String category; //部位カテゴリー
	private String description; //商品説明文
	private String imagePath; //画像ファイル名
	private int stock; //在庫数
	private String meatStatus; // 生、熟成などの状態

	public Item() {
	}

	//この商品単体の「税込価格」を自動計算
	public int getPriceWithTax() {
		return (int) (this.price * 1.08);
	}

	//各プロパティのGetter/Setter

	public int getItemId() {
		return itemId;
	}

	public void setItemId(int itemId) {
		this.itemId = itemId;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public String getMeatType() {
		return meatType;
	}

	public void setMeatType(String meatType) {
		this.meatType = meatType;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

	public int getStock() {
		return stock;
	}

	public void setStock(int stock) {
		this.stock = stock;
	}

	public String getMeatStatus() {
		return meatStatus;
	}

	public void setMeatStatus(String meatStatus) {
		this.meatStatus = meatStatus;
	}

	@Override
	public String toString() {
		return "ItemBean [ID=" + itemId + ", 商品名=" + itemName + ", 税抜単価=" + price + "円, 税込単価=" + getPriceWithTax()
				+ "円, 在庫=" + stock + ", 状態=" + meatStatus + "]";
	}

}
