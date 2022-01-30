package com.cse.api.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import com.cse.api.exception.ResourceNotFoundException;
import com.cse.api.model.Product;
import com.cse.api.repository.ProductRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class ProductController {

  @Autowired
  private ProductRepository productRepository;

  @PostMapping("/products")
  public Product createProduct(@Valid @RequestBody Product product) throws ResourceNotFoundException {
    return productRepository.save(product);
  }

  @GetMapping("/products")
  public List<Product> getAllProducts(HttpServletRequest request) {
    System.out.println("Request::: "+request.toString());
    return productRepository.findAll();
  }

  @GetMapping("/products/{id}")
  public ResponseEntity<Product> getProductById(@PathVariable(value = "id") Long productId)
      throws ResourceNotFoundException {
    Product product = productRepository.findById(productId)
        .orElseThrow(() -> new ResourceNotFoundException("Product not found :: " + productId));
    return ResponseEntity.ok().body(product);
  }

  @DeleteMapping("/products/{id}")
  public Map<String, Boolean> deleteProduct(@PathVariable(value = "id") Long productId)
      throws ResourceNotFoundException {
    Product product = productRepository.findById(productId)
        .orElseThrow(() -> new ResourceNotFoundException("Product not found :: " + productId));
    productRepository.delete(product);
    Map<String, Boolean> response = new HashMap<>();
    response.put("deleted", Boolean.TRUE);
    return response;
  }

  @PutMapping("/products/{id}")
  public ResponseEntity<Product> updateProduct(@PathVariable(value = "id") Long productId,
      @Valid @RequestBody Product productDetails) throws ResourceNotFoundException {
    Product product = productRepository.findById(productId)
        .orElseThrow(() -> new ResourceNotFoundException("Product not found :: " + productId));
    product.setpname(productDetails.getpname());
    product.setprice(productDetails.getprice());
    product.setquantity(productDetails.getquantity());

    final Product updatedProduct = productRepository.save(product);
    return ResponseEntity.ok(updatedProduct);
  }

}
