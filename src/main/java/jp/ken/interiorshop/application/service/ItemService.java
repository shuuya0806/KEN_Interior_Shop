package jp.ken.interiorshop.application.service;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpSession;
import jp.ken.interiorshop.domain.entity.CategoryEntity;
import jp.ken.interiorshop.domain.entity.ItemEntity;
import jp.ken.interiorshop.domain.repository.ItemSearchRepository;
import jp.ken.interiorshop.presentation.form.CategoryForm;
import jp.ken.interiorshop.presentation.form.ItemForm;

@Service
public class ItemService {

	private final ItemSearchRepository itemSearchRepository;
	private final ModelMapper modelMapper;

	public ItemService(ItemSearchRepository itemSearchRepository, ModelMapper modelMapper) {
		this.itemSearchRepository = itemSearchRepository;
		this.modelMapper = modelMapper;
	}

	// 商品一覧を取得（Entity → Form 変換）
	public List<ItemForm> getItemList() throws Exception {
		List<ItemEntity> entityList = itemSearchRepository.getItemAllList();
		return convertItemForm(entityList);
	}

	// カテゴリ一覧を取得（Entity → Form 変換）
	public List<CategoryForm> getCategoryList() throws Exception {
		List<CategoryEntity> entityList = itemSearchRepository.getCategoryAllList();
		return convertCategoryForm(entityList);
	}

	// 商品Entity → Form変換（ModelMapperを使用）
	public List<ItemForm> convertItemForm(List<ItemEntity> entityList) {
		List<ItemForm> formList = new ArrayList<>();
		for (ItemEntity entity : entityList) {
			formList.add(modelMapper.map(entity, ItemForm.class));
		}
		return formList;
	}

	// カテゴリEntity → Form変換（ModelMapperを使用）
	public List<CategoryForm> convertCategoryForm(List<CategoryEntity> entityList) {
		List<CategoryForm> formList = new ArrayList<>();
		for (CategoryEntity entity : entityList) {
			formList.add(modelMapper.map(entity, CategoryForm.class));
		}
		return formList;
	}

	// セッションからカート情報を取得（なければ新規作成）
	public List<ItemForm> getCart(HttpSession session) {
		List<ItemForm> cart = (List<ItemForm>) session.getAttribute("cart");
		if (cart == null) {
			cart = new ArrayList<>();
			session.setAttribute("cart", cart);
		}
		return cart;
	}

	/*
	 * カートに商品を追加（同一商品なら数量加算）
	 */
	public void addToCart(HttpSession session, ItemForm form) {
		List<ItemForm> cart = getCart(session);
		boolean found = false;
		
		// カート内に同じ商品があるか確認
		for (ItemForm item : cart) {
			if (item.getItemId().equals(form.getItemId())) {
				item.setItemQuantity(item.getItemQuantity() + form.getItemQuantity());
				found = true;
				break;
			}
		}
		
		// カート内に商品がなければ新しく追加
		if (!found) {
			ItemForm newItem = new ItemForm();
			newItem.setItemId(form.getItemId());
			newItem.setItemName(form.getItemName());
			newItem.setItemPrice(form.getItemPrice());
			newItem.setItemQuantity(form.getItemQuantity());
			cart.add(newItem);
		}

		session.setAttribute("cart", cart);
	}

	/*
	 * 削除対象の商品IDを除いて新しいリストに詰め替え
	 */
	public void removeCart(HttpSession session, List<String> itemIds) {
		List<ItemForm> cart = getCart(session);
		List<ItemForm> newCart = new ArrayList<>();
		
		//itemIdsは「削除したい商品IDリスト」
		for (ItemForm item : cart) {
			if (!itemIds.contains(item.getItemId())) {
				newCart.add(item);
			}
		}
		
		session.setAttribute("cart", newCart);
	}

	// カートを全削除
	public void clearCart(HttpSession session) {
		session.removeAttribute("cart");
	}

	/*
	 * カート内商品の数量を更新
	 */
	public void updateQuantity(HttpSession session, List<String> itemIds, List<Integer> itemQuantities) {
		List<ItemForm> cart = getCart(session);
		
		//更新したい商品IDを探して、数量を更新
		for (int i = 0; i < itemIds.size(); i++) {
			String targetId = itemIds.get(i);
			int quantity = itemQuantities.get(i);
			for (ItemForm item : cart) {
				if (item.getItemId().equals(targetId)) {
					item.setItemQuantity(quantity);
					break;
				}
			}
		}
		session.setAttribute("cart", cart);
	}

	// 税抜き合計金額を計算
	public int totalExclTax(List<ItemForm> cartItems) {
		int totalExclTax = 0;
		for (ItemForm item : cartItems) {
			int price = Integer.parseInt(item.getItemPrice());
			totalExclTax += price * item.getItemQuantity();
		}
		return totalExclTax;
	}

	// 消費税を計算（10%）
	public int totalTax(int totalExclTax) {
		return (int) Math.round(totalExclTax * 0.1);
	}

	// 税込金額を計算
	public int totalInclTax(int totalExclTax) {
		return totalExclTax + totalTax(totalExclTax);
	}

	/*
	 * 検索条件に応じて商品を取得（条件が1つでもあれば絞り込み）
	 */
	public List<ItemEntity> searchItem(String keyword, Integer categoryId, Integer minPrice, Integer maxPrice) throws Exception {
		
		// 検索条件の有無を判定
		boolean hasKeyword = keyword != null && !keyword.trim().isEmpty();
		boolean hasCategory = categoryId != null;
		boolean hasMinPrice = minPrice != null;
		boolean hasMaxPrice = maxPrice != null;

		// リポジトリに渡す条件を整形
		return itemSearchRepository.searchByConditions(
			hasKeyword ? keyword.trim() : null,
			hasCategory ? categoryId : null,
			hasMinPrice ? minPrice : null,
			hasMaxPrice ? maxPrice : null
		);
	}

	// 商品IDから商品詳細を取得（Entity → Form）
	public ItemForm getItemById(int itemId) throws Exception {
		ItemEntity entity = itemSearchRepository.getItemById(itemId);
		return modelMapper.map(entity, ItemForm.class);
	}
}