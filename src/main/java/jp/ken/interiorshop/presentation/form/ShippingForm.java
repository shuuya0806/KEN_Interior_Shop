package jp.ken.interiorshop.presentation.form;

import java.io.Serializable;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jp.ken.interiorshop.common.validatior.groups.ValidGroup1;
import jp.ken.interiorshop.common.validatior.groups.ValidGroup2;
import lombok.Data;

@Data
public class ShippingForm implements Serializable {

    //発送先ID
    private String shippingId;
    
    //発送先氏名
    @NotBlank(message = "必須入力です", groups = ValidGroup1.class)
	@Pattern(regexp = "^[一-龥々ぁ-んァ-ヶー]+$", message = "名前は漢字・ひらがな・カタカナで入力してください",  groups = ValidGroup2.class)
    private String shippingName;
    
    //発送先フリガナ
    @NotBlank(message = "必須入力です", groups = ValidGroup1.class)
    @Pattern(regexp = "^[ァ-ヶー]+$", message = "フリガナは全角カタカナのみで入力してください", groups = ValidGroup2.class)
    private String shippingKana;
    
    //発送先電話番号
    @NotBlank(message = "必須入力です", groups = ValidGroup1.class)
    @Pattern(regexp = "^[0-9]{10,11}$", message = "電話番号はハイフンなしの10～11桁の数字で入力してください", groups = ValidGroup2.class)
    private String shippingphone;
    
    //発送先郵便番号
    @Pattern(regexp = "^$|^[0-9]{7}$", message = "郵便番号は空欄もしくはハイフンなしの7桁の数字で入力してください", groups = ValidGroup2.class)
    private String shippingPostalCode;
    
    //発送先都道府県
    @NotBlank(message = "必須入力です", groups = ValidGroup1.class)
    private String shippingAddress1;
    
    //発送先市区町村
    @NotBlank(message = "必須入力です", groups = ValidGroup1.class)
    private String shippingAddress2;
    
    //発送先番地
    @NotBlank(message = "必須入力です", groups = ValidGroup1.class)
    private String shippingAddress3;
}