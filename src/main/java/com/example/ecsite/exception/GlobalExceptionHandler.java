package com.example.ecsite.exception;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/** REST APIの例外をJSONレスポンスに変換する */
@RestControllerAdvice
public class GlobalExceptionHandler {

	/** リソース未検出（404） */
	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<Map<String, Object>> handleNotFound(ResourceNotFoundException e) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body(HttpStatus.NOT_FOUND, e.getMessage()));
	}

	/** 入力値検証エラー（400） */
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Map<String, Object>> handleValidation(MethodArgumentNotValidException e) {
		Map<String, String> errors = new LinkedHashMap<>();
		e.getBindingResult().getFieldErrors()
				.forEach(fieldError -> errors.put(fieldError.getField(), fieldError.getDefaultMessage()));

		Map<String, Object> body = body(HttpStatus.BAD_REQUEST, "入力値が不正です");
		body.put("errors", errors);
		return ResponseEntity.badRequest().body(body);
	}

	/** 業務ルール違反など（400） */
	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<Map<String, Object>> handleIllegalArgument(IllegalArgumentException e) {
		return ResponseEntity.badRequest().body(body(HttpStatus.BAD_REQUEST, e.getMessage()));
	}

	private Map<String, Object> body(HttpStatus status, String message) {
		Map<String, Object> body = new LinkedHashMap<>();
		body.put("timestamp", LocalDateTime.now());
		body.put("status", status.value());
		body.put("error", status.getReasonPhrase());
		body.put("message", message);
		return body;
	}
}
