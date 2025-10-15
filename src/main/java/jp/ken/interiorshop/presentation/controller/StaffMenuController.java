package jp.ken.interiorshop.presentation.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

import jp.ken.interiorshop.presentation.form.StaffLoginForm;

@Controller
public class StaffMenuController {
	
	//メニュー画面へ遷移
	@GetMapping(value = "/staffmenu")
	 public String showStaffMenu(@SessionAttribute("loginStaff") StaffLoginForm staffLoginForm, Model model) {
		model.addAttribute("loginStaff", staffLoginForm);
		return "staffMenu";
	}
	
	// 注文管理メニュー画面表示
	@GetMapping("/staffordermenu")
	public String showStaffOrderMenu(@SessionAttribute("loginStaff") StaffLoginForm staffLoginForm, Model model) {
		model.addAttribute("loginStaff", staffLoginForm);
		return "staffOrderMenu";
	}
	
	// 従業員情報管理メニュー画面表示
	@GetMapping("/empinfomenu")
	public String showEmoInfoMenu(@SessionAttribute("loginStaff") StaffLoginForm staffLoginForm, Model model) {
		model.addAttribute("loginStaff", staffLoginForm);
		return "empInfoMenu";
	}
	
}
