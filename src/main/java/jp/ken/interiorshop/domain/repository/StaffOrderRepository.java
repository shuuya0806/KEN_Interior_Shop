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
public class StaffOrderRepository {
	
	private RowMapper<OrderEntity> staffOrderMapper = new StaffOrderMapper();
	private RowMapper<OrderDetailsEntity> staffOrderDetailsMapper = new StaffOrderDetailsMapper();
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
		
		List<OrderEntity> orderList = jdbcTemplate.query(sql, staffOrderMapper);
		
		return orderList;
	}
	
	//注文詳細情報を取得(orderIdをキーに設定)
	public List<OrderDetailsEntity> getOrderDetailsById(int orderId) throws Exception{

		String sql = "SELECT od.*, mi.item_name\n"
				+ "FROM order_details od\n"
				+ "JOIN m_items mi ON od.item_id = mi.item_id\n"
				+ "WHERE od.order_id = ?";
		
		List<OrderDetailsEntity> orderDetailsEntity = jdbcTemplate.query(sql, staffOrderDetailsMapper, orderId);
		
		return orderDetailsEntity;
	}
}
