package jp.ken.interiorshop.infrastructure.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import jp.ken.interiorshop.domain.entity.OrderDetailsEntity;

public class StaffOrderDetailsMapper implements RowMapper<OrderDetailsEntity> {
	
	@Override
	public OrderDetailsEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
		
		OrderDetailsEntity orderDetailsEntity = new OrderDetailsEntity();
		
		orderDetailsEntity.setDetailId(rs.getInt("detail_id"));
		orderDetailsEntity.setOrderId(rs.getInt("order_id"));
		orderDetailsEntity.setItemId(rs.getInt("item_id"));
		orderDetailsEntity.setItemQuantity(rs.getInt("item_quantity"));
		orderDetailsEntity.setSubtotal(rs.getInt("subtotal"));
		orderDetailsEntity.setItemName(rs.getString("item_name"));
		
		return orderDetailsEntity;

	}

}
