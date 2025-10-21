package jp.ken.interiorshop.presentation.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

import jakarta.servlet.http.HttpSession;
import jp.ken.interiorshop.application.service.ItemService;
import jp.ken.interiorshop.application.service.OrderRegistService;
import jp.ken.interiorshop.common.validatior.groups.ValidGroupOrder;
import jp.ken.interiorshop.presentation.form.ItemForm;
import jp.ken.interiorshop.presentation.form.MemberLoginForm;
import jp.ken.interiorshop.presentation.form.MemberRegistForm;
import jp.ken.interiorshop.presentation.form.OrderDetailsForm;
import jp.ken.interiorshop.presentation.form.OrderForm;
import jp.ken.interiorshop.presentation.form.ShippingForm;

@Controller
public class OrderController {

	
	private OrderRegistService orderRegistService;
	private ItemService itemService;
	
	public OrderController(OrderRegistService orderRegistService, ItemService itemService) {
		this.orderRegistService = orderRegistService;
		this.itemService = itemService;
	}

	/**
	 * 注文確認画面の表示
	 */
	@PostMapping("/order/confirm")
    public String confirmOrder (Model model, HttpSession session, 
                                  @SessionAttribute("loginUser") MemberLoginForm loginUser) throws Exception {
		
        OrderForm orderForm = new OrderForm();
        MemberRegistForm memberRegistForm = new MemberRegistForm();
        ShippingForm shippingForm = new ShippingForm();
        
        // ログインユーザーのIDを取得
        int id = loginUser.getMemberId();
        
        // 会員情報取得
        memberRegistForm = orderRegistService.getMemberList(id);
        
        // 会員情報をshippingFormに格納
        shippingForm.setShippingName(memberRegistForm.getMemberName());
        shippingForm.setShippingKana(memberRegistForm.getMemberKana());
        shippingForm.setShippingphone(memberRegistForm.getPhoneNumber());
        shippingForm.setShippingPostalCode(memberRegistForm.getPostalCode());
        shippingForm.setShippingAddress1(memberRegistForm.getAddress1());
        shippingForm.setShippingAddress2(memberRegistForm.getAddress2());
        shippingForm.setShippingAddress3(memberRegistForm.getAddress3());
        orderForm.setShippingForm(shippingForm);
        
        // セッションのカート情報を OrderDetailsForm リストに変換して格納
        List<ItemForm> cartItems = (List<ItemForm>) session.getAttribute("cart");
        List<OrderDetailsForm> detailsList = new ArrayList<>();
        int totalQuantity = 0;
        
        //カートが空でなければ商品詳細のデータをFormへ格納
        if (cartItems != null) {
            for (ItemForm item : cartItems) {
                OrderDetailsForm details = new OrderDetailsForm();
                details.setItemId(item.getItemId());
                details.setItemQuantity(String.valueOf(item.getItemQuantity()));
                details.setSubtotal(String.valueOf(Integer.parseInt(item.getItemPrice()) * item.getItemQuantity()));
                details.setItemName(item.getItemName());
                detailsList.add(details);
                
                totalQuantity += item.getItemQuantity();
            }

            orderForm.setOrderDetailsForm(detailsList);
        }

        //セッションから税抜き金額を取得
        int totalExclTax = (int) session.getAttribute("totalExclTax");
        
        //税込金額を計算
        int totalInclTax = itemService.totalInclTax(totalExclTax);
        
        //消費税を計算
        int totalTax = itemService.totalTax(totalExclTax);
        
        //獲得ポイントを計算
        int getPoint = orderRegistService.getPoint(totalInclTax);
	    
	    //金額やカート情報をモデルに格納
	    model.addAttribute("totalExclTax", totalExclTax);
	    model.addAttribute("totalTax", totalTax);
	    model.addAttribute("totalInclTax", totalInclTax);
	    model.addAttribute("cartItemNames", cartItems);
	    model.addAttribute("orderForm", orderForm);
	    model.addAttribute("totalQuantity", totalQuantity);
	    model.addAttribute("futureDate", LocalDate.now().plusDays(2));
	    model.addAttribute("getPoint", getPoint);
	    model.addAttribute("memberRegistForm", memberRegistForm);

	    // 注文確認画面へ遷移
	    return "ordercheck";
	}
	
	/**
	 * 注文完了画面の表示
	 */
	@PostMapping(value="/ordercomp")
	public String completeOrder( @Validated(ValidGroupOrder.class) @ModelAttribute OrderForm orderForm,
	        					 BindingResult result,
	        					 @SessionAttribute("loginUser") MemberLoginForm memberLoginForm,
	        					 HttpSession session,
	        					 Model model) throws Exception  {
		
		//未入力チェック
		if(result.hasErrors()) {
	        
	        // セッションのカート情報を OrderDetailsForm リストに変換して格納
	        List<ItemForm> cartItems = (List<ItemForm>) session.getAttribute("cart");
	        List<OrderDetailsForm> detailsList = new ArrayList<>();
	        int totalQuantity = 0;
	        
	        //カートが空でなければ商品詳細のデータをFormへ格納
	        if (cartItems != null) {
	            for (ItemForm item : cartItems) {
	                OrderDetailsForm details = new OrderDetailsForm();
	                details.setItemId(item.getItemId());
	                details.setItemQuantity(String.valueOf(item.getItemQuantity()));
	                details.setSubtotal(String.valueOf(Integer.parseInt(item.getItemPrice()) * item.getItemQuantity()));
	                details.setItemName(item.getItemName());
	                detailsList.add(details);
	                
	                totalQuantity += item.getItemQuantity();
	            }

	            orderForm.setOrderDetailsForm(detailsList);
	        }

	        //セッションから税抜き金額を取得
	        int totalExclTax = (int) session.getAttribute("totalExclTax");
	        
	        //税込金額を計算
	        int totalInclTax = itemService.totalInclTax(totalExclTax);
	        
	        //消費税を計算
	        int totalTax = itemService.totalTax(totalExclTax);
	        
	        //獲得ポイントを計算
	        int getPoint = orderRegistService.getPoint(totalInclTax);
		    
		    //金額やカート情報をモデルに格納
		    model.addAttribute("totalExclTax", totalExclTax);
		    model.addAttribute("totalTax", totalTax);
		    model.addAttribute("totalInclTax", totalInclTax);
		    model.addAttribute("cartItemNames", cartItems);
		    model.addAttribute("orderForm", orderForm);
		    model.addAttribute("totalQuantity", totalQuantity);
		    model.addAttribute("futureDate", LocalDate.now().plusDays(2));
		    model.addAttribute("getPoint", getPoint);

		    // 注文確認画面へ遷移
		    return "ordercheck"; 
		}
		
		// カートの合計を OrderForm に格納
		Integer totalExclTax = (Integer) session.getAttribute("totalExclTax");
		orderForm.setTotal(totalExclTax);
		
		// サービスで注文情報を登録（発送、注文、注文詳細の3テーブル）
		orderRegistService.orderRegist(memberLoginForm, orderForm, orderForm.getShippingForm());
		
        // 会員情報取得
        MemberRegistForm memberRegistForm = orderRegistService.getMemberList(memberLoginForm.getMemberId());
		
		//購入後のポイントを会員情報に入れる
		orderRegistService.pointUpdate(memberRegistForm.getPoint(), orderForm.getUsePoint(),
									   orderRegistService.getPoint(totalExclTax),
									   memberLoginForm.getMemberId());
		
		// カートのセッションを破棄
	    session.removeAttribute("cart");
	    session.removeAttribute("totalExclTax");
	    session.removeAttribute("totalTax");
	    session.removeAttribute("totalInclTax");

	    // 注文完了画面へ遷移
		 return "orderComplete";
	}
}
