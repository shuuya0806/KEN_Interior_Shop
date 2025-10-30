package jp.ken.interiorshop.presentation.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import jp.ken.interiorshop.application.service.StaffLoginService;
import jp.ken.interiorshop.presentation.form.StaffLoginForm;

@Controller
public class StaffLoginController {
	
	private StaffLoginService staffLoginService;
	
	public StaffLoginController(StaffLoginService staffLoginService) {
		this.staffLoginService = staffLoginService;
	}
	
	//ログイン画面表示
	@GetMapping("/stafflogin")
	public String getLoginForm(StaffLoginForm staffLoginForm) {
		staffLoginForm = new StaffLoginForm();
		return "staffLogin";
	}
	
	//ログイン処理
	@PostMapping(value = "/stafflogin")
	 public String doLogin(@Valid StaffLoginForm staffLoginForm,
			 BindingResult result, Model model, HttpSession session) {
		
		//エラー時にログイン画面に戻る
		if(result.hasErrors()) {
			return "staffLogin";
		}

		try {
			//全メンバー情報を取得し、リストに保存
			List<StaffLoginForm> login = staffLoginService.getStaffList();
			
			boolean match = false;
		
			//従業員IDとパスワードが一致していれば、matchをtrueに
			StaffLoginForm matchedStaff = null;
			for(StaffLoginForm sta : login) {
				if(sta.getStaffId().equals(staffLoginForm.getStaffId())
				&& sta.getPassword().equals(staffLoginForm.getPassword())){
					match = true;
					
					matchedStaff = sta;
					break;
					
				}
			}
			
			if(match) {
				matchedStaff.getStaffId();
				//ログイン情報をsessionに保存
				staffLoginForm = new StaffLoginForm();
				staffLoginForm.setStaffId(matchedStaff.getStaffId());
				staffLoginForm.setStaffName(matchedStaff.getStaffName());
				staffLoginForm.setPassword(matchedStaff.getPassword());
				staffLoginForm.setAdministrator(matchedStaff.getAdministrator());
				session.setAttribute("loginStaff", staffLoginForm);
				return "redirect:/staffmenu";
			}else {
				model.addAttribute("staffLoginError", "従業員IDまたはパスワードが正しくありません");
				return "staffLogin";
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			model.addAttribute("staffLoginError", "システムエラーが発生しました");
			return "staffLogin";
		}
	}
    // ログアウト処理
    @GetMapping("/stafflogout")
    public String logout(HttpSession session) {
        // セッション破棄
        session.invalidate();
        // ログイン画面にリダイレクト
        return "redirect:/stafflogin";
    }
}

