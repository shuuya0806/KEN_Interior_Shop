package jp.ken.interiorshop.infrastructure.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import jp.ken.interiorshop.domain.entity.ShippingEntity;

public class StaffShippingMapper implements RowMapper<ShippingEntity> {
	
	@Override
	public ShippingEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
		
		ShippingEntity shippingEntity = new ShippingEntity();
		
		shippingEntity.setShippingId(rs.getInt("shipping_id"));
		shippingEntity.setShippingName(rs.getString("shipping_name"));
		shippingEntity.setShippingKana(rs.getString("shipping_kana"));
		shippingEntity.setShippingphone(rs.getString("shipping_phone"));
		shippingEntity.setShippingPostalCode(rs.getString("shipping_postal_code"));
		shippingEntity.setShippingAddress1(rs.getString("shipping_address1"));
		shippingEntity.setShippingAddress2(rs.getString("shipping_address2"));
		shippingEntity.setShippingAddress3(rs.getString("shipping_address3"));
		
		return shippingEntity;

	}

}
