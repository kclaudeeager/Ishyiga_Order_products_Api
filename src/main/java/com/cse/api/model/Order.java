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
@Table(name = "Orders")
public class Order extends AuditModel {

    private static final long serialVersionUID = 1L;

     @Id
     @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "ordernum", unique = false)
    private Integer ordernum;
     
    @Column(name = "Pname", nullable = true)
    private String pname;

    @Column(name = "client", nullable = false)
    private String client;

    @Column(name = "suplier", nullable = false)
    private String suplier;

    @Column(name = "Quantity", nullable = true)
    private Integer quantity;

    public Order() {
        super();
    }

    public Order(Integer ordernum, String pname, String suplier, Integer quantity, String client) {
        super();
        this.ordernum=ordernum;
        this.client = client;
        this.pname = pname;
        this.suplier = suplier;
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

    public void setordernum( Integer ordernum) {
        this.ordernum = ordernum;
    }
    public Integer getordernum() {
        return ordernum;
    }

    public void setpname(String pname) {
        this.pname = pname;
    }

    public String getclient() {
        return client;
    }

    public void setclient(String client) {
        this.client = client;
    }

    public String getsuplier() {
        return suplier;
    }

    public void setsuplier(String suplier) {
        this.suplier = suplier;
    }

    public Integer getquantity() {
        return quantity;
    }

    public void setquantity(Integer quantity) {
        this.quantity = quantity;
    }

}
