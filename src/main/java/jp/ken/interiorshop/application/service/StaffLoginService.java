package jp.ken.interiorshop.application.service;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import jp.ken.interiorshop.domain.entity.StaffEntity;
import jp.ken.interiorshop.domain.repository.StaffLoginRepository;
import jp.ken.interiorshop.presentation.form.StaffLoginForm;

@Service
public class StaffLoginService {
	
	private StaffLoginRepository staffLoginRepository;
	private ModelMapper modelMapper;
	
	public StaffLoginService(StaffLoginRepository staffLoginRepository, ModelMapper modelMapper) {
		this.staffLoginRepository = staffLoginRepository;
		this.modelMapper = modelMapper;
	}
	
	public List<StaffLoginForm> getStaffList() throws Exception {
		
		List<StaffEntity> entityList = null;
		List<StaffLoginForm> formList = null;
		
		entityList = staffLoginRepository.getStaffAllList();
		
		formList = convert(entityList);
		
		return formList;
	}
	
	//従業員IDをキーに従業員情報を取得
	public StaffLoginForm getStaffListById(int staffId) throws Exception{
		StaffEntity staffEntity = staffLoginRepository.getStaffListById(staffId);
		
		StaffLoginForm staffLoginForm = convert(staffEntity);
		
		return staffLoginForm;
	}
	
	//従業員情報登録処理
	public int staffRegist(StaffLoginForm staffLoginForm) throws Exception{
		StaffEntity entity = modelMapper.map(staffLoginForm, StaffEntity.class);
		return staffLoginRepository.staffRegist(entity);
	}
	
	//従業員情報変更処理
	public int staffEditRegist(StaffLoginForm staffLoginForm) throws Exception{
		StaffEntity entity = modelMapper.map(staffLoginForm, StaffEntity.class);
		return staffLoginRepository.staffEditRegist(entity);
	}
	
	//従業員情報削除処理
	public int staffDelete(int staffId)throws Exception{
		int judge = staffLoginRepository.staffDelete(staffId);
		return judge;
	}
	
	private List<StaffLoginForm> convert(List<StaffEntity> entityList) {
		
		List<StaffLoginForm> formList = new ArrayList<StaffLoginForm>();
		
		for (StaffEntity entity : entityList) {
			StaffLoginForm form = modelMapper.map(entity, StaffLoginForm.class);
			formList.add(form);
		}
		
		return formList;
	}
	
	private StaffLoginForm convert(StaffEntity staffEntity) {
			StaffLoginForm staffLoginForm = modelMapper.map(staffEntity, StaffLoginForm.class);
		
		return staffLoginForm;
	}
}
