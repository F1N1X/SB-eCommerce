package com.ecommerce.project.service;

import com.ecommerce.project.model.Category;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.nio.file.ReadOnlyFileSystemException;
import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    private Long nextId = 1L;

    private List<Category> categories =
            new ArrayList<>();

    @Override
    public List<Category> getAllCategories() {
        return categories;
    }

    @Override
    public void createCategory(Category category) {
        category.setCategoryId(nextId++);
        categories.add(category);
    }

    @Override
    public String deleteCategory(Long categoryId) {

        Category category = categories.stream()
                .filter( c -> c.getCategoryId().equals(categoryId))
                .findFirst()
                .orElseThrow( () -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Resource not found"));

        if (category == null)
            return "Category not found";

        categories.remove(category);
        return "category deleted with id: " + categoryId;
    }

    @Override
    public Category updateCategory(Category category, Long categoryId) {
        Category updateCategory = categories.stream()
                .filter(c -> c.getCategoryId().equals(categoryId))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Resource not found"));

        updateCategory.setCategoryName(category.getCategoryName());
        return updateCategory;
    }
}
