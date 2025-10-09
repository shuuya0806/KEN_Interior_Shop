package jp.ken.interiorshop.application.service;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import jp.ken.interiorshop.domain.entity.OrderEntity;
import jp.ken.interiorshop.domain.repository.StaffOrderRepository;
import jp.ken.interiorshop.presentation.form.OrderForm;

@Service
public class StaffOrderService {
	
	private StaffOrderRepository staffOrderRepository;
	private ModelMapper formMapper;
	
	public StaffOrderService(StaffOrderRepository staffOrderRepository,
			ModelMapper formMapper) {
		this.staffOrderRepository = staffOrderRepository;
		this.formMapper = formMapper;
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
	
	private List<OrderForm> convert(List<OrderEntity> entityList) {
		
		List<OrderForm> formList = new ArrayList<OrderForm>();
		
		for(OrderEntity entity : entityList) {
			OrderForm form = formMapper.map(entity, OrderForm.class);
			formList.add(form);
		}
		
		return formList;
	}

}
