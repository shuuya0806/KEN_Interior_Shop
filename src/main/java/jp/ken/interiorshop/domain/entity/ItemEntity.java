package jp.ken.interiorshop.domain.entity;

import java.sql.Date;

import lombok.Data;

@Data
public class ItemEntity {

    // 商品ID
    private int itemId;
    
    // 商品名
    private String itemName;
    
    // カテゴリーID
    private int categoryId;
    
    // 価格
    private int itemPrice;
    
    // 発売開始日
    private Date rsDate;
    
    // 画像データ
    private String image;
    
    // 商品説明
    private String explanation;
    
    // 在庫数
    private int stock;
    
    // セールフラグ
    private String saleFrag;
    
    // セール価格
    private int salePrice;
}
