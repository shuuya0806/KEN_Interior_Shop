package jp.ken.interiorshop.infrastructure.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import jp.ken.interiorshop.domain.entity.SalesEntity;

public class SalesMapper implements RowMapper<SalesEntity> {

	@Override
	public SalesEntity mapRow(ResultSet rs, int rowNum) throws SQLException{
		
		SalesEntity salseEntity = new SalesEntity();
		
		salseEntity.setSales(rs.getInt("sales"));
		
		return salseEntity;
	}
}
