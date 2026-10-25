package com.inventory.api.model;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class Sale {
    private Integer id;
    private String saleNumber;
    private BigDecimal totalAmount;
    private Timestamp saleDate;
    private List<SaleItem> items = new ArrayList<SaleItem>();

    public Sale() {
    }

    public Sale(Integer id, String saleNumber, BigDecimal totalAmount, Timestamp saleDate) {
        this.id = id;
        this.saleNumber = saleNumber;
        this.totalAmount = totalAmount;
        this.saleDate = saleDate;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSaleNumber() {
        return saleNumber;
    }

    public void setSaleNumber(String saleNumber) {
        this.saleNumber = saleNumber;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Timestamp getSaleDate() {
        return saleDate;
    }

    public void setSaleDate(Timestamp saleDate) {
        this.saleDate = saleDate;
    }

    public List<SaleItem> getItems() {
        return items;
    }

    public void setItems(List<SaleItem> items) {
        this.items = items;
    }
}
