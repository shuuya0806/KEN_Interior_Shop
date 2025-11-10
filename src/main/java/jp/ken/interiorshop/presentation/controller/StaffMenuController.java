package jp.ken.interiorshop.presentation.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;
import jp.ken.interiorshop.application.service.ItemService;
import jp.ken.interiorshop.application.service.StaffLoginService;
import jp.ken.interiorshop.common.validatior.groups.ValidGroupOrder;
import jp.ken.interiorshop.presentation.form.ItemForm;
import jp.ken.interiorshop.presentation.form.StaffLoginForm;

@Controller
public class StaffMenuController {
	StaffLoginService staffLoginService;
	ItemService itemService;
	
	public StaffMenuController(StaffLoginService staffLoginService, ItemService itemService){
		this.staffLoginService = staffLoginService;
		this.itemService = itemService;
	}
	
	//メニュー画面へ遷移
	@GetMapping(value = "/staffmenu")
	 public String showStaffMenu(Model model) {
		return "staffMenu";
	}
	
	// 注文管理メニュー画面表示
	@GetMapping("/staffordermenu")
	public String showStaffOrderMenu(Model model) {
		return "staffOrderMenu";
	}
	
	// 従業員情報管理メニュー画面表示
	@GetMapping("/empinfomenu")
	public String showEmoInfoMenu(Model model) {
		return "empInfoMenu";
	}
	
	//従業員情報登録画面表示
	@GetMapping("/staffregist")
	public String showStaffRegist(@ModelAttribute StaffLoginForm staffLoginForm, Model model, HttpSession session) {
		StaffLoginForm loginStaff = (StaffLoginForm) session.getAttribute("loginStaff");
	    model.addAttribute("loginStaff", loginStaff);
		
		if(staffLoginForm == null) {
			model.addAttribute("staffLoginForm", new StaffLoginForm());
		}
		model.addAttribute("staffLoginForm", staffLoginForm);
		
		return "staffRegist";
	}
	
	//従業員情報登録確認画面表示
	@PostMapping("/staffregistconfirm")
	public String showStaffRegistConfirm(@Validated(ValidGroupOrder.class) @ModelAttribute StaffLoginForm staffLoginForm, BindingResult bindingResult,  Model model) {
		model.addAttribute("staffLoginForm", staffLoginForm);
		
	    if (bindingResult.hasErrors()) {
	        return "staffRegist";
	    }

		return "staffRegistConfirm";
	}
	
	//従業員情報登録完了画面表示
	@PostMapping("/staffregistDB")
	public String showStaffRegistComplete(@ModelAttribute StaffLoginForm staffLoginForm, Model model) throws Exception{
		//従業員情報登録処理
		staffLoginService.staffRegist(staffLoginForm);
		
		return "staffRegistComplete";
	}
	
	//従業員情報閲覧画面表示
	@GetMapping("/staffview")
	public String showStaffView(Model model) throws Exception{
		//従業員情報を取得
		List<StaffLoginForm> staffLoginForm = staffLoginService.getStaffList();
		model.addAttribute("staffLoginForm", staffLoginForm);
		
		return "staffView";
	}
	
	//従業員情報変更画面表示
	@GetMapping("/staffedit")
	public String showStaffEdit(@ModelAttribute StaffLoginForm staffLoginForm, @RequestParam int staffId, Model model) throws Exception{
		//staffIdをキーに従業員情報を取得
		staffLoginForm = staffLoginService.getStaffListById(staffId);
		
		model.addAttribute("staffLoginForm", staffLoginForm);
		
		return "staffEdit";
	}
	
	//従業員情報変更確認画面表示
	@PostMapping("staffeditconfirm")
	public String showStaffEditConfirm(@Validated(ValidGroupOrder.class) @ModelAttribute StaffLoginForm staffLoginForm, BindingResult bindingResult, Model model) throws Exception{
		//従業員情報変更処理
		model.addAttribute("staffLoginForm", staffLoginForm);
		
		if(bindingResult.hasErrors()) {
			return "staffedit";
		}
		return "staffEditConfirm";
	}
	
	//従業員情報登録完了画面表示
	@PostMapping("staffeditDB")
	public String showStaffEditComplete(@ModelAttribute StaffLoginForm staffLoginForm, Model model) throws Exception{
		//従業員情報変更処理
		staffLoginService.staffEditRegist(staffLoginForm);
		
		return "staffEditComplete";
	}
	
	//従業員情報削除確認画面表示
	@PostMapping("staffdelete")
	public String showStaffDeleteConfirm(@RequestParam("staffId") int staffId, Model model) throws Exception{
		//staffIdをキーに従業員情報を取得
		StaffLoginForm staffLoginForm = staffLoginService.getStaffListById(staffId);
		model.addAttribute("staffLoginForm", staffLoginForm);
		
		return "staffDeleteConfirm";
	}
	
	//従業員情報削除完了画面表示
	@PostMapping("staffdeleteDB")
	public String showStaffDeleteComplete(@RequestParam("staffId") int staffId, Model model) throws Exception{
		//従業員情報削除処理
		//judgeは登録できたかできてないか判定の数字保持(仮置き)
		int judge = staffLoginService.staffDelete(staffId);
		
		return "staffDeleteComplete";
	}
	
	//在庫管理メニュー表示
	@GetMapping("staffitemstock")
	public String showStaffItemStock(Model model) throws Exception{
		return "staffItemStock";
	}
	
	//在庫一覧表示処理
	@GetMapping("staffitemstocklist")
	public String showStaffItemStockList(Model model) throws Exception{
		//商品一覧を取得
		List<ItemForm> itemStockList = itemService.getItemList();
		
		model.addAttribute("itemStockList", itemStockList);
		
		return "staffStockItemList";
	}
	
	/*//商品情報編集画面
	@GetMapping("staffitemview")
	public String showStaffItemView(Model model) throws Exception{
		//商品一覧を取得
		List<ItemForm> itemStockList = itemService.getItemList();
			
		model.addAttribute("itemStockList", itemStockList);
		
		return "staffItemView";
	   }*/
	
	//商品情報編集の編集画面
	@GetMapping("staffitemedit")
	public String staffItemEdit(@ModelAttribute("itemForm") ItemForm itemForm, @RequestParam("itemId") int itemId, Model model) throws Exception{
		if(itemForm.getExplanation() == null) {
			itemForm = itemService.getItemById(itemId);
			model.addAttribute("itemForm", itemForm);
		}
	
		model.addAttribute("itemForm", itemForm);
		return "staffItemEdit";
	}
	
	//商品情報編集確認画面
	@PostMapping("staffitemeditconfirm")
	public String staffItemEditConfirm(@ModelAttribute("itemForm") ItemForm itemForm, Model model) throws Exception{
		model.addAttribute("itemForm", itemForm);
		
		return "staffItemEditConfirm";
	}
	
	//商品情報編集登録完了画面
	@PostMapping("staffitemeditDB")
	public String staffItemEditDB(@ModelAttribute("itemForm") ItemForm itemForm, Model model) throws Exception{
		//商品情報変更処理
		itemService.editItem(itemForm);
		
		return "staffItemEditComplete";
	}
}
