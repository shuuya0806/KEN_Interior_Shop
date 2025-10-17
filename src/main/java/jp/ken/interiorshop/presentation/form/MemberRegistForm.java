package jp.ken.interiorshop.presentation.form;

import java.io.Serializable;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jp.ken.interiorshop.common.validatior.groups.ValidGroup1;
import jp.ken.interiorshop.common.validatior.groups.ValidGroup2;
import lombok.Data;

@Data
public class MemberRegistForm implements Serializable {
	//会員ID
	private Integer memberId;
	
	//氏名
	@NotEmpty(message = "必須入力です",  groups = ValidGroup1.class)
	@Pattern(regexp = "^[一-龥々ぁ-んァ-ヶー]+$", message = "名前は漢字・ひらがな・カタカナで入力してください",  groups = ValidGroup2.class)
	private String memberName;
	
	//フリガナ
	@NotEmpty(message = "必須入力です",  groups = ValidGroup1.class)	
	@Pattern(regexp = "^[ァ-ヶー]+$", message = "フリガナは全角カタカナのみで入力してください",  groups = ValidGroup2.class)
	private String memberKana;

	//メールアドレス
	@NotEmpty(message = "必須入力です", groups = ValidGroup1.class)
	private String mail;

	//パスワード
	@NotEmpty(message = "必須入力です", groups = ValidGroup1.class)
	private String password;

	//電話番号
	@NotEmpty(message = "必須入力です", groups = ValidGroup1.class)
    @Pattern(regexp = "^[0-9]{10,11}$", message = "電話番号はハイフンなしの10～11桁の数字で入力してください", groups = ValidGroup2.class)
	private String phoneNumber;
	
	//郵便番号
    @Pattern(regexp = "^$|^[0-9]{7}$", message = "郵便番号は空欄もしくはハイフンなしの7桁の数字で入力してください", groups = ValidGroup2.class)
	private String postalCode;

	//住所1(都道府県)
	@NotEmpty(message = "必須入力です", groups = ValidGroup1.class)
	private String address1;

	//住所2(市区町村)
	@NotEmpty(message = "必須入力です", groups = ValidGroup1.class)
	private String address2;

	//住所3(番地)
	@NotEmpty(message = "必須入力です", groups = ValidGroup1.class)
	private String address3;
	
	//クレジットカード番号
	private Integer creditNo;
	
	//有効期限(月)
	private Integer creditMonth;
	
	//有効期限(年)
	private Integer creditYear;
	
	//カード名義
	private String creditName;
	
	//セキュリティコード
	private Integer securityCode;
	
	//退会フラグ 初期値は0
	private Integer cancel = 0; 
	
	//ポイント 初期値は0
	private Integer point = 0;
}
