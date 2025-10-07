package jp.ken.interiorshop.domain.entity;

import lombok.Data;

@Data
public class MemberEntity {
    
    // 会員ID
    private int memberId;
    
    // 氏名
    private String memberName;
    
    // フリガナ
    private String memberKana;
    
    // メールアドレス
    private String mail;
    
    // パスワード
    private String password;
    
    // 電話番号
    private String phoneNumber;
    
    // 郵便番号
    private String postalCode;
    
    // 住所1(都道府県)
    private String address1;
    
    // 住所2(市区町村)
    private String address2;
    
    // 住所3(番地以降)
    private String address3;
    
    // クレジットカード番号
    private String creditNo;
    
    // 退会フラグ 初期値は0
    private int cancel = 0;
}
