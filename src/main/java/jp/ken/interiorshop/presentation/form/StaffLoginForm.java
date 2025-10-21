package jp.ken.interiorshop.presentation.form;

import java.io.Serializable;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jp.ken.interiorshop.common.validatior.groups.ValidGroup1;
import lombok.Data;

@Data
public class StaffLoginForm implements Serializable {
	
	@NotNull(message = "必須入力です", groups = ValidGroup1.class)
	private Integer staffId;
	
	@NotEmpty(message = "必須入力です",  groups = ValidGroup1.class)
	private String staffName;

	@NotEmpty(message="パスワードを入力して下さい", groups = ValidGroup1.class)
	private String password;
	
	//初期値は従業員
	@NotEmpty(message="権限を選択して下さい", groups = ValidGroup1.class)
	private String administrator = "従業員";
}

