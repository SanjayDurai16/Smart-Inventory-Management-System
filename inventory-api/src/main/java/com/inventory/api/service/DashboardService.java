package com.inventory.api.service;

import com.inventory.api.dto.DashboardStatsDto;
import com.inventory.api.repository.CategoryRepository;
import com.inventory.api.repository.ProductRepository;
import com.inventory.api.repository.SaleRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class DashboardService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final SaleRepository saleRepository;

    public DashboardService(ProductRepository productRepository,
                            CategoryRepository categoryRepository,
                            SaleRepository saleRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.saleRepository = saleRepository;
    }

    public DashboardStatsDto getDashboardStats() {
        int totalProducts = productRepository.countAll();
        int totalCategories = categoryRepository.countAll();
        int totalSales = saleRepository.countAll();
        BigDecimal totalRevenue = saleRepository.sumTotalRevenue();
        int lowStockCount = productRepository.countLowStock();

        return new DashboardStatsDto(totalProducts, totalCategories, totalSales, totalRevenue, lowStockCount);
    }
}
