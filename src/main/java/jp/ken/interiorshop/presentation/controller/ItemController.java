package jp.ken.interiorshop.presentation.controller;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpSession;
import jp.ken.interiorshop.application.service.ItemService;
import jp.ken.interiorshop.domain.entity.ItemEntity;
import jp.ken.interiorshop.presentation.form.CategoryForm;
import jp.ken.interiorshop.presentation.form.ItemForm;
import jp.ken.interiorshop.presentation.form.MemberLoginForm;

@Controller
public class ItemController {

	private final ItemService itemService;

	public ItemController(ItemService itemService) {
		this.itemService = itemService;
	}

	/**
	 * 商品一覧表示
	 */
	@GetMapping("/item")
	public String showItem(Model model, HttpSession session) throws Exception {
		
		// DBから商品一覧を取得
		List<ItemForm> formItemList = itemService.getItemList(); 
		
		 // DBからカテゴリ一覧を取得
		List<CategoryForm> categoryFormList = itemService.getCategoryList();
		
		// 発売日の商品のみを取得
		List<ItemForm> releasedItemList = itemService.getReleasedItemList(formItemList);
		
		// 商品情報(発売日の商品のみ)とカテゴリー情報をモデルに格納
		model.addAttribute("itemForm", releasedItemList);
		model.addAttribute("categoryForm", categoryFormList);
		model.addAttribute("itemNewForm", new ItemForm());
		
		// 「戻る」ボタン用のURLをセッションに格納
		session.setAttribute("currentUrl", "/item");

		// 商品一覧画面へ遷移
		return "item"; 
	}
	
	/**
	 * 次月発売予定商品一覧表示
	 */
	@GetMapping("/nextmonthitem")
	public String showNextMonthItem(Model model, HttpSession session) throws Exception{
		//DBから次月発売商品を取得
		List<ItemForm> nextMonthItemList = itemService.getNextMonthItem();
		
		model.addAttribute("itemForm", nextMonthItemList);
		
		// 「戻る」ボタン用のURLをセッションに格納
		session.setAttribute("currentUrl", "/item");
		
		return "nextMonthItem";
	}

	/**
	 * カート画面表示
	 */
	@GetMapping("/cart")
	public String showCart(@ModelAttribute("loginUser") MemberLoginForm memberLoginForm,
	                       Model model, HttpSession session) {

		// 名前の有無でログイン判定
		if (memberLoginForm.getMemberName() != null) {

			// 「戻る」ボタン用URLをセッションに格納
			session.setAttribute("currentUrl", "/cart");

			// sessionからカート情報を取得しモデルに格納
			List<ItemForm> cartItems = itemService.getCart(session);
			model.addAttribute("cartItems", cartItems);

			// 税抜き金額・消費税・税込金額を計算
			int totalExclTax = itemService.totalExclTax(cartItems);
			int totalTax = itemService.totalTax(totalExclTax);
			int totalInclTax = itemService.totalInclTax(totalExclTax);

			// 金額情報をセッションとモデルに格納
			session.setAttribute("totalExclTax", totalExclTax);
			model.addAttribute("totalExclTax", totalExclTax);
			model.addAttribute("totalTax", totalTax);
			model.addAttribute("totalInclTax", totalInclTax);

			// カート画面へ遷移
			return "cart"; 
		} else {
			
			// 未ログイン時はログイン画面へリダイレクト
			return "redirect:/login";
		}
	}

	/**
	 * カートに追加ボタン押下
	  */
	@PostMapping("/cart/add")
	public String addToCart(@ModelAttribute ItemForm item,
	                        @RequestParam String redirectUrl,
	                        HttpSession session,
	                        RedirectAttributes redirectAttributes) {
		
		// カートに商品を追加
		itemService.addToCart(session, item);

		// メッセージをセッションに格納（画面表示用）
		session.setAttribute("message", item.getItemName() + " を" + item.getItemQuantity() + "個カートに追加しました");

		// 元の画面にリダイレクト
		return "redirect:" + redirectUrl;
	}

	/**
	 * カート全削除ボタン押下
	  */
	@PostMapping("/cart/clear")
	public String clearCart(HttpSession session) {
		
		// カート情報をセッションから削除
		itemService.clearCart(session); 
		
		// カート画面へリダイレクト
		return "redirect:/cart";
	}

	/**
	 * カートの数量変更・削除
	  */
	@PostMapping("/cart/updateCart")
	public String updateCart(@RequestParam("action") String action,
	                         @RequestParam("itemId") List<String> itemIds,
	                         @RequestParam("itemQuantity") List<Integer> itemQuantities,
	                         @RequestParam(name = "removeItemId", required = false) List<String> removeItemIds,
	                         HttpSession session) {

		//数量変更か削除ボタンのどちらが押下されたかを判定
		if ("update".equals(action)) {
			
			// 数量一括変更
			if (itemIds != null && itemQuantities != null) {
				itemService.updateQuantity(session, itemIds, itemQuantities);
			}
		} else if ("delete".equals(action)) {
			
			// 選択された商品を削除
			if (removeItemIds != null) {
				itemService.removeCart(session, removeItemIds);
			}
		}

		// カート画面へリダイレクト
		return "redirect:/cart";
	}

	/**
	 * 商品検索画面表示（キーワード・カテゴリ・価格範囲による絞り込み）
	  */
	@GetMapping("/search")
	public String searchItems(
	        @RequestParam(name = "keyword", required = false) String keyword,
	        @RequestParam(name = "categoryId", required = false) Integer categoryId,
	        @RequestParam(name = "minPrice", required = false) Integer minPrice,
	        @RequestParam(name = "maxPrice", required = false) Integer maxPrice,
	        Model model, HttpSession session) throws Exception {

	    // DBからカテゴリ一覧を取得しモデルに格納
	    List<CategoryForm> categoryFormList = itemService.getCategoryList();
	    model.addAttribute("categoryForm", categoryFormList);

	    // 入力値をモデルに格納（再表示用）
	    model.addAttribute("keyword", keyword);
	    model.addAttribute("categoryId", categoryId);
	    model.addAttribute("minPrice", minPrice);
	    model.addAttribute("maxPrice", maxPrice);

	    
	    List<ItemForm> itemFormList;
	    List<ItemForm> releasedItemList;

	    // 金額の妥当性チェック（負の値が含まれている場合はエラー）
	    if ((minPrice != null && minPrice < 0) || (maxPrice != null && maxPrice < 0)) {
	        model.addAttribute("errorMessage", "金額には0以上の値を入力してください");

	        // 条件が不正な場合はDBから全件取得
	        itemFormList = itemService.getItemList();
	        
	        // 条件が不正な場合はDBから全件取得した物から発売日の商品のみを取得
		 	releasedItemList = itemService.getReleasedItemList(itemFormList);
	    }
	    // 価格整合性チェック（最低価格 > 最高価格 の場合はエラー）
	    else if (minPrice != null && maxPrice != null && maxPrice < minPrice) {
	        model.addAttribute("errorMessage", "最高金額は最低金額以上に設定してください");

	        // 条件が不正な場合はDBから全件取得
	        itemFormList = itemService.getItemList();
	        
	        // 条件が不正な場合はDBから全件取得した物から発売日の商品のみを取得
		 	 releasedItemList = itemService.getReleasedItemList(itemFormList);
	    } else {
	        // 商品検索（条件が1つでもあれば絞り込み）
	        List<ItemEntity> itemEntityList = itemService.searchItem(keyword, categoryId, minPrice, maxPrice);
	        itemFormList = itemService.convertItemForm(itemEntityList);
	        
	        // 商品検索（条件が1つでもあれば絞り込み）から発売日の商品のみを取得
		 	 releasedItemList = itemService.getReleasedItemList(itemFormList);

	        // 検索結果が0件ならメッセージをモデルに格納
	        if (releasedItemList.isEmpty()) {
	            model.addAttribute("infoMessage", "該当商品はありません");
	        }
	    }

	    // 検索結果をモデルに格納
	    model.addAttribute("itemForm", releasedItemList);

	    // 「戻る」用URLを検索条件で判定（検索条件がある場合のみ上書き）
	    StringBuilder backUrl = new StringBuilder("/search?");
	    if (keyword != null && !keyword.trim().isEmpty()) {
	        backUrl.append("keyword=").append(URLEncoder.encode(keyword, StandardCharsets.UTF_8)).append("&");
	    }
	    if (categoryId != null) {
	        backUrl.append("categoryId=").append(categoryId).append("&");
	    }
	    if (minPrice != null) {
	        backUrl.append("minPrice=").append(minPrice).append("&");
	    }
	    if (maxPrice != null) {
	        backUrl.append("maxPrice=").append(maxPrice).append("&");
	    }
	    if (backUrl.toString().endsWith("&")) {
	        backUrl.setLength(backUrl.length() - 1);
	    }
	    
	    //検索条件で判定した「戻る」用URLをセッションに格納
	    session.setAttribute("currentUrl", backUrl.toString());

	    // カート追加メッセージがあれば表示してセッションから削除
	    Object message = session.getAttribute("message");
	    if (message != null) {
	        model.addAttribute("message", message);
	        session.removeAttribute("message");
	    }

	    // 検索結果画面へ遷移
	    return "search"; 
	}

	/**
	 * 商品詳細画面表示（検索条件を保持して戻れるようにする）
	  */
	@GetMapping("/item/detail/{itemId}")
	public String showItemDetail(
	    @PathVariable("itemId") int itemId,
	    @RequestParam(name = "from", required = false, defaultValue = "item") String from,
	    @RequestParam(name = "keyword", required = false) String keyword,
	    @RequestParam(name = "categoryId", required = false) Integer categoryId,
	    @RequestParam(name = "minPrice", required = false) Integer minPrice,
	    @RequestParam(name = "maxPrice", required = false) Integer maxPrice,
	    Model model, HttpSession session) throws Exception {

		// 「戻る」用URLを検索条件で判定（検索条件がある場合のみ上書き）
	    if (session.getAttribute("currentUrl") == null) {
	    	
	    	// デフォルトは商品一覧ページ
	        String backUrl = "/item"; 

	        if (keyword != null || categoryId != null || minPrice != null || maxPrice != null) {
	            StringBuilder sb = new StringBuilder("/search?");
	            if (keyword != null && !keyword.trim().isEmpty()) {
	                sb.append("keyword=").append(URLEncoder.encode(keyword, StandardCharsets.UTF_8)).append("&");
	            }
	            if (categoryId != null) {
	                sb.append("categoryId=").append(categoryId).append("&");
	            }
	            if (minPrice != null) {
	                sb.append("minPrice=").append(minPrice).append("&");
	            }
	            if (maxPrice != null) {
	                sb.append("maxPrice=").append(maxPrice).append("&");
	            }
	            if (sb.toString().endsWith("&")) {
	                sb.setLength(sb.length() - 1);
	            }
	            backUrl = sb.toString();
	        }
	        
	        //検索条件で判定した「戻る」用URLをセッションに格納
	        session.setAttribute("currentUrl", backUrl);
	    }

	    // DBから商品情報の取得と税込価格の計算
	    ItemForm item = itemService.getItemById(itemId);
	    int price = Integer.parseInt(item.getItemPrice());
	    int taxIncludedPrice;
	    if (item.getSaleFrag() == "未実施") {
	    	taxIncludedPrice = (int) Math.floor(price * 1.1); // 消費税10%加算
	    } else {
	    	int salePrice = Integer.parseInt(item.getSalePrice());
	    	taxIncludedPrice = (int) Math.floor(salePrice * 1.1); //消費税10%加算
	    }

	    // 商品情報と税込価格の計算結果をモデルに格納
	    model.addAttribute("item", item);
	    model.addAttribute("taxIncludedPrice", taxIncludedPrice);

	    // 検索条件もモデルに格納（hiddenなどで再利用可能）
	    model.addAttribute("from", from);
	    model.addAttribute("keyword", keyword);
	    model.addAttribute("categoryId", categoryId);
	    model.addAttribute("minPrice", minPrice);
	    model.addAttribute("maxPrice", maxPrice);

	    // カート追加メッセージがあればモデルに格納してセッションから削除
	    Object message = session.getAttribute("message");
	    if (message != null) {
	        model.addAttribute("message", message);
	        session.removeAttribute("message");
	    }
	    
	    if("nextMonth".equals(from)) {
	    	session.setAttribute("currentUrl", "/nextmonthitem");
	    }
	    
	    // 商品詳細画面へ遷移
	    return "itemDetails"; 
	}

	/**
	 * 戻る処理を統一（セッションに保存されたURLへリダイレクト）
	  */
	@GetMapping("/back1")
	public String back(HttpSession session) {
		
		//「戻る」用URLを取得
	    Object backUrl = session.getAttribute("currentUrl");
	    
	    //取得したURLへリダイレクト
	    return "redirect:" + backUrl;
	}
}