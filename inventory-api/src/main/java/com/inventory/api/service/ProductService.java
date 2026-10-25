package com.inventory.api.service;

import com.inventory.api.exception.AppException;
import com.inventory.api.model.Category;
import com.inventory.api.model.Product;
import com.inventory.api.repository.CategoryRepository;
import com.inventory.api.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public ProductService(ProductRepository productRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product getProductById(int id) {
        Product product = productRepository.findById(id);
        if (product == null) {
            throw new AppException("Product not found with id: " + id);
        }
        return product;
    }

    public List<Product> getLowStockProducts() {
        return productRepository.findLowStock();
    }

    public Product createProduct(Product product) {
        validateProduct(product, null);
        productRepository.save(product);
        return getProductById(product.getId());
    }

    public Product updateProduct(int id, Product product) {
        getProductById(id);
        product.setId(id);
        validateProduct(product, id);
        productRepository.update(product);
        return getProductById(id);
    }

    @Transactional
    public void deleteProduct(int id) {
        getProductById(id);
        productRepository.deleteSaleItemsByProductId(id);
        productRepository.deleteById(id);
    }

    private void validateProduct(Product product, Integer excludeId) {
        if (product.getName() == null || product.getName().trim().isEmpty()) {
            throw new AppException("Product name is required.");
        }
        product.setName(product.getName().trim());

        if (product.getSku() == null || product.getSku().trim().isEmpty()) {
            throw new AppException("Product SKU is required.");
        }
        product.setSku(product.getSku().trim().toUpperCase());

        if (productRepository.existsBySku(product.getSku(), excludeId)) {
            throw new AppException("Product with SKU '" + product.getSku() + "' already exists.");
        }

        if (product.getCategoryId() == null) {
            throw new AppException("Category selection is required.");
        }

        Category category = categoryRepository.findById(product.getCategoryId());
        if (category == null) {
            throw new AppException("Selected category does not exist.");
        }

        if (product.getPrice() == null || product.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
            throw new AppException("Price must be greater than zero.");
        }

        if (product.getQuantity() == null || product.getQuantity() < 0) {
            throw new AppException("Quantity must be zero or positive.");
        }

        if (product.getLowStockThreshold() == null || product.getLowStockThreshold() < 0) {
            throw new AppException("Low stock threshold must be zero or positive.");
        }
    }
}
