package jp.ken.interiorshop.application.service;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import jp.ken.interiorshop.domain.entity.ItemEntity;
import jp.ken.interiorshop.domain.entity.ItemSalesEntity;
import jp.ken.interiorshop.domain.entity.SalesEntity;
import jp.ken.interiorshop.domain.repository.ItemSearchRepository;
import jp.ken.interiorshop.domain.repository.StaffStatisticsRepository;
import jp.ken.interiorshop.presentation.form.ItemSalesForm;
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
	
	//商品別売上をDBから取得
	public List<ItemSalesForm> getItemSalesList() throws Exception {
		
		List<ItemSalesEntity> entityList = null;
		List<ItemSalesForm> formList = null;
		
		entityList = staffStatisticsRepository.getItemSalesAllList();
		
		formList = itemSalesConvert(entityList);
		
		for(ItemSalesForm form : formList) {
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
	
	//売上をDBから取得
	public List<SalesForm> getSalesList() throws Exception {
		
		List<SalesEntity> entityList = null;
		List<SalesForm> formList = null;
		
		entityList = staffStatisticsRepository.getSalesList();
		
		formList = salesConvert(entityList);
		
		return formList;
 	}
	
	private List<ItemSalesForm> itemSalesConvert(List<ItemSalesEntity> entityList) {
		
		List<ItemSalesForm> formList = new ArrayList<ItemSalesForm>();
		
		for(ItemSalesEntity entity : entityList) {
			ItemSalesForm form = FormMapper.map(entity, ItemSalesForm.class);
			formList.add(form);
		}
		
		return formList;
	}
	
	private List<SalesForm> salesConvert(List<SalesEntity> entityList) {
		
		List<SalesForm> formList = new ArrayList<SalesForm>();
		
		for(SalesEntity entity : entityList) {
			SalesForm form = FormMapper.map(entity, SalesForm.class);
			formList.add(form);
		}
		
		return formList;
	}
	
}
