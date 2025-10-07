package jp.ken.interiorshop.infrastructure.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import jp.ken.interiorshop.domain.entity.MemberEntity;

public class MemberMapper implements RowMapper<MemberEntity>  {

    // DBから取得した会員テーブルの値をMemberEntityにセット
	@Override
	public MemberEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
		
		MemberEntity memberEntity = new MemberEntity();
		
		memberEntity.setMemberId(rs.getInt("member_id"));
		memberEntity.setMemberName(rs.getString("member_name"));
		memberEntity.setMemberKana(rs.getString("member_kana"));
		memberEntity.setMail(rs.getString("mail"));
		memberEntity.setPassword(rs.getString("password"));
		memberEntity.setPhoneNumber(rs.getString("phone_number"));
		memberEntity.setPostalCode(rs.getString("postal_code"));
		memberEntity.setAddress1(rs.getString("address1"));	
		memberEntity.setAddress2(rs.getString("address2"));
		memberEntity.setAddress3(rs.getString("address3"));
		memberEntity.setCreditNo(rs.getString("credit_no"));
		memberEntity.setCancel(rs.getInt("cancel"));
		
		return memberEntity;

	}
}
