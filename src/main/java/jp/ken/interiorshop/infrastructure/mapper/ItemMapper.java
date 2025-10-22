package jp.ken.interiorshop.infrastructure.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import jp.ken.interiorshop.domain.entity.ItemEntity;

public class ItemMapper implements RowMapper<ItemEntity> {

	//DBから取得したアイテムテーブルの値をアイテムEntityにセット
	@Override
	public ItemEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
		ItemEntity itemEntity = new ItemEntity();
		
		itemEntity.setItemId(rs.getInt("item_id"));
		itemEntity.setItemName(rs.getString("item_name"));
		itemEntity.setCategoryId(rs.getInt("category_id"));
		itemEntity.setItemPrice(rs.getInt("item_price"));
		itemEntity.setRsDate(rs.getDate("rs_date"));
		itemEntity.setImage(rs.getString("image"));
		itemEntity.setExplanation(rs.getString("explanation"));
		itemEntity.setStock(rs.getInt("stock"));
		itemEntity.setSaleFrag(rs.getString("sale_frag"));
		itemEntity.setSalePrice(rs.getInt("sale_price"));
		
		return itemEntity;
	}
}