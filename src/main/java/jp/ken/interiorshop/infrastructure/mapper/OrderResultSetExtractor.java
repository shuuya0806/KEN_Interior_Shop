package jp.ken.interiorshop.infrastructure.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import jp.ken.interiorshop.domain.entity.OrderDetailsEntity;
import jp.ken.interiorshop.domain.entity.OrderEntity;

public class OrderResultSetExtractor implements ResultSetExtractor<List<OrderEntity>> {

    @Override
    public List<OrderEntity> extractData(ResultSet rs) throws SQLException, DataAccessException {

        // OrderEntityのListを作成し、変数を初期化
        List<OrderEntity> orderList = new ArrayList<OrderEntity>();
        OrderEntity orderEntity = null;
        int tmpOrderId = 0;

        while (rs.next()) {
            // order_id分のOrderEntityオブジェクトを呼び出し
            if (orderEntity == null || tmpOrderId != rs.getInt("order_id")) {
                orderEntity = new OrderEntity();
                orderEntity.setOrderDetailsEntity(new ArrayList<OrderDetailsEntity>());
                orderEntity.setOrderId(rs.getInt("order_id"));
                orderEntity.setMemberId(rs.getInt("member_id"));
                orderEntity.setTotal(rs.getInt("total"));
                orderEntity.setOrderDate(rs.getDate("order_date"));
                orderEntity.setPayment(rs.getString("payment"));
                orderEntity.setShippingId(rs.getInt("shipping_id"));
                orderEntity.setShippingFrag(rs.getInt("shipping_frag")); // 修正済み
            }

            // order_idに対応したorder_detailsを登録
            OrderDetailsEntity orderDetailsEntity = new OrderDetailsEntity();
            orderDetailsEntity.setDetailId(rs.getInt("detail_id"));
            orderDetailsEntity.setOrderId(rs.getInt("order_id"));
            orderDetailsEntity.setItemId(rs.getInt("item_id"));
            orderDetailsEntity.setItemQuantity(rs.getInt("item_quantity"));
            orderDetailsEntity.setSubtotal(rs.getInt("subtotal"));
            orderEntity.getOrderDetailsEntity().add(orderDetailsEntity);

            // order_idが変わったタイミングでリストに追加
            if (tmpOrderId != rs.getInt("order_id")) {
                orderList.add(orderEntity);
            }

            // tmpOrderIdを現在のorder_idに変更する（whileの判定に使用）
            tmpOrderId = rs.getInt("order_id");
        }

        return orderList;
    }
}
