package jp.ken.interiorshop.presentation.form;

import java.io.Serializable;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.Min;
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
	@NotBlank(message = "発売日を入力してください", groups = ValidGroup1.class)
	@Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "発売日はYYYY-MM-DD形式で入力してください", groups = ValidGroup2.class)
	private String rsDate;
	
	//画像データ
	private String image;
	
	//画像アップロード用
	private MultipartFile imageFile;
	
	// 確認画面用に画像データをバイト配列で保持
	private byte[] imageBytes; 
	
	// ファイル名保持
	private String originalFilename;
	
	//商品説明
	@NotBlank(message = "説明は必須です", groups = ValidGroup1.class)
	private String explanation;
	
	//在庫数
	@NotBlank(message = "在庫数は必須です", groups = ValidGroup1.class)
	@Pattern(regexp = "^[0-9]+$", message = "在庫数は数字のみで入力してください", groups = ValidGroup2.class)
	private String stock;
	
	//セールフラグ
	private String saleFrag;
	
	//セール価格
	@Min(value = 0, message = "セール価格は0以上で入力してください", groups = ValidGroup1.class)
	private int salePrice;
}