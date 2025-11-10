package jp.ken.interiorshop.domain.entity;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class OrderEntity {
    
    // 注文ID
    private int orderId;
    
    // 顧客ID
    private int memberId;
    
    // 合計金額
    private int total;
    
    // 注文日
    private Date orderDate;
    
    // 支払方法
    private String payment;
    
    // 発送先ID
    private int shippingId;
    
    // 発送フラグ
    private String shippingFrag = "未発送";
    
    // 使用したポイント
    private int usePoint;
    
    //配送指定
    private String specifyDelivery;
    
    //日付指定
    private Date specifyDate;
    
    //時間指定
    private String specifyTime;
    
    // 複数商品を保持するリスト
    // private List<OrderDetailsForm> orderDetailsForm = new ArrayList<>();  元のデータ(テスト後の挙動次第で元に戻す)   
    private List<OrderDetailsEntity> orderDetailsEntity = new ArrayList<>();
}