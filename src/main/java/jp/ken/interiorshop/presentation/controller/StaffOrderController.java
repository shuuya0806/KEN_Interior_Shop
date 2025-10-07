package jp.ken.interiorshop.presentation.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import jp.ken.interiorshop.application.service.StaffOrderService;
import jp.ken.interiorshop.presentation.form.OrderForm;

@Controller
public class StaffOrderController {
	
	StaffOrderService staffOrderService;
	
	public StaffOrderController(StaffOrderService staffOrderService){
		this.staffOrderService = staffOrderService;
	}

	@GetMapping(value = "/stafforder")
	public String OrderManagement() throws Exception{
		
		//DBから注文履歴を取得
		List<OrderForm> listOrderForm = staffOrderService.getOrderList();
		
		return "redirect:/staffOrderHistory";
	}
}
