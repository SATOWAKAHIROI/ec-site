package com.example.ecsite.repository;

import com.example.ecsite.domain.Product;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {

	/** カテゴリIDで商品を検索する */
	List<Product> findByCategoryId(Long categoryId);

	/** 商品名の部分一致で検索する */
	List<Product> findByNameContaining(String keyword);
}
