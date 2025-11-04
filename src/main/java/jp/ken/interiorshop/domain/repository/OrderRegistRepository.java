package jp.ken.interiorshop.domain.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import jp.ken.interiorshop.domain.entity.OrderDetailsEntity;
import jp.ken.interiorshop.domain.entity.OrderEntity;
import jp.ken.interiorshop.domain.entity.ShippingEntity;
import jp.ken.interiorshop.infrastructure.mapper.StaffOrderMapper;

@Repository
public class OrderRegistRepository {

	private JdbcTemplate jdbcTemplate;
	private RowMapper<OrderEntity> staffOrderMapper = new StaffOrderMapper();
	
	public OrderRegistRepository(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
	/*
	 *  発送情報をDBに保存し、登録した発送IDを返す
	 */
	public int shippingRegist(ShippingEntity shippingEntity){
		
		String sql = "INSERT INTO shipping (shipping_name, shipping_kana, shipping_phone, " +
	             "shipping_postal_code, shipping_address1, shipping_address2, shipping_address3) " +
	             "VALUES (?, ?, ?, ?, ?, ?, ?)";
		
		// shippingEntity から各発送情報を取得し、? プレースホルダに順番にセットして DB に挿入
		jdbcTemplate.update(sql, shippingEntity.getShippingName(),
                shippingEntity.getShippingKana(),
                shippingEntity.getShippingphone(),
                shippingEntity.getShippingPostalCode(),
                shippingEntity.getShippingAddress1(),
                shippingEntity.getShippingAddress2(),
                shippingEntity.getShippingAddress3());

		// 登録直後の発送IDを取得して返す
		Integer shippingId = jdbcTemplate.queryForObject("SELECT LAST_INSERT_ID()", Integer.class);

		return shippingId;
}
	/*
	 *  注文情報をDBに保存し、登録した注文IDを返す
	 */
		public int orderRegist(int shippingId, int memberId, OrderEntity orderEntity) {
		String orderSQL = "INSERT INTO orders (member_id, total, order_date, " +
	             "payment, shipping_id, shipping_frag, use_point) " +
	             "VALUES (?, ?, ?, ?, ?, ?, ?)";
		
		String salesSQL = "INSERT INTO sales (sales_id, sales) VALUES(1, ?)"
				+ "ON DUPLICATE KEY UPDATE sales = sales + ?";
		
		// 注文情報をDBに登録（未発送状態で保存）
		jdbcTemplate.update(orderSQL, memberId,
                orderEntity.getTotal(),
                LocalDate.now(),
                orderEntity.getPayment(),
                shippingId, 
                "未発送",
                orderEntity.getUsePoint());
		
		//登録直後の注文IDを取得
		Integer orderId = jdbcTemplate.queryForObject("SELECT LAST_INSERT_ID()", Integer.class);
		
		// 売上をDBに登録
		jdbcTemplate.update(salesSQL, 
				orderEntity.getTotal(),
				orderEntity.getTotal());

		return orderId;
	}
	
	/*
	 * 注文詳細情報をDBに保存する	
	 */
	public void orderDetailsRegist(int orderId, List<OrderDetailsEntity> orderDetailsEntity) {
		String orderDetailsSQL = "INSERT INTO order_details (order_id, item_id, item_quantity, " +
	             "subtotal)" +
	             "VALUES (?, ?, ?, ?)";
		
		String itemSalesSQL = "INSERT INTO item_sales(item_id, total_quantity, sales_total) VALUES (?, ?, ?) " +
                "ON DUPLICATE KEY UPDATE total_quantity = total_quantity + ?, sales_total = sales_total + ?";
		
		String itemStockSQL = "UPDATE m_items AS i " +
                "JOIN order_details AS od ON i.item_id = od.item_id " +
                "SET i.stock = GREATEST(i.stock - od.item_quantity, 0) " +
                "WHERE od.order_id = ?";
		
		// リスト内の各商品を順に登録
		for(OrderDetailsEntity regist : orderDetailsEntity) {
		// 注文詳細テーブルに登録
		jdbcTemplate.update(orderDetailsSQL, 
				orderId,
                regist.getItemId(),
                regist.getItemQuantity(),
                regist.getSubtotal());
		
		// 商品売り上げテーブルに登録
		jdbcTemplate.update(itemSalesSQL,
				regist.getItemId(),
				regist.getItemQuantity(),
				regist.getSubtotal(),
				regist.getItemQuantity(),
				regist.getSubtotal());
		}
		
		jdbcTemplate.update(itemStockSQL, orderId);
	}
	
	//獲得ポイントをDBに登録
	public void pointUpdate(int currentPoint, int memberId) {
		//UPDATE文を作成
		String sql = "UPDATE members SET point = ? WHERE ?";
		
		//引数を反映させて登録
		jdbcTemplate.update(sql, currentPoint, memberId);
	}
	
	public List<OrderEntity> getOrderListById(int memberId) throws Exception{
		String sql = "SELECT order_id, member_id, total, order_date, payment, shipping_id, shipping_frag,  use_point FROM orders WHERE member_id = ? ORDER BY order_id";
		return jdbcTemplate.query(sql, staffOrderMapper, memberId);
	}
}