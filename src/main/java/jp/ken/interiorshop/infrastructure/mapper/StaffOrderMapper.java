package jp.ken.interiorshop.infrastructure.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import jp.ken.interiorshop.domain.entity.OrderEntity;

public class StaffOrderMapper implements RowMapper<OrderEntity>  {
	
	@Override
	public OrderEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
		
		OrderEntity orderEntity = new OrderEntity();
		
		orderEntity.setOrderId(rs.getInt("order_id"));
		orderEntity.setMemberId(rs.getInt("member_id"));
		orderEntity.setTotal(rs.getInt("total"));
		orderEntity.setOrderDate(rs.getDate("order_date"));
		orderEntity.setPayment(rs.getString("payment"));
		orderEntity.setShippingId(rs.getInt("shipping_id"));
		orderEntity.setShippingFrag(rs.getString("shipping_frag"));
		orderEntity.setUsePoint(rs.getInt("use_point"));
		orderEntity.setSpecifyDelivery(rs.getString("specify_delivery"));
		orderEntity.setSpecifyDate(rs.getDate("specify_date"));
		orderEntity.setSpecifyTime(rs.getString("specify_time"));
		
		return orderEntity;

	}

}
