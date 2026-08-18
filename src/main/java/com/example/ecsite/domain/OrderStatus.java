package com.example.ecsite.domain;

/** 注文ステータス */
public enum OrderStatus {
	/** 受付済み */
	PENDING,
	/** 支払い完了 */
	PAID,
	/** 発送済み */
	SHIPPED,
	/** キャンセル */
	CANCELLED
}
