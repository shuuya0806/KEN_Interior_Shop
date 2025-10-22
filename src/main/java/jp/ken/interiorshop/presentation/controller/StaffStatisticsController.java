package jp.ken.interiorshop.presentation.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import jp.ken.interiorshop.application.service.StaffOrderService;

@Controller
public class StaffStatisticsController {
	StaffOrderService staffOrderService;
	
	public StaffStatisticsController(StaffOrderService staffOrderService){
		this.staffOrderService = staffOrderService;
	}
	
	// 集計管理メニュー画面表示
	@GetMapping("/staffstatistics")
	public String showStaffStatisticsMenu(Model model) {
		return "staffStatisticsMenu";
	}
}
