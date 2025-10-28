package jp.ken.interiorshop.domain.repository;

import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import jp.ken.interiorshop.domain.entity.OrderDetailsEntity;
import jp.ken.interiorshop.domain.entity.SalesEntity;
import jp.ken.interiorshop.infrastructure.mapper.SalesMapper;
import jp.ken.interiorshop.infrastructure.mapper.StaffOrderDetailsMapper;

@Repository
public class StaffStatisticsRepository {
	
	private RowMapper<SalesEntity> salesMapper = new SalesMapper();
	private RowMapper<OrderDetailsEntity> staffOrderDetailsMapper = new StaffOrderDetailsMapper();
	private JdbcTemplate jdbcTemplate;
	
	public StaffStatisticsRepository(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
	//全商品売上を取得
	public List<SalesEntity> getItemSalesAllList() throws Exception {
		
		String sql = "SELECT * "
				+ "FROM item_sales "
				+ "ORDER BY item_id";
		
		List<SalesEntity> itemSalesList = jdbcTemplate.query(sql, salesMapper);
		
		return itemSalesList;
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
