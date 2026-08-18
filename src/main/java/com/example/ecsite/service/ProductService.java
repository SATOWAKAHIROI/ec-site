package com.example.ecsite.service;

import com.example.ecsite.domain.Category;
import com.example.ecsite.domain.Product;
import com.example.ecsite.dto.ProductCreateRequest;
import com.example.ecsite.dto.ProductResponse;
import com.example.ecsite.exception.ResourceNotFoundException;
import com.example.ecsite.repository.CategoryRepository;
import com.example.ecsite.repository.ProductRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** 商品に関する業務処理 */
@Service
@Transactional(readOnly = true)
public class ProductService {

	private final ProductRepository productRepository;
	private final CategoryRepository categoryRepository;

	public ProductService(ProductRepository productRepository, CategoryRepository categoryRepository) {
		this.productRepository = productRepository;
		this.categoryRepository = categoryRepository;
	}

	/** 商品一覧を取得する（カテゴリIDの指定があれば絞り込む） */
	public List<ProductResponse> findAll(Long categoryId) {
		List<Product> products = (categoryId == null)
				? productRepository.findAll()
				: productRepository.findByCategoryId(categoryId);
		return products.stream().map(ProductResponse::from).toList();
	}

	/** 商品を1件取得する */
	public ProductResponse findById(Long id) {
		Product product = productRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("商品が見つかりません: id=" + id));
		return ProductResponse.from(product);
	}

	/** 商品を登録する */
	@Transactional
	public ProductResponse create(ProductCreateRequest request) {
		Category category = categoryRepository.findById(request.categoryId())
				.orElseThrow(() -> new ResourceNotFoundException("カテゴリが見つかりません: id=" + request.categoryId()));

		Product product = new Product(
				category,
				request.name(),
				request.description(),
				request.price(),
				request.stock());
		return ProductResponse.from(productRepository.save(product));
	}
}
