package jp.co.aforce.beans;

public class CartItem implements java.io.Serializable {
	private Item item; //肉の商品情報
	private int quantity; //購入数量
	private String serialNumber; //購入時に割り当てられたロット番号

	//デフォルトコンストラクタ
	public CartItem() {
	}

	//引数付きコンストラクタ
	public CartItem(Item item, int quantity) {
		this.item = item;
		this.quantity = quantity;
	}

	// 小計（価格 × 数量）の自動計算ロジック
	public int getSubtotal() {
		return this.item.getPrice() * this.quantity;
	}

	//小計（税込価格 × 数量）の自動計算
	public int getSubtotalWithTax() {
		return (int) (getSubtotal() * 1.08);
	}

	// ゲッター・セッター
	public Item getItem() {
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public String getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}

	// 「どの肉が何個入っているか」が一瞬でコンソールに表示されるようになる
	@Override
	public String toString() {
		if (item == null)
			return "CartItem [商品情報なし]";
		return "CartItem [商品=" + item.getItemName() + "(ID:" + item.getItemId() + "), 単価=" + item.getPrice() + "円, 数量="
				+ quantity + "個, 小計(税抜)=" + getSubtotal() + "円]";
	}
}
