package jp.ken.interiorshop.infrastructure.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import jp.ken.interiorshop.domain.entity.ItemSalesEntity;

public class ItemSalesMapper implements RowMapper<ItemSalesEntity> {

	@Override
	public ItemSalesEntity mapRow(ResultSet rs, int rowNum) throws SQLException{
		
		ItemSalesEntity salseEntity = new ItemSalesEntity();
		
		salseEntity.setItemId(rs.getInt("item_id"));
		salseEntity.setItemQuantity(rs.getInt("total_quantity"));
		salseEntity.setTotal(rs.getInt("sales_total"));
		
		return salseEntity;
	}
}
