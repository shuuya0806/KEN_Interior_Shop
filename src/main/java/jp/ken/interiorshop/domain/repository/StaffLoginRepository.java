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
	
	//従業員IDをキーに従業員情報を取得
	public StaffEntity getStaffListById(int staffId) throws Exception{
		StringBuilder sb = createCommonSQL();
		sb.append(" WHERE staff_id = ?");
		String sql = sb.toString();
		
		StaffEntity staffList = jdbcTemplate.queryForObject(sql, staffLoginMapper, staffId);
		return staffList;
	}
	
	//従業員情報登録処理
	public int staffRegist(StaffEntity staffEntity) throws Exception{
		StringBuilder sb = new StringBuilder();
		sb.append("INSERT INTO m_staff (");
		sb.append("staff_id, staff_name, password, administrator");
		sb.append(") VALUES (?, ?, ?, ?)");
		String sql = sb.toString();
		
		Object[] params = {
				staffEntity.getStaffId(),
				staffEntity.getStaffName(),
				staffEntity.getPassword(),
				staffEntity.getAdministrator()
			};
		return jdbcTemplate.update(sql, params);
	}
	
	//従業員情報変更処理
	public int staffEditRegist(StaffEntity staffEntity) throws Exception{
		StringBuilder sb = new StringBuilder();
		  sb.append("UPDATE m_staff SET ");
		  sb.append("staff_name = ?, ");
		  sb.append("password = ?, ");
		  sb.append("administrator = ? ");
		  sb.append("WHERE staff_id = ?");
		String sql = sb.toString();
		
		Object[] params = {
				staffEntity.getStaffName(),
				staffEntity.getPassword(),
				staffEntity.getAdministrator(),
				staffEntity.getStaffId()
			};
		return jdbcTemplate.update(sql, params);
	}
	
	//従業員情報削除処理
	public int staffDelete(int staffId) throws Exception{
		String sql = "DELETE FROM m_staff WHERE staff_id = ?";
	    return jdbcTemplate.update(sql, staffId);
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
	