package jp.ken.interiorshop.domain.repository;

import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import jp.ken.interiorshop.domain.entity.OrderEntity;
import jp.ken.interiorshop.infrastructure.mapper.StaffOrderMapper;

@Repository
public class StaffOrderRepository {
	
	private RowMapper<OrderEntity> staffOrderMapper = new StaffOrderMapper();
	private JdbcTemplate jdbcTemplate;
	
	public StaffOrderRepository(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
	//全注文履歴を取得
	public List<OrderEntity> getOrderAllList() throws Exception {
		
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT");
		sb.append(" *");
		sb.append(" FROM");
		sb.append(" orders");
		sb.append(" ORDER BY");
		sb.append(" order_id");
		String sql = sb.toString();
		
		List<OrderEntity> orderList = jdbcTemplate.query(sql,staffOrderMapper);
		
		return orderList;
	}

}
