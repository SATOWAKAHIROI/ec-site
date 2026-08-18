package com.example.ecsite.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

/** 商品登録リクエスト */
public record ProductCreateRequest(
		@NotNull(message = "カテゴリIDは必須です") Long categoryId,
		@NotBlank(message = "商品名は必須です") @Size(max = 200, message = "商品名は200文字以内で入力してください") String name,
		@Size(max = 1000, message = "商品説明は1000文字以内で入力してください") String description,
		@NotNull(message = "価格は必須です") @DecimalMin(value = "0.0", message = "価格は0以上で入力してください") BigDecimal price,
		@NotNull(message = "在庫数は必須です") @Min(value = 0, message = "在庫数は0以上で入力してください") Integer stock) {
}
