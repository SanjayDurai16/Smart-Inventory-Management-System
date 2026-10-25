package com.inventory.api.service;

import com.inventory.api.exception.AppException;
import com.inventory.api.model.Category;
import com.inventory.api.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public Category getCategoryById(int id) {
        Category category = categoryRepository.findById(id);
        if (category == null) {
            throw new AppException("Category not found with id: " + id);
        }
        return category;
    }

    public Category createCategory(Category category) {
        if (category.getName() == null || category.getName().trim().isEmpty()) {
            throw new AppException("Category name is required.");
        }
        category.setName(category.getName().trim());
        if (categoryRepository.existsByName(category.getName(), null)) {
            throw new AppException("Category with name '" + category.getName() + "' already exists.");
        }
        categoryRepository.save(category);
        return category;
    }

    public Category updateCategory(int id, Category category) {
        Category existing = getCategoryById(id);
        if (category.getName() == null || category.getName().trim().isEmpty()) {
            throw new AppException("Category name is required.");
        }
        category.setName(category.getName().trim());
        if (categoryRepository.existsByName(category.getName(), id)) {
            throw new AppException("Category with name '" + category.getName() + "' already exists.");
        }
        existing.setName(category.getName());
        existing.setDescription(category.getDescription());
        categoryRepository.update(existing);
        return existing;
    }

    public void deleteCategory(int id) {
        getCategoryById(id);
        int productCount = categoryRepository.countProductsByCategoryId(id);
        if (productCount > 0) {
            throw new AppException("Cannot delete category because " + productCount + " product(s) are assigned to it.");
        }
        categoryRepository.deleteById(id);
    }
}
