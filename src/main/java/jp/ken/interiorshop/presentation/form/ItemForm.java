package jp.ken.interiorshop.presentation.form;

import java.io.Serializable;

import lombok.Data;

@Data
public class ItemForm implements Serializable {
	
	//商品ID
	private String itemId;
	
	//商品名
	private String itemName;
	
	//商品数
	private int itemQuantity = 1; // デフォルト 1
	
	//カテゴリーID
	private String categoryId;
	
	//価格
	private String itemPrice;
	
	//発売開始日
	private String rsDate;
	
	//画像データ
	private String image;
	
	//商品説明
	private String explanation;
	
	//在庫数
	private String stock;
	
	//セールフラグ
	private String saleFrag;
	
	//セール価格
	private String salePrice;
}