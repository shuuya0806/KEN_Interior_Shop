package jp.ken.interiorshop.infrastructure.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import jp.ken.interiorshop.domain.entity.StaffEntity;

public class StaffLoginMapper implements RowMapper<StaffEntity>  {
	
	@Override
	public StaffEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
		
		StaffEntity staffEntity = new StaffEntity();
		
		staffEntity.setStaffId(rs.getInt("staff_id"));
		staffEntity.setStaffName(rs.getString("staff_name"));
		staffEntity.setPassword(rs.getString("password"));
		staffEntity.setAdministrator(rs.getString("administrator"));
		
		return staffEntity;

	}

}
