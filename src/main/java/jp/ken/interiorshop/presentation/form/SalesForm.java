package jp.ken.interiorshop.presentation.form;

import java.io.Serializable;

import lombok.Data;

@Data
public class SalesForm implements Serializable {
	
	//商品ID
	private Integer itemId;
	
	//売り上げ数
	private String itemQuantity;
	
	//商品別売上
	private String total;
	
	//商品名
	private String itemName;
	
	//売上
	private String sales;
}
