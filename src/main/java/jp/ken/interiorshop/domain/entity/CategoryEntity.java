package jp.ken.interiorshop.domain.entity;

import java.io.Serializable;

import lombok.Data;

@Data
public class CategoryEntity implements Serializable{
	//カテゴリーID
	private int categoryId;
	
	//カテゴリー名
	private String categoryName;
}