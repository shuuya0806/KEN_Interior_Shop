package jp.ken.interiorshop.domain.entity;

import lombok.Data;

@Data
public class StaffEntity {
	
	private int staffId;
	
	private String staffName;
	
	private String password;
	
	private String administrator;

}
