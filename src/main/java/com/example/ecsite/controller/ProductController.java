package com.example.ecsite.controller;

import com.example.ecsite.dto.ProductCreateRequest;
import com.example.ecsite.dto.ProductResponse;
import com.example.ecsite.service.ProductService;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/** 商品API */
@RestController
@RequestMapping("/api/products")
public class ProductController {

	private final ProductService productService;

	public ProductController(ProductService productService) {
		this.productService = productService;
	}

	/** 商品一覧（例: GET /api/products?categoryId=1） */
	@GetMapping
	public List<ProductResponse> list(@RequestParam(required = false) Long categoryId) {
		return productService.findAll(categoryId);
	}

	/** 商品詳細（例: GET /api/products/1） */
	@GetMapping("/{id}")
	public ProductResponse detail(@PathVariable Long id) {
		return productService.findById(id);
	}

	/** 商品登録（例: POST /api/products） */
	@PostMapping
	public ResponseEntity<ProductResponse> create(@Valid @RequestBody ProductCreateRequest request) {
		ProductResponse created = productService.create(request);
		return ResponseEntity.created(URI.create("/api/products/" + created.id())).body(created);
	}
}
