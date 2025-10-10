package jp.ken.interiorshop.application.service;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import jp.ken.interiorshop.domain.entity.MemberEntity;
import jp.ken.interiorshop.domain.entity.OrderDetailsEntity;
import jp.ken.interiorshop.domain.entity.OrderEntity;
import jp.ken.interiorshop.domain.entity.ShippingEntity;
import jp.ken.interiorshop.domain.repository.MemberRepository;
import jp.ken.interiorshop.domain.repository.StaffOrderRepository;
import jp.ken.interiorshop.presentation.form.OrderDetailsForm;
import jp.ken.interiorshop.presentation.form.OrderForm;
import jp.ken.interiorshop.presentation.form.ShippingForm;

@Service
public class StaffOrderService {
	
	private StaffOrderRepository staffOrderRepository;
	private MemberRepository memberRepository;
	private ModelMapper FormMapper;
	
	public StaffOrderService(StaffOrderRepository staffOrderRepository,
			MemberRepository memberRepository,
			ModelMapper FormMapper) {
		this.staffOrderRepository = staffOrderRepository;
		this.memberRepository = memberRepository;
		this.FormMapper = FormMapper;

	}
	
	//注文履歴を取得
	public List<OrderForm> getOrderList () throws Exception{
		
		List<OrderEntity> entityList = null;
		List<OrderForm> formList = null;
		
		//DB内のorders情報を全て取得
		entityList = staffOrderRepository.getOrderAllList();
		
		//取得したデータをList<OrderForm>型に変換
		formList = convert(entityList);
		
		//変換したデータに会員名を追加する
		for (OrderForm form : formList) {
			form.setMemberName(getMemberName(form.getMemberId()));
		}
		
		return formList;
	}
	
	//注文履歴内の会員IDから会員名を取得
	public String getMemberName (int memberId) throws Exception {
				
		//引数の会員IDから会員情報を取得
		MemberEntity entity = memberRepository.findByMemberId(memberId);
		
        if (entity == null) {
        	// 認証失敗時は null を返す
        	return null; 
        }

        return entity.getMemberName();
		
	}
	
	//注文詳細情報を取得
	public List<OrderDetailsForm> getOrderDetailsById(int orderId) throws Exception{
		List<OrderDetailsEntity> orderDetailsEntity = null;
		
		orderDetailsEntity = staffOrderRepository.getOrderDetailsById(orderId);
		
		List<OrderDetailsForm> orderDetailsForm = convertOrderDetails(orderDetailsEntity);
		
		return orderDetailsForm;
	}
	
	//orderIdをキーに発送先情報を取得
	public ShippingForm getShippingId(int orderId) throws Exception{
		ShippingEntity shippingEntity = staffOrderRepository.getShippingId(orderId);
		
		ShippingForm shippingForm = convertShipping(shippingEntity);
		
		return shippingForm;
		
	}
	
	//orderIdをキーに発送フラグを取得
	public String getShippingFrag(int orderId) throws Exception{
		String shippingFrag = staffOrderRepository.getShippingFrag(orderId);
		
		
		return shippingFrag;
	}
	
	//ステータスを発送済みに変更する処理
	public void shippedOrder(int orderId) throws Exception{
		staffOrderRepository.shippedOrder(orderId);
	}
	
	//ステータスを未発送に変更する処理
	public void cancelShippedOrder(int orderId) throws Exception{
		staffOrderRepository.cancelShippedOrder(orderId);
	}
	
	private List<OrderForm> convert(List<OrderEntity> entityList) {
		
		List<OrderForm> formList = new ArrayList<OrderForm>();
		
		for(OrderEntity entity : entityList) {
			OrderForm form = FormMapper.map(entity, OrderForm.class);
			formList.add(form);
		}
		
		return formList;
	}
	
	private List<OrderDetailsForm> convertOrderDetails(List<OrderDetailsEntity> orderDetailsEntity) {
		List<OrderDetailsForm> formList = new ArrayList<OrderDetailsForm>();
		
		for(OrderDetailsEntity entity : orderDetailsEntity) {
			OrderDetailsForm form = FormMapper.map(entity, OrderDetailsForm.class);
			formList.add(form);
		}
		
		return formList;
	}
	
	private ShippingForm convertShipping(ShippingEntity shippingEntity) {
		ShippingForm shippingForm = FormMapper.map(shippingEntity, ShippingForm.class);
		
		return shippingForm;
	}

}
