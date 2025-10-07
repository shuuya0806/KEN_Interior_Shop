package jp.ken.interiorshop.domain.entity;

import lombok.Data;

@Data
public class OrderDetailsEntity {
	
	//商品詳細ID
	private int detailId;
	
	//注文ID
	private int orderId;
	
	//商品ID
	private int itemId;
	
	//注文数
	private int itemQuantity;
	
	//小計
	private int subtotal;
}
