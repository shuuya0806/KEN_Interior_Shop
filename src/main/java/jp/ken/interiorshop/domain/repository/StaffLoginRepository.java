package jp.ken.interiorshop.domain.repository;

import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import jp.ken.interiorshop.domain.entity.StaffEntity;
import jp.ken.interiorshop.infrastructure.mapper.StaffLoginMapper;

@Repository
public class StaffLoginRepository {

	private RowMapper<StaffEntity> staffLoginMapper = new StaffLoginMapper();
	private JdbcTemplate jdbcTemplate;
	
	public StaffLoginRepository(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
	public List<StaffEntity> getStaffAllList() throws Exception {
		
		StringBuilder sb = createCommonSQL();
		sb.append(" ORDER BY");
		sb.append(" staff_id");
		String sql = sb.toString();
		
		List<StaffEntity> staffList = jdbcTemplate.query(sql, staffLoginMapper);
		
		return staffList;
	}
	
	private StringBuilder createCommonSQL() {
		
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT");
		sb.append(" staff_id");
		sb.append(", staff_name");
		sb.append(", password");
		sb.append(", administrator");
		sb.append(" FROM");
		sb.append(" m_staff");
		
		return sb;
	}	
}
	