package jp.ken.interiorshop.presentation.form;

import java.io.Serializable;

import lombok.Data;

@Data
public class CategoryForm implements Serializable {
	//カテゴリーID
	private Integer categoryId;
	
	//カテゴリー名
	private String categoryName;
}