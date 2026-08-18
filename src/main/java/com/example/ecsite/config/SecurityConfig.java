package com.example.ecsite.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * セキュリティ設定。
 *
 * <p>学習用のため、現時点では商品APIをすべて公開している。 認証を学ぶ段階になったら {@code permitAll()} を
 * {@code authenticated()} に変更してJWT等を導入する。
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
				// REST APIはトークンを使わないためCSRF保護を無効化する
				.csrf(csrf -> csrf.disable())
				// セッションを持たないステートレスなAPIとして扱う
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.authorizeHttpRequests(auth -> auth
						.requestMatchers(HttpMethod.GET, "/api/**").permitAll()
						.requestMatchers("/api/**").permitAll()
						.anyRequest().authenticated())
				.httpBasic(basic -> {
				});
		return http.build();
	}

	/** パスワードのハッシュ化に使用する */
	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
