package jp.co.aforce.beans;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class Lot {
	private int lotId;
	private int itemId;
	private String serialNumber;
	private LocalDateTime arrivalDate;
	private int meatStatus;
	private String batchId;
	private int stock;
	private LocalDateTime agingStartDate;
	public static final int STATUS_AVAILABLE = 0; // 在庫
	public static final int STATUS_AGING = 1; // 熟成中
	public static final int STATUS_DISCARDED = 2; // 廃棄
	public static final int STATUS_SOLD = 3; // 販売済み

	// 熟成日数を計算して返すメソッド
	public long getAgingDays() {
		if (this.agingStartDate == null || this.meatStatus != STATUS_AGING) {
			return 0;
		}
		// between は「差分」なので、+1 することで「経過日数（初日を1とする）」になる
		return ChronoUnit.DAYS.between(this.agingStartDate.toLocalDate(), LocalDate.now()) + 1;
	}

	// ゲッター・セッターを記述
	public int getLotId() {
		return lotId;
	}

	public void setLotId(int lotId) {
		this.lotId = lotId;
	}

	public int getItemId() {
		return itemId;
	}

	public void setItemId(int itemId) {
		this.itemId = itemId;
	}

	public String getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}

	public LocalDateTime getArrivalDate() {
		return arrivalDate;
	}

	public void setArrivalDate(LocalDateTime arrivalDate) {
		this.arrivalDate = arrivalDate;
	}

	public int getMeatStatus() {
		return meatStatus;
	}

	public void setMeatStatus(int meatStatus) {
		this.meatStatus = meatStatus;
	}

	public LocalDateTime getAgingStartDate() {
		return agingStartDate;
	}

	public void setAgingStartDate(LocalDateTime agingStartDate) {
		this.agingStartDate = agingStartDate;
	}

	public int getStock() {
		return stock;
	}

	public void setStock(int stock) {
		this.stock = stock;
	}

	public String getBatchId() {
		return batchId;
	}

	public void setBatchId(String batchId) {
		this.batchId = batchId;
	}
}