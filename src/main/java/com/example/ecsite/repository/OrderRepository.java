package com.example.ecsite.repository;

import com.example.ecsite.domain.Order;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {

	List<Order> findByUserIdOrderByOrderedAtDesc(Long userId);
}
