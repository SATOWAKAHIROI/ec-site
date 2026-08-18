package com.example.ecsite.exception;

/** 指定したリソースが見つからない場合に投げる例外 */
public class ResourceNotFoundException extends RuntimeException {

	public ResourceNotFoundException(String message) {
		super(message);
	}
}
