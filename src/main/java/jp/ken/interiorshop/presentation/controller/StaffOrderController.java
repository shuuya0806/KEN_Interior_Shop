package jp.ken.interiorshop.presentation.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.multipart.MultipartFile;

import jp.ken.interiorshop.application.service.StaffOrderService;
import jp.ken.interiorshop.common.validatior.groups.ValidGroupOrder;
import jp.ken.interiorshop.presentation.form.ItemForm;
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
    public String showOrderDetail(@PathVariable("orderId") int orderId, @SessionAttribute("loginStaff") StaffLoginForm staffLoginForm, Model model) throws Exception {

        // DBから注文詳細情報を取得
    	List<OrderDetailsForm> orderDetailsList = staffOrderService.getOrderDetailsById(orderId);
    	
    	// DBからorderIdをキーに発送先情報を取得
    	ShippingForm shippingForm = staffOrderService.getShippingId(orderId);
    	
    	// DBからorderIdをキーに発送フラグを取得
    	String shippingFrag = staffOrderService.getShippingFrag(orderId);
    	
        model.addAttribute("orderDetailsList", orderDetailsList);
        model.addAttribute("shippingForm", shippingForm);
        model.addAttribute("shippingFrag", shippingFrag);
        model.addAttribute("loginStaff", staffLoginForm);
        
        if(orderDetailsList == null || orderDetailsList.isEmpty()) {
            throw new RuntimeException("注文詳細が取得できませんでした");
        }
        
        return "staffOrderDetails";
    }
   
   // 発送確認画面表示
   @GetMapping("/stafforder/staffShippingConfirm")
   public String showShippingConfirm(@RequestParam("orderId") int orderId, @SessionAttribute("loginStaff") StaffLoginForm staffLoginForm, Model model) throws Exception{
       // 注文明細取得
       List<OrderDetailsForm> orderDetailsList = staffOrderService.getOrderDetailsById(orderId);

       // 発送先情報取得
       ShippingForm shippingForm = staffOrderService.getShippingId(orderId);
       
       model.addAttribute("orderDetailsList", orderDetailsList);
       model.addAttribute("shippingForm", shippingForm);
       model.addAttribute("orderId", orderId);
       model.addAttribute("loginStaff", staffLoginForm);
       
       return "staffShippingConfirm";
   }
	
    //注文履歴削除処理
    @PostMapping("/stafforder/cancel")
    @ResponseBody //返り値がView名ではなくHTTPレスポンスになる
    public String OrderCancel(@RequestBody Map<String, Object> cancelOrderId) throws Exception {
    	
    	Object orderIdObj = cancelOrderId.get("orderId");
    	
    	if(orderIdObj == null) {
    		throw new IllegalArgumentException("orderIdがnullです");
    	}
    	
    	int orderId = Integer.parseInt(orderIdObj.toString());
    	staffOrderService.OrderCancel(orderId);
    	
    	return "OK";
    }
   
    //発送、キャンセル処理
    @PostMapping("/staffbatchAction")
    public String staffBatchAction(
            @RequestParam(name = "selectedOrders", required = false) List<Integer> selectedOrders,
            @SessionAttribute("loginStaff") StaffLoginForm staffLoginForm,
            @RequestParam("action") String action,
            Model model) throws Exception {

        model.addAttribute("loginStaff", staffLoginForm);
        
        //チェックボックスが選択されていない場合
        if (selectedOrders == null || selectedOrders.isEmpty()) {
            model.addAttribute("message", "注文を選択してください");
            List<OrderForm> listOrderForm = staffOrderService.getOrderList();
            model.addAttribute("listOrderForm", listOrderForm);
            return "staffOrderHistory";
        }
        
        //成功リストと失敗リスト
        List<Integer> successList = new ArrayList<>();
        List<Integer> failList = new ArrayList<>();

        //失敗リスト作成（ルールに合わない注文）
        for (Integer orderId : selectedOrders) {
            String shippingFrag = staffOrderService.getShippingFrag(orderId);

            if ("ship".equals(action) && !"未発送".equals(shippingFrag)) {
                failList.add(orderId);
            } else if ("cancel".equals(action) && !"発送済み".equals(shippingFrag)) {
                failList.add(orderId);
            }
        }

        //失敗リストがあればメッセージを表示
        if (!failList.isEmpty()) {
            model.addAttribute("message", "不正な操作です: ");
            List<OrderForm> listOrderForm = staffOrderService.getOrderList();
            model.addAttribute("listOrderForm", listOrderForm);
            return "staffOrderHistory";
        }

        // 成功リストがあれば発送処理を実施
        for (Integer orderId : selectedOrders) {
            if ("ship".equals(action)) {
                staffOrderService.shippedOrder(orderId);
                successList.add(orderId);
            } else if ("cancel".equals(action)) {
                staffOrderService.cancelShippedOrder(orderId);
                successList.add(orderId);
            }
        }

        // ボタンにより画面遷移を分岐
        if ("ship".equals(action)) {
            return "shippingComplete";
        } else if ("cancel".equals(action)) {
            return "shippingCancel";
        }

        return "staffOrderHistory";
    }

   // 在庫変更処理
   @PostMapping("/staffstockupdate")
   public String updateStock(@RequestParam("itemId") List<String> itemIds, @RequestParam("stock") List<String> stocks) throws Exception{
	   for (int i = 0; i < itemIds.size(); i++) {
		 String itemId = itemIds.get(i);
	     String newStock = stocks.get(i);
		 //在庫更新処理
         staffOrderService.updateStock(itemId, newStock);
	   }
           


       return "redirect:/staffitemstocklist";
   }
   
   //新規商品登録画面へ遷移
   @GetMapping("/staffitemregist")
   public String showStaffItemRegist(Model model) throws Exception{
	   model.addAttribute("itemForm", new ItemForm());
	   
	   return "staffItemRegist";
   }

   @PostMapping("/staffitemregistconfirm")
   public String showStaffItemRegistConfirm(
           @Validated(ValidGroupOrder.class) ItemForm itemForm,
           BindingResult bindingResult,
           Model model) throws Exception {

       // 画像がすでにある場合（再入力時）
       if (itemForm.getImageBytes() != null && itemForm.getImageBytes().length > 0) {
           String base64Image = Base64.getEncoder().encodeToString(itemForm.getImageBytes());
           model.addAttribute("imageBase64", base64Image);
       }

       // 新規アップロードがある場合
       MultipartFile imageFile = itemForm.getImageFile();
       if (imageFile != null && !imageFile.isEmpty()) {
           try {
               byte[] imageBytes = imageFile.getBytes();
               itemForm.setImageBytes(imageBytes);
               itemForm.setOriginalFilename(imageFile.getOriginalFilename());

               String base64Image = Base64.getEncoder().encodeToString(imageBytes);
               model.addAttribute("imageBase64", base64Image);

           } catch (IOException e) {
               e.printStackTrace();
               model.addAttribute("imageError", "画像の読み込みに失敗しました");
               return "staffItemRegist";
           }
       }

       // 画像ファイル必須チェック
       if ((itemForm.getImageBytes() == null || itemForm.getImageBytes().length == 0)
               && (imageFile == null || imageFile.isEmpty())) {
           model.addAttribute("imageError", "画像ファイルは必須です");
           return "staffItemRegist"; // 登録画面に戻る
       }

       // バリデーションエラーがあれば入力画面に戻す
       if (bindingResult.hasErrors()) {
           return "staffItemRegist";
       }

       model.addAttribute("itemForm", itemForm);
       return "staffItemRegistConfirm";
   }

}
