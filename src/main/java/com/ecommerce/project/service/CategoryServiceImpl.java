package com.ecommerce.project.service;

import com.ecommerce.project.exceptions.APIException;
import com.ecommerce.project.exceptions.MyGlobalExceptionHandler;
import com.ecommerce.project.exceptions.ResourceNotFoundException;
import com.ecommerce.project.model.Category;
import com.ecommerce.project.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    MyGlobalExceptionHandler myGlobalExceptionHandler;

    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll() ;
    }

    @Override
    public void createCategory(Category category) {
        Category savedCategory = categoryRepository.findByCategoryName(category.getCategoryName());
        if (savedCategory != null) {
            throw new APIException("Category already exists");
        }
        categoryRepository.save(category);
    }

    @Override
    public String deleteCategory(Long categoryId) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Category","categoryId",categoryId));
        categoryRepository.delete(category);
        return "Category with" + category.getCategoryName() + " deleted";
    }

    @Override
    public Category updateCategory(Category category, Long categoryId) {

        Category savedCategory = categoryRepository.findById(categoryId)
                .orElseThrow( () -> new ResourceNotFoundException("Category","categoryId",categoryId));

        savedCategory.setCategoryName(category.getCategoryName());
        return categoryRepository.save(savedCategory);
    }
}
