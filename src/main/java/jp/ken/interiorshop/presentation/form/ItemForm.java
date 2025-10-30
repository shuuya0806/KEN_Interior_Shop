package jp.ken.interiorshop.presentation.form;

import java.io.Serializable;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jp.ken.interiorshop.common.validatior.groups.ValidGroup1;
import jp.ken.interiorshop.common.validatior.groups.ValidGroup2;
import lombok.Data;

@Data
public class ItemForm implements Serializable {
	
	//商品ID
	private String itemId;
	
	//商品名
	@NotBlank(message = "商品名は必須です", groups = ValidGroup1.class)
	private String itemName;
	
	//商品数
	private int itemQuantity = 1; // デフォルト 1
	
	//カテゴリーID
	@NotBlank(message = "カテゴリーIDは必須です", groups = ValidGroup1.class)
	@Pattern(regexp = "^[0-9]+$", message = "カテゴリーIDは数字のみで入力してください", groups = ValidGroup2.class)
	private String categoryId;
	
	//価格
	@NotBlank(message = "価格は必須です", groups = ValidGroup1.class)
	@Pattern(regexp = "^[0-9]+$", message = "価格は数字のみで入力してください", groups = ValidGroup2.class)
	private String itemPrice;
	
	//発売開始日
	@NotBlank(message = "発売日は必須です", groups = ValidGroup1.class)
	@Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "発売日はYYYY-MM-DD形式で入力してください", groups = ValidGroup2.class)
	private String rsDate;
	
	//画像データ
	@NotBlank(message = "画像データは必須です", groups = ValidGroup1.class)
	private String image;
	
	//商品説明
	@NotBlank(message = "説明は必須です", groups = ValidGroup1.class)
	private String explanation;
	
	//在庫数
	@NotBlank(message = "在庫数は必須です", groups = ValidGroup1.class)
	@Pattern(regexp = "^[0-9]+$", message = "在庫数は数字のみで入力してください", groups = ValidGroup2.class)
	private Integer stock;
	
	//セールフラグ
	@NotBlank(message = "セールフラグは必須です", groups = ValidGroup1.class)
	private String saleFrag;
	
	//セール価格
	@NotBlank(message = "セール価格は必須です", groups = ValidGroup1.class)
	@Pattern(regexp = "^[0-9]+$", message = "セール価格は数字のみで入力してください", groups = ValidGroup2.class)
	private String salePrice;

}