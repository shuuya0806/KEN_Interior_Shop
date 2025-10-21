package jp.ken.interiorshop.domain.repository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import jp.ken.interiorshop.domain.entity.CategoryEntity;
import jp.ken.interiorshop.domain.entity.ItemEntity;
import jp.ken.interiorshop.infrastructure.mapper.CategoryMapper;
import jp.ken.interiorshop.infrastructure.mapper.ItemMapper;

@Repository
public class ItemSearchRepository {

	private final RowMapper<ItemEntity> itemMapper = new ItemMapper();			 		// 商品データマッピング
	private final RowMapper<CategoryEntity> categoryMapper = new CategoryMapper();		// カテゴリデータマッピング
	private final JdbcTemplate jdbcTemplate;		// SQL実行用テンプレート

	// コンストラクタによるDI（JdbcTemplateの注入）
	public ItemSearchRepository(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	// 商品テーブルの共通SELECT句を生成
	private StringBuilder createCommonSQL() {
		return new StringBuilder(
			"SELECT item_id, item_name, category_id, item_price, rs_date, image, explanation, stock FROM m_items"
		);
	}

	// カテゴリテーブルの共通SELECT句を生成
	private StringBuilder createCommonCategorySQL() {
		return new StringBuilder("SELECT category_id, category_name FROM category");
	}

	// 全商品を取得（商品ID昇順）
	public List<ItemEntity> getItemAllList() throws Exception {
		String sql = createCommonSQL().append(" ORDER BY item_id").toString();
		return jdbcTemplate.query(sql, itemMapper);
	}

	// 全カテゴリを取得（カテゴリID昇順）
	public List<CategoryEntity> getCategoryAllList() throws Exception {
		String sql = createCommonCategorySQL().append(" ORDER BY category_id").toString();
		return jdbcTemplate.query(sql, categoryMapper);
	}
	
	// 在庫一覧を取得
	public List<ItemEntity> getItemStockList() throws Exception{
		String sql = "SELECT item_id, item_name, stock FROM m_items ORDER BY item_id";
		return jdbcTemplate.query(sql, itemMapper);
	}

	// 商品IDから1件取得
	public ItemEntity getItemById(int itemId) throws Exception {
		String sql = createCommonSQL().append(" WHERE item_id = ?").toString();
		return jdbcTemplate.queryForObject(sql, itemMapper, itemId);
	}
	
	// 次月発売予定商品を取得
	public List<ItemEntity> getNextMonthItem() throws Exception{
		String sql = "SELECT * FROM m_items WHERE YEAR(rs_date) = YEAR(CURDATE() + INTERVAL 1 MONTH) AND MONTH(rs_date) = MONTH(CURDATE() + INTERVAL 1 MONTH)";
		return jdbcTemplate.query(sql, itemMapper);
	}

	/*
	 *  検索条件（キーワード・カテゴリ・価格範囲）に応じて商品を取得
	 */
	public List<ItemEntity> searchByConditions(String keyword, Integer categoryId, Integer minPrice, Integer maxPrice) {
		StringBuilder sb = createCommonSQL(); // SELECT句のベース
		List<Object> params = new ArrayList<>(); // パラメータ格納用リスト

		// 各条件の有無を判定
		boolean hasKeyword = keyword != null && !keyword.trim().isEmpty();
		boolean hasCategory = categoryId != null;
		boolean hasMinPrice = minPrice != null;
		boolean hasMaxPrice = maxPrice != null;

		// 条件が1つでもあれば WHERE句を追加
		if (hasKeyword || hasCategory || hasMinPrice || hasMaxPrice) {
			sb.append(" WHERE");

			List<String> conditions = new ArrayList<>();

			// キーワードによる部分一致検索（%や_をエスケープし、ESCAPE句で明示）
			if (hasKeyword) {
				conditions.add(" item_name LIKE ? ESCAPE '\\\\'");
				String escapedKeyword = "%" + keyword.trim().replace("%", "\\%").replace("_", "\\_") + "%";
				params.add(escapedKeyword);
			}

			// カテゴリによる完全一致検索
			if (hasCategory) {
				conditions.add(" category_id = ?");
				params.add(categoryId);
			}

			// 最低金額による下限検索
			if (hasMinPrice) {
				conditions.add(" item_price >= ?");
				params.add(minPrice);
			}

			// 最高金額による上限検索
			if (hasMaxPrice) {
				conditions.add(" item_price <= ?");
				params.add(maxPrice);
			}
			sb.append(String.join(" AND", conditions));
		}
		sb.append(" ORDER BY item_id");

		try {
			// 条件がある場合は絞り込み検索、ない場合は全件取得
			return jdbcTemplate.query(sb.toString(), itemMapper, params.toArray());
		} catch (Exception e) {
			throw new RuntimeException("商品検索に失敗しました", e);		// SQL実行時の例外をラップして通知
		}
	}
}