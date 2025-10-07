package jp.ken.interiorshop.infrastructure.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import jp.ken.interiorshop.domain.entity.MemberEntity;

public class LoginMapper implements RowMapper<MemberEntity>  {
    
    // DBから取得した会員テーブルの値をMemberEntityにセット
    @Override
    public MemberEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
        
        MemberEntity memberEntity = new MemberEntity();
        
        // 会員IDと会員名のみをセット（ログイン処理用）
        memberEntity.setMemberId(rs.getInt("member_id"));
        memberEntity.setMemberName(rs.getString("member_name"));
        
        return memberEntity;
    }
}
