package com.inventory.api;

import com.inventory.api.dto.CreateSaleItemRequest;
import com.inventory.api.dto.CreateSaleRequest;
import com.inventory.api.dto.DashboardStatsDto;
import com.inventory.api.exception.AppException;
import com.inventory.api.model.Category;
import com.inventory.api.model.Product;
import com.inventory.api.model.Sale;
import com.inventory.api.service.CategoryService;
import com.inventory.api.service.DashboardService;
import com.inventory.api.service.ProductService;
import com.inventory.api.service.SaleService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class InventoryApiApplicationTests {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ProductService productService;

    @Autowired
    private SaleService saleService;

    @Autowired
    private DashboardService dashboardService;

    @Test
    public void testCategoryCrud() {
        Category cat = new Category();
        cat.setName("TestCategory_" + System.currentTimeMillis());
        cat.setDescription("Category for testing");

        Category created = categoryService.createCategory(cat);
        assertNotNull(created.getId());

        Category fetched = categoryService.getCategoryById(created.getId());
        assertEquals(created.getName(), fetched.getName());

        fetched.setDescription("Updated description");
        Category updated = categoryService.updateCategory(fetched.getId(), fetched);
        assertEquals("Updated description", updated.getDescription());

        categoryService.deleteCategory(created.getId());
        final int deletedId = created.getId();
        assertThrows(AppException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                categoryService.getCategoryById(deletedId);
            }
        });
    }

    @Test
    public void testProductCrudAndLowStock() {
        List<Category> categories = categoryService.getAllCategories();
        assertFalse(categories.isEmpty());
        int categoryId = categories.get(0).getId();

        Product prod = new Product();
        prod.setName("Test Product " + System.currentTimeMillis());
        prod.setSku("SKU-TEST-" + System.currentTimeMillis());
        prod.setCategoryId(categoryId);
        prod.setPrice(new BigDecimal("12.50"));
        prod.setQuantity(3);
        prod.setLowStockThreshold(5);

        Product created = productService.createProduct(prod);
        assertNotNull(created.getId());
        assertEquals(3, created.getQuantity());

        // Should appear in low stock because quantity (3) <= threshold (5)
        List<Product> lowStock = productService.getLowStockProducts();
        boolean foundInLowStock = false;
        for (int i = 0; i < lowStock.size(); i++) {
            if (lowStock.get(i).getId().equals(created.getId())) {
                foundInLowStock = true;
                break;
            }
        }
        assertTrue(foundInLowStock);

        // Update product
        created.setPrice(new BigDecimal("15.00"));
        Product updated = productService.updateProduct(created.getId(), created);
        assertEquals(new BigDecimal("15.00"), updated.getPrice());

        productService.deleteProduct(created.getId());
    }

    @Test
    public void testSaleTransactionalStockDeduction() {
        List<Category> categories = categoryService.getAllCategories();
        int categoryId = categories.get(0).getId();

        Product prod = new Product();
        prod.setName("Sale Test Item " + System.currentTimeMillis());
        prod.setSku("SKU-SALE-" + System.currentTimeMillis());
        prod.setCategoryId(categoryId);
        prod.setPrice(new BigDecimal("20.00"));
        prod.setQuantity(10);
        prod.setLowStockThreshold(2);
        Product created = productService.createProduct(prod);

        // Try to buy more than available -> should fail
        final CreateSaleRequest excessiveReq = new CreateSaleRequest();
        List<CreateSaleItemRequest> excessiveItems = new ArrayList<CreateSaleItemRequest>();
        excessiveItems.add(new CreateSaleItemRequest(created.getId(), 20));
        excessiveReq.setItems(excessiveItems);
        assertThrows(AppException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                saleService.createSale(excessiveReq);
            }
        });

        // Successful sale of 4 items
        CreateSaleRequest validReq = new CreateSaleRequest();
        List<CreateSaleItemRequest> validItems = new ArrayList<CreateSaleItemRequest>();
        validItems.add(new CreateSaleItemRequest(created.getId(), 4));
        validReq.setItems(validItems);

        Sale sale = saleService.createSale(validReq);
        assertNotNull(sale.getId());
        assertEquals(new BigDecimal("80.00"), sale.getTotalAmount());

        // Stock should now be reduced from 10 to 6
        Product afterSale = productService.getProductById(created.getId());
        assertEquals(6, afterSale.getQuantity());
    }

    @Test
    public void testDashboardStats() {
        DashboardStatsDto stats = dashboardService.getDashboardStats();
        assertNotNull(stats);
        assertTrue(stats.getTotalProducts() >= 0);
        assertTrue(stats.getTotalCategories() >= 0);
        assertTrue(stats.getTotalSalesCount() >= 0);
        assertNotNull(stats.getTotalRevenue());
    }
}
