package com.inventory.ui.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.math.BigDecimal;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DashboardStats {
    private int totalProducts;
    private int totalCategories;
    private int totalSalesCount;
    private BigDecimal totalRevenue = BigDecimal.ZERO;
    private int lowStockCount;

    public DashboardStats() {
    }

    public int getTotalProducts() {
        return totalProducts;
    }

    public void setTotalProducts(int totalProducts) {
        this.totalProducts = totalProducts;
    }

    public int getTotalCategories() {
        return totalCategories;
    }

    public void setTotalCategories(int totalCategories) {
        this.totalCategories = totalCategories;
    }

    public int getTotalSalesCount() {
        return totalSalesCount;
    }

    public void setTotalSalesCount(int totalSalesCount) {
        this.totalSalesCount = totalSalesCount;
    }

    public BigDecimal getTotalRevenue() {
        return totalRevenue;
    }

    public void setTotalRevenue(BigDecimal totalRevenue) {
        this.totalRevenue = totalRevenue;
    }

    public int getLowStockCount() {
        return lowStockCount;
    }

    public void setLowStockCount(int lowStockCount) {
        this.lowStockCount = lowStockCount;
    }
}
