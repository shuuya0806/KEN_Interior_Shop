package jp.ken.interiorshop.application.controlleradvice;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import jakarta.servlet.http.HttpSession;
import jp.ken.interiorshop.presentation.form.MemberLoginForm;

@ControllerAdvice
public class GlobalControllerAdvice {



	//ログインしているユーザー名
	@ModelAttribute("loginUser")
	public MemberLoginForm memberLoginForm(HttpSession session) {
		
		// セッションにログイン情報の有無確認
		MemberLoginForm memberLoginForm = (MemberLoginForm) session.getAttribute("loginUser");
		
		if(memberLoginForm == null) {
			
			return new MemberLoginForm();
		} else {
			
			return memberLoginForm;
		}
	}
	
	@ModelAttribute
    public void addLoginFlag(HttpSession session, Model model) {
		
        // セッションに "userId" があればログイン済みと判定しモデルに格納
        boolean isLogin = session.getAttribute("loginUser") != null;
        model.addAttribute("isLogin", isLogin);
        
        // ポップアップ用メッセージと色を1回だけ渡す
        String message = (String) session.getAttribute("popupMessage");
        String color = (String) session.getAttribute("popupColor");

        if (message != null && color != null) {
        	
        	// ポップアップ用メッセージと色をモデルに格納
            model.addAttribute("popupMessage", message);
            model.addAttribute("popupColor", color);

            // セッションから削除して1回だけ表示
            session.removeAttribute("popupMessage");
            session.removeAttribute("popupColor");
        }
    }
	

/*	//IllegalArgumentException を捕捉
	@ExceptionHandler(IllegalArgumentException.class)
	public String handleIllegalArgumentException(IllegalArgumentException ex, Model model) {
		model.addAttribute("errorMessage", ex.getMessage());
		return "error"; // error.html に遷移
	}

	// その他の例外を捕捉
	@ExceptionHandler(Exception.class)
	public String handleException(Exception ex, Model model) {
		model.addAttribute("errorMessage", "予期しないエラーが発生しました");
		// ログ出力もしておくとデバッグに便利
		ex.printStackTrace();
		return "error";
	}*/

}