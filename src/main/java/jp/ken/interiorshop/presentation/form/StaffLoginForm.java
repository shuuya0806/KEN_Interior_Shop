package jp.ken.interiorshop.presentation.form;

import java.io.Serializable;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jp.ken.interiorshop.common.validatior.groups.ValidGroup1;
import jp.ken.interiorshop.common.validatior.groups.ValidGroup2;
import lombok.Data;

@Data
public class StaffLoginForm implements Serializable {
	
	@NotNull(message = "必須入力です", groups = ValidGroup1.class)
	@Min(value = 1000, message = "4桁の数字で入力してください", groups = ValidGroup2.class)
	private Integer staffId;
	
	@NotEmpty(message = "必須入力です",  groups = ValidGroup1.class)
	@Pattern(regexp = "^[一-龥々ぁ-んァ-ヶー]+$", message = "名前は漢字・ひらがな・カタカナで入力してください",  groups = ValidGroup2.class)
	private String staffName;

	@NotEmpty(message="パスワードを入力して下さい", groups = ValidGroup1.class)
	private String password;
	
	//初期値は従業員
	@NotEmpty(message="権限を選択して下さい", groups = ValidGroup1.class)
	private String administrator = "従業員";
}

