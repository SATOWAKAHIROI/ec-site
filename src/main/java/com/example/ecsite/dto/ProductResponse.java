package com.example.ecsite.dto;

import com.example.ecsite.domain.Product;
import java.math.BigDecimal;

/** 商品APIのレスポンス */
public record ProductResponse(
		Long id,
		String name,
		String description,
		BigDecimal price,
		Integer stock,
		Long categoryId,
		String categoryName) {

	public static ProductResponse from(Product product) {
		return new ProductResponse(
				product.getId(),
				product.getName(),
				product.getDescription(),
				product.getPrice(),
				product.getStock(),
				product.getCategory().getId(),
				product.getCategory().getName());
	}
}
