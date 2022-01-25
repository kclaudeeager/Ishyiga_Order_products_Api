package com.cse.api.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler", "fieldHandler" })
@Entity
@Table(name = "product")
public class Product extends AuditModel {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@Column(name = "Pname", nullable = true)
	private String pname;

	@Column(name = "Price", nullable = false)
	private Double price;

	@Column(name = "Quantity", nullable = true)
	private Integer quantity;

	public Product() {
		super();
	}

	public Product(String pname, double price, Integer quantity) {
		super();
		this.pname = pname;
		this.price = price;
		this.quantity = quantity;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getpname() {
		return pname;
	}

	public void setpname(String pname) {
		this.pname = pname;
	}

	public Double getprice() {
		return price;
	}

	public void setprice(Double price) {
		this.price = price;
	}

	public Integer getquantity() {
		return quantity;
	}

	public void setquantity(Integer quantity) {
		this.quantity = quantity;
	}

}
