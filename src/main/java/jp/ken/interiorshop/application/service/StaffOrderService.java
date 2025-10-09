package jp.ken.interiorshop.application.service;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import jp.ken.interiorshop.domain.entity.MemberEntity;
import jp.ken.interiorshop.domain.entity.OrderEntity;
import jp.ken.interiorshop.domain.repository.MemberRepository;
import jp.ken.interiorshop.domain.repository.StaffOrderRepository;
import jp.ken.interiorshop.presentation.form.MemberLoginForm;
import jp.ken.interiorshop.presentation.form.OrderForm;

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
		
		return formList;
	}
	
	//注文履歴内の会員IDから会員名を取得
	public List<MemberLoginForm> getMemberNameList (List<OrderForm> listOrderForm) throws Exception {
		
		List<MemberEntity> entityList = new ArrayList<>();
		MemberEntity entity = null;
		List<MemberLoginForm> formList = null;
		
		//引数のリスト内にある会員IDから会員情報を取得し、entityListに格納
		for (OrderForm orderForm : listOrderForm) {
			entity = memberRepository.findByMemberId(orderForm.getMemberId());
			entityList.add(entity);
		}
		
		//取得したデータをList<MemberLoginForm>型に変換
		formList = convertName(entityList);
		
		return formList;
	}
	
	private List<OrderForm> convert(List<OrderEntity> entityList) {
		
		List<OrderForm> formList = new ArrayList<OrderForm>();
		
		for(OrderEntity entity : entityList) {
			OrderForm form = FormMapper.map(entity, OrderForm.class);
			formList.add(form);
		}
		
		return formList;
	}
	
	private List<MemberLoginForm> convertName(List<MemberEntity> entityList) {
		
		List<MemberLoginForm> formList = new ArrayList<MemberLoginForm>();
		
		for(MemberEntity entity : entityList) {
			MemberLoginForm form = FormMapper.map(entity, MemberLoginForm.class);
			formList.add(form);
		}
		
		return formList;
	}

}
