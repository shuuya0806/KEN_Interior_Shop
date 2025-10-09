package jp.ken.interiorshop.presentation.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.SessionAttribute;

import jp.ken.interiorshop.application.service.StaffOrderService;
import jp.ken.interiorshop.presentation.form.OrderForm;
import jp.ken.interiorshop.presentation.form.StaffLoginForm;

@Controller
public class StaffOrderController {
	
	StaffOrderService staffOrderService;
	
	public StaffOrderController(StaffOrderService staffOrderService){
		this.staffOrderService = staffOrderService;
	}

	@GetMapping(value = "/stafforder")
	public String OrderManagement(Model model) throws Exception{
		
		//DBから注文履歴を取得
		List<OrderForm> listOrderForm = staffOrderService.getOrderList();
		model.addAttribute("listOrderForm", listOrderForm);
		
		return "redirect:/staffOrderHistory";
	}
	
	// 注文履歴確認画面表示
	@GetMapping("/stafforder/history")
	public String showOrderHistory(@SessionAttribute("loginStaff") StaffLoginForm staffLoginForm, Model model) 
		throws Exception{
		model.addAttribute("loginStaff", staffLoginForm);
		
//		//DBから注文履歴を取得
		List<OrderForm> listOrderForm = staffOrderService.getOrderList();
		model.addAttribute("listOrderForm", listOrderForm);
		
		return "staffOrderHistory";
	}
	// 注文詳細画面表示
	@GetMapping("/stafforder/detail/{id}")
	public String showOrderDetail(@PathVariable("id") int orderId,
	                              @SessionAttribute("loginStaff") StaffLoginForm staffLoginForm,
	                              Model model) throws Exception {
	    model.addAttribute("loginStaff", staffLoginForm);

	    // ID に対応する注文情報 + 注文詳細 + 商品情報をまとめて取得
	    OrderForm orderForm = staffOrderService.getOrderDetailById(orderId);
	    
	    model.addAttribute("orderForm", orderForm);

	    return "staffOrderDetail"; // 詳細ページのテンプレート名
	}

}
