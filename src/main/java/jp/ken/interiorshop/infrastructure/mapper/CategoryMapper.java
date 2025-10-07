package jp.ken.interiorshop.infrastructure.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import jp.ken.interiorshop.domain.entity.CategoryEntity;

public class CategoryMapper implements RowMapper<CategoryEntity> {

	//DBから取得したカテゴリテーブルの値をカテゴリEntityにセット
	@Override
	public CategoryEntity mapRow(ResultSet rs, int rowNum) throws SQLException{		
		CategoryEntity categoryEntity = new CategoryEntity();
		
		categoryEntity.setCategoryId(rs.getInt("category_id"));
		categoryEntity.setCategoryName(rs.getString("category_name"));
		
		return categoryEntity;
	}
}