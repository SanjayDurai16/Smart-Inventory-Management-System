package com.inventory.api.model;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class Product {
    private Integer id;
    private String name;
    private String sku;
    private Integer categoryId;
    private String categoryName;
    private BigDecimal price;
    private Integer quantity;
    private Integer lowStockThreshold;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    public Product() {
    }

    public Product(Integer id, String name, String sku, Integer categoryId, String categoryName,
                   BigDecimal price, Integer quantity, Integer lowStockThreshold,
                   Timestamp createdAt, Timestamp updatedAt) {
        this.id = id;
        this.name = name;
        this.sku = sku;
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.price = price;
        this.quantity = quantity;
        this.lowStockThreshold = lowStockThreshold;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getLowStockThreshold() {
        return lowStockThreshold;
    }

    public void setLowStockThreshold(Integer lowStockThreshold) {
        this.lowStockThreshold = lowStockThreshold;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }
}
