package jp.ken.interiorshop.presentation.form;

import java.io.Serializable;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class MemberLoginForm implements Serializable {
	
	//会員ID
	private int memberId;
	
	//氏名
	private String memberName;
	
	//メールアドレス
	@NotEmpty(message = "メールアドレスを入力して下さい")
	private String mail;
	
	//パスワード
	@NotEmpty(message = "パスワードを入力して下さい")
	private String password;
}
