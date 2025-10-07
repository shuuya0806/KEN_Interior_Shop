package jp.ken.interiorshop.presentation.form;

import java.io.Serializable;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class StaffLoginForm implements Serializable {
	
	@NotNull(message="従業員IDを入力して下さい")
	private Integer staffId;
	
	private String staffName;

	@NotEmpty(message="パスワードを入力して下さい")
	private String password;
	
	private String administrator;
}

