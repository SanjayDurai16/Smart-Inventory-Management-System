package com.inventory.api.dto;

import java.math.BigDecimal;

public class DashboardStatsDto {
    private int totalProducts;
    private int totalCategories;
    private int totalSalesCount;
    private BigDecimal totalRevenue;
    private int lowStockCount;

    public DashboardStatsDto() {
        this.totalRevenue = BigDecimal.ZERO;
    }

    public DashboardStatsDto(int totalProducts, int totalCategories, int totalSalesCount,
                             BigDecimal totalRevenue, int lowStockCount) {
        this.totalProducts = totalProducts;
        this.totalCategories = totalCategories;
        this.totalSalesCount = totalSalesCount;
        this.totalRevenue = totalRevenue;
        this.lowStockCount = lowStockCount;
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
