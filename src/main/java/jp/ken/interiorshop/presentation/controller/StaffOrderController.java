package jp.ken.interiorshop.presentation.controller;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;

import jp.ken.interiorshop.application.service.StaffOrderService;
import jp.ken.interiorshop.presentation.form.OrderDetailsForm;
import jp.ken.interiorshop.presentation.form.OrderForm;
import jp.ken.interiorshop.presentation.form.ShippingForm;
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
		
		//DBから注文履歴を取得
		List<OrderForm> listOrderForm = staffOrderService.getOrderList();
		
		//nullチェック
		if (listOrderForm == null || listOrderForm.isEmpty()) {
	        model.addAttribute("message", "注文履歴はありません。");
	        // 空リストを渡しておくとテンプレート内で null チェック不要
	        model.addAttribute("listOrderForm", List.of());
	    } else {
	        model.addAttribute("listOrderForm", listOrderForm);
	    }
		
		return "staffOrderHistory";
	}
	
	//注文履歴詳細画面表示
   @GetMapping("/staff/orders/{orderId:[0-9]+}")
    public String showOrderDetail(@PathVariable("orderId") int orderId, Model model) throws Exception {

        // DBから注文詳細情報を取得
    	List<OrderDetailsForm> orderDetailsList = staffOrderService.getOrderDetailsById(orderId);
    	
    	// DBからorderIdをキーに発送先情報を取得
    	ShippingForm shippingForm = staffOrderService.getShippingId(orderId);
    	
    	// DBからorderIdをキーに発送フラグを取得
    	String shippingFrag = staffOrderService.getShippingFrag(orderId);
    	
        model.addAttribute("orderDetailsList", orderDetailsList);
        model.addAttribute("shippingForm", shippingForm);
        model.addAttribute("shippingFrag", shippingFrag);
        
        if(orderDetailsList == null || orderDetailsList.isEmpty()) {
            throw new RuntimeException("注文詳細が取得できませんでした");
        }
        
        return "staffOrderDetails";
    }
   
   // 発送確認画面表示
   @GetMapping("/stafforder/staffShippingConfirm")
   public String showShippingConfirm(@RequestParam("orderId") int orderId, Model model) throws Exception{
       // 注文明細取得
       List<OrderDetailsForm> orderDetailsList = staffOrderService.getOrderDetailsById(orderId);

       // 発送先情報取得
       ShippingForm shippingForm = staffOrderService.getShippingId(orderId);
       
       model.addAttribute("orderDetailsList", orderDetailsList);
       model.addAttribute("shippingForm", shippingForm);
       model.addAttribute("orderId", orderId);
       
       return "staffShippingConfirm";
   }
	
    //注文履歴削除処理
    @PostMapping("/stafforder/cancel")
    public String OrderCancel(@RequestBody Map<String, Object> cancelOrderId) throws Exception {
    	
    	Object orderIdObj = cancelOrderId.get("orderId");
    	
    	if(orderIdObj == null) {
    		throw new IllegalArgumentException("orderIdがnullです");
    	}
    	
    	int orderId = Integer.parseInt(orderIdObj.toString());
    	staffOrderService.OrderCancel(orderId);
    	
    	return "redirect:/stafforder";
    }

   // 発送処理
   @PostMapping("/stafforder/shippingComplete")
   public String shippingComplete(@RequestParam("orderId") int orderId, Model model) throws Exception{
	   //ステータスを発送済みに変更する処理
	   staffOrderService.shippedOrder(orderId);
	   
	   return "shippingComplete";
   }
   
   // 発送キャンセル処理
   @PostMapping("/stafforder/cancelShipping")
   public String cancelShipping(@RequestParam("orderId") int orderId, Model model) throws Exception{
	   //ステータスを未発送に変更する処理
	   staffOrderService.cancelShippedOrder(orderId);
	   
	   return "shippingCancel";
   }

}
