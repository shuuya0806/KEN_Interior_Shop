package jp.ken.interiorshop.domain.repository;

import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import jp.ken.interiorshop.domain.entity.OrderDetailsEntity;
import jp.ken.interiorshop.domain.entity.OrderEntity;
import jp.ken.interiorshop.domain.entity.ShippingEntity;
import jp.ken.interiorshop.infrastructure.mapper.StaffOrderDetailsMapper;
import jp.ken.interiorshop.infrastructure.mapper.StaffOrderMapper;
import jp.ken.interiorshop.infrastructure.mapper.StaffShippingMapper;

@Repository
public class StaffOrderRepository {
	
	private RowMapper<OrderEntity> staffOrderMapper = new StaffOrderMapper();
	private RowMapper<OrderDetailsEntity> staffOrderDetailsMapper = new StaffOrderDetailsMapper();
	private RowMapper<ShippingEntity> staffShippingMapper = new StaffShippingMapper();
	private JdbcTemplate jdbcTemplate;
	
	public StaffOrderRepository(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
	//全注文履歴を取得
	public List<OrderEntity> getOrderAllList() throws Exception {
		
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT");
		sb.append(" *");
		sb.append(" FROM");
		sb.append(" orders");
		sb.append(" ORDER BY");
		sb.append(" order_id");
		String sql = sb.toString();
		
		List<OrderEntity> orderList = jdbcTemplate.query(sql, staffOrderMapper);
		
		return orderList;
	}
	
	//注文詳細情報を取得(orderIdをキーに設定)
	public List<OrderDetailsEntity> getOrderDetailsById(int orderId) throws Exception{

		String sql = "SELECT od.*, mi.item_name\n"
				+ "FROM order_details od\n"
				+ "JOIN m_items mi ON od.item_id = mi.item_id\n"
				+ "WHERE od.order_id = ?";
		
		List<OrderDetailsEntity> orderDetailsEntity = jdbcTemplate.query(sql, staffOrderDetailsMapper, orderId);
		
		return orderDetailsEntity;
	}
	
	//orderIdをキーにshippingIdを取得
	public ShippingEntity getShippingId(int orderId) throws Exception {
	    String sql = "SELECT s.shipping_id, s.shipping_name, s.shipping_kana, s.shipping_phone,\n"
	    		+ "       s.shipping_postal_code, s.shipping_address1, s.shipping_address2, s.shipping_address3\n"
	    		+ "FROM orders o\n"
	    		+ "JOIN shipping s ON o.shipping_id = s.shipping_id\n"
	    		+ "WHERE o.order_id = ?";
	    ShippingEntity shippingEntity = jdbcTemplate.queryForObject(sql, staffShippingMapper, orderId);
	    
	    return shippingEntity;
	}
	
	//orderIdをキーに発送フラグを取得
	public String getShippingFrag(int orderId) throws Exception{
		String sql = "SELECT shipping_frag FROM orders WHERE order_id = ?";
		return jdbcTemplate.queryForObject(sql, String.class, orderId);
		
	}
	
	//ステータスを発送済みに変更する処理
	public void shippedOrder(int orderId) throws Exception{
		 String sql = "UPDATE orders SET shipping_frag = '発送済み' WHERE order_id = ?";
		 jdbcTemplate.update(sql, orderId);
	}
	
	//ステータスを未発送に変更する処理
	public void cancelShippedOrder(int orderId) throws Exception{
		 String sql = "UPDATE orders SET shipping_frag = '未発送' WHERE order_id = ?";
		 jdbcTemplate.update(sql, orderId);
	}
}
