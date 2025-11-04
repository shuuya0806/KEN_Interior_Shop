package jp.ken.interiorshop.domain.entity;

import lombok.Data;

@Data
public class ItemSalesEntity {

	//商品ID
	private int itemId;
	
	//売り上げ数
	private int itemQuantity;
	
	//商品別売上
	private int total;
	
	//商品名
	private String itemName;
}
