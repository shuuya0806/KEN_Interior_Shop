package jp.ken.interiorshop.presentation.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import jp.ken.interiorshop.application.service.StaffStatisticsService;
import jp.ken.interiorshop.presentation.form.ItemSalesForm;
import jp.ken.interiorshop.presentation.form.SalesForm;

@Controller
public class StaffStatisticsController {
	StaffStatisticsService staffStatisticsService;
	
	public StaffStatisticsController(StaffStatisticsService staffStatisticsService){
		this.staffStatisticsService = staffStatisticsService;
	}
	
	// 集計管理メニュー画面表示
	@GetMapping("/staffstatistics")
	public String showStaffStatisticsMenu(Model model) {
		return "staffStatisticsMenu";
	}
	
	// 売上一覧を表示
	@GetMapping("/Sales")
	public String showSales(Model model) throws  Exception{
		
		// DBから売上を取得
		List<SalesForm>  listSalesForm = staffStatisticsService.getSalesList();
		
		// DBから商品売上を取得
		List<ItemSalesForm> listItemSalesForm = staffStatisticsService.getItemSalesList();
		
		//売上のnullチェック
		if(listSalesForm == null || listSalesForm.isEmpty()) {
			
			model.addAttribute("message", "商品別売上はありません。");
			// 空リストを渡しておくとテンプレート内で null チェック不要
	        model.addAttribute("listSalesForm", List.of());
		}else {
			model.addAttribute("listSalesForm", listSalesForm);
		}
		
		//商品別売上のnullチェック
		if(listItemSalesForm == null || listItemSalesForm.isEmpty()) {
			
			model.addAttribute("message", "商品別売上はありません。");
			// 空リストを渡しておくとテンプレート内で null チェック不要
	        model.addAttribute("listItemSalesForm", List.of());
		}else {
			model.addAttribute("listItemSalesForm", listItemSalesForm);
		}
		
		return "Sales";
	}
	
}
