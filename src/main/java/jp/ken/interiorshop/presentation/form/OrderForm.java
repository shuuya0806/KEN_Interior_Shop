package jp.ken.interiorshop.presentation.form;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import jakarta.validation.Valid;
import lombok.Data;

@Data
public class OrderForm implements Serializable {

	//登録住所or別住所(初期値は登録住所)
	private String addressOption = "member";
	
	//注文ID
	private String orderId;
	
	//顧客ID
	private String memberId;
	
	//合計金額
	private Integer total;
	
	//注文日
	private String orderDate;
	
	//支払方法
	private String payment = "現金";
	
	//配送指定
	private String shipping = "Standard";
	
	//発送先ID
	private String shippingId;
	
	//発送フラグ
	private String shippingFrag;
	
	//プルダウンで表示用(月)
	private List<String> month;
	
	//プルダウンで表示用(日)
	private List<String> day;
	
	// 複数商品を保持するリスト
	@Valid 
    private List<OrderDetailsForm> orderDetailsForm = new ArrayList<>();
	
	//発送のネストForm
	@Valid
	private ShippingForm shippingForm = new ShippingForm();
}