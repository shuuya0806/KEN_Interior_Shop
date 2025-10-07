package jp.ken.interiorshop.presentation.form;

import java.io.Serializable;

import lombok.Data;

@Data
public class OrderDetailsForm implements Serializable {
	
	//商品詳細ID
	private String detailId;
	
	//注文ID
	private String orderId;
	
	//商品ID
	private String itemId;
	
	//商品名
	private String itemName;
	
	//注文数
	private String itemQuantity;
	
	//小計
	private String subtotal;
}
