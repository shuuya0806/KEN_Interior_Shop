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
	
	public List<StaffLoginForm> getStaffList(StaffLoginForm form) throws Exception {
		
		List<StaffEntity> entityList = null;
		List<StaffLoginForm> formList = null;
		
		entityList = staffLoginRepository.getStaffAllList();
		
		formList = convert(entityList);
		
		return formList;
	}
	
	private List<StaffLoginForm> convert(List<StaffEntity> entityList) {
		
		List<StaffLoginForm> formList = new ArrayList<StaffLoginForm>();
		
		for (StaffEntity entity : entityList) {
			StaffLoginForm form = modelMapper.map(entity, StaffLoginForm.class);
			formList.add(form);
		}
		
		return formList;
	}
}
