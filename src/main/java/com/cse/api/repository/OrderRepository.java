package com.cse.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cse.api.model.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    Order findById(String orderId);
}