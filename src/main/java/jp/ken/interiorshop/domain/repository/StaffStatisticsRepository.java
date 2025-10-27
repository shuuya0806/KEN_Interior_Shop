package jp.ken.interiorshop.domain.repository;

import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import jp.ken.interiorshop.domain.entity.OrderDetailsEntity;
import jp.ken.interiorshop.domain.entity.OrderEntity;
import jp.ken.interiorshop.infrastructure.mapper.StaffOrderDetailsMapper;
import jp.ken.interiorshop.infrastructure.mapper.StaffOrderMapper;

@Repository
public class StaffStatisticsRepository {
	
	private RowMapper<OrderEntity> staffOrderMapper = new StaffOrderMapper();
	private RowMapper<OrderDetailsEntity> staffOrderDetailsMapper = new StaffOrderDetailsMapper();
	private JdbcTemplate jdbcTemplate;
	
	public StaffStatisticsRepository(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
	//全注文履歴を取得
	public List<OrderEntity> getOrderAllList() throws Exception {
		
		String sql = "SELECT * "
				+ "FROM orders "
				+ "ORDER BY order_id";
		
		List<OrderEntity> orderList = jdbcTemplate.query(sql, staffOrderMapper);
		
		return orderList;
	}
	
	//全注文詳細情報を取得
	public List<OrderDetailsEntity> getOrderDetailsById() throws Exception{

		String sql = "SELECT * "
				+ "FROM order_details "
				+ "ORDER BY order_details_id";
		
		List<OrderDetailsEntity> orderDetailsEntity = jdbcTemplate.query(sql, staffOrderDetailsMapper);
		
		return orderDetailsEntity;
	}
	
	
}
