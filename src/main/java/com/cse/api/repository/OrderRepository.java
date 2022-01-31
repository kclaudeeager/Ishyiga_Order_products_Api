package com.cse.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

import com.cse.api.model.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    Order findById(String orderId);

    @Query("SELECT o FROM Order o WHERE o.client=?1")
    List<Order> findByClient(String client);

    @Query("SELECT o FROM Order o WHERE o.ordernum=?1")
    List<Order> findAllCreatedAtTime(Integer ordernumber);
 
}