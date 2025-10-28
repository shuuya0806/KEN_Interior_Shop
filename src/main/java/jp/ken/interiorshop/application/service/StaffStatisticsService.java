package jp.ken.interiorshop.application.service;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import jp.ken.interiorshop.domain.entity.ItemEntity;
import jp.ken.interiorshop.domain.entity.SalesEntity;
import jp.ken.interiorshop.domain.repository.ItemSearchRepository;
import jp.ken.interiorshop.domain.repository.StaffStatisticsRepository;
import jp.ken.interiorshop.presentation.form.SalesForm;

@Service
public class StaffStatisticsService {

	private StaffStatisticsRepository staffStatisticsRepository;
	private ItemSearchRepository itemSearchRepository;
	private ModelMapper  FormMapper;
	
	public StaffStatisticsService(StaffStatisticsRepository staffStatisticsRepository,
			ItemSearchRepository itemSearchRepository,
			ModelMapper  FormMapper) {
		this.staffStatisticsRepository = staffStatisticsRepository;
		this.itemSearchRepository = itemSearchRepository;
		this.FormMapper = FormMapper;
	}
	
	public List<SalesForm> getItemSalesList() throws Exception {
		
		List<SalesEntity> entityList = null;
		List<SalesForm> formList = null;
		
		entityList = staffStatisticsRepository.getItemSalesAllList();
		
		formList = convert(entityList);
		
		for(SalesForm form : formList) {
			form.setItemName(getItemName(form.getItemId()));
		}
		
		return formList;
	}
	
	//商品別売上内の商品IDから商品名を取得
	public String getItemName (int itemId) throws Exception{
		
		//引数の商品IDから商品情報を取得
		ItemEntity entity = itemSearchRepository.getItemById(itemId);
		
		if(entity == null) {
			//認証失敗時はnullを返す
			return null;
		}
		
		return entity.getItemName();
	}
	
	private List<SalesForm> convert(List<SalesEntity> entityList) {
		
		List<SalesForm> formList = new ArrayList<SalesForm>();
		
		for(SalesEntity entity : entityList) {
			SalesForm form = FormMapper.map(entity, SalesForm.class);
			formList.add(form);
		}
		
		return formList;
	}
	
}
