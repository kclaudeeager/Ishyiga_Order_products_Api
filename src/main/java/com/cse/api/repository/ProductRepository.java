package com.cse.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cse.api.model.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
	Product findById(String contactId);
}
