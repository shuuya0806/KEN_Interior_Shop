package jp.ken.interiorshop.application.service;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jp.ken.interiorshop.domain.entity.MemberEntity;
import jp.ken.interiorshop.domain.entity.OrderDetailsEntity;
import jp.ken.interiorshop.domain.entity.OrderEntity;
import jp.ken.interiorshop.domain.entity.ShippingEntity;
import jp.ken.interiorshop.domain.repository.MemberRepository;
import jp.ken.interiorshop.domain.repository.OrderRegistRepository;
import jp.ken.interiorshop.presentation.form.MemberLoginForm;
import jp.ken.interiorshop.presentation.form.MemberRegistForm;
import jp.ken.interiorshop.presentation.form.OrderDetailsForm;
import jp.ken.interiorshop.presentation.form.OrderForm;
import jp.ken.interiorshop.presentation.form.ShippingForm;

@Service
public class OrderRegistService {

	private OrderRegistRepository orderRegistRepository;
	private MemberRepository memberRepository;
	private ModelMapper modelMapper;
	
	public OrderRegistService(OrderRegistRepository orderRegistRepository, MemberRepository memberRepository, ModelMapper modelMapper) {
		this.orderRegistRepository = orderRegistRepository;
		this.memberRepository = memberRepository;
		this.modelMapper = modelMapper;
	}
	
	@Transactional(rollbackFor = Exception.class)
	public void orderRegist(MemberLoginForm memberLoginForm, OrderForm orderForm, ShippingForm shippingForm) throws Exception{
		
		// nullチェック
	    if (memberLoginForm == null) {
	        throw new IllegalArgumentException("ログインユーザー情報が空です");
	    }
	    if (orderForm == null) {
	        throw new IllegalArgumentException("注文情報が空です");
	    }
	    if (orderForm.getOrderDetailsForm() == null || orderForm.getOrderDetailsForm().isEmpty()) {
	        throw new IllegalArgumentException("注文詳細が空です");
	    }
	    
		ShippingEntity shippingEntity;
		
		shippingEntity = convert(shippingForm);
		shippingEntity.setShippingName(shippingForm.getShippingName());
        shippingEntity.setShippingKana(shippingForm.getShippingKana());
        shippingEntity.setShippingphone(shippingForm.getShippingphone());
        shippingEntity.setShippingPostalCode(shippingForm.getShippingPostalCode());
        shippingEntity.setShippingAddress1(shippingForm.getShippingAddress1());
        shippingEntity.setShippingAddress2(shippingForm.getShippingAddress2());
        shippingEntity.setShippingAddress3(shippingForm.getShippingAddress3());
        	
        shippingEntity = convert(orderForm.getShippingForm());
		
		MemberEntity memberEntity = convert(memberLoginForm);
		
		OrderEntity orderEntity = convert(orderForm);
		List<OrderDetailsEntity> detailsList = convert(orderForm.getOrderDetailsForm());

		// nullチェック
	    if (detailsList.isEmpty()) {
	        throw new IllegalArgumentException("注文詳細が空です");
	    }
		
		
		//発送情報登録して発送IDを取得
		int shippingId = orderRegistRepository.shippingRegist(shippingEntity);
		
		//注文内容登録して注文IDを取得
		int orderId = orderRegistRepository.orderRegist(shippingId, memberEntity.getMemberId(), orderEntity);
		
		//注文詳細内容登録
		orderRegistRepository.orderDetailsRegist(orderId, detailsList);
		
	}
	
	//sessionのIDを元にDBから会員情報を取得
	public MemberRegistForm getMemberList(int id) throws Exception {
		MemberEntity memberEntity = null;
		MemberRegistForm memberRegistForm = null;
		
		memberEntity = memberRepository.findByMemberId(id);
		memberRegistForm = convert(memberEntity);
		return memberRegistForm;
	}
	
	//ログイン情報変換（Entity → Form 変換）
	private MemberEntity convert(MemberLoginForm form) {
		
		MemberEntity entity = modelMapper.map(form, MemberEntity.class);
		
		return entity;
	}
	
	//発送情報変換（Entity → Form 変換）
	private ShippingEntity convert(ShippingForm form) {
		
		ShippingEntity entity = modelMapper.map(form, ShippingEntity.class);
		
		return entity;
	}
	
	//注文情報変換（Entity → Form 変換）
	private OrderEntity convert(OrderForm form) {
		
		OrderEntity entity = modelMapper.map(form, OrderEntity.class);
		
		return entity;
	}
	
	//注文詳細変換（Entity → Form 変換）
	private List<OrderDetailsEntity> convert(List<OrderDetailsForm> formList) {
	    List<OrderDetailsEntity> entityList = new ArrayList<>();
	    for (OrderDetailsForm form : formList) {
	        OrderDetailsEntity entity = modelMapper.map(form, OrderDetailsEntity.class);
	        entityList.add(entity);
	    }
	    return entityList;
	}
	
	//会員情報変換（Entity → Form 変換）
	private MemberRegistForm convert(MemberEntity memberEntity) {
		
		MemberRegistForm memberRegistForm =  modelMapper.map(memberEntity, MemberRegistForm.class);
		
		return memberRegistForm;
	}
}