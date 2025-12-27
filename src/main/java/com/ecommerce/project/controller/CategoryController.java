package com.ecommerce.project.controller;

import com.ecommerce.project.config.AppConstants;
import com.ecommerce.project.payload.CategoryDTO;
import com.ecommerce.project.payload.CategoryResponse;
import com.ecommerce.project.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(
        name = "Categories",
        description = "Public and admin endpoints for managing product categories"
)
@RestController
@RequestMapping("/api")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @Operation(
            summary = "Get all categories",
            description = "Returns a paginated and sortable list of all categories"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Categories retrieved successfully")
    })
    @GetMapping("/public/categories")
    public ResponseEntity<CategoryResponse> getAllCategories(
            @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageSize,
            @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_CATEGORY_BY, required = false) String sortBy,
            @RequestParam(name = "sortOder", defaultValue = AppConstants.SORT_DIR, required = false) String sortOrder) {

        CategoryResponse allCategories =
                categoryService.getAllCategories(pageNumber, pageSize, sortOrder, sortBy);
        return new ResponseEntity<>(allCategories, HttpStatus.OK);
    }

    @Operation(
            summary = "Create a category",
            description = "Creates a new category with the provided data"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Category created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid category data")
    })
    @PostMapping("/public/categories")
    public ResponseEntity<CategoryDTO> createCategory(
            @Valid @RequestBody CategoryDTO category) {

        CategoryDTO createdCategoryDTO = categoryService.createCategory(category);
        return new ResponseEntity<>(createdCategoryDTO, HttpStatus.CREATED);
    }

    @Operation(
            summary = "Delete category",
            description = "Deletes a category by ID (admin access required)"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Category deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Category not found")
    })
    @DeleteMapping("/admin/categories/{categoryId}")
    public ResponseEntity<CategoryDTO> deleteCategory(
            @PathVariable Long categoryId) {

        CategoryDTO categoryDTO = categoryService.deleteCategory(categoryId);
        return new ResponseEntity<>(categoryDTO, HttpStatus.OK);
    }

    @Operation(
            summary = "Update category",
            description = "Updates an existing category identified by its ID"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Category updated successfully"),
            @ApiResponse(responseCode = "404", description = "Category not found")
    })
    @PutMapping("/public/categories/{categoryId}")
    public ResponseEntity<CategoryDTO> updateCategory(
            @Valid @RequestBody CategoryDTO categoryDTO,
            @PathVariable Long categoryId) {

        CategoryDTO savedCategoryDTO =
                categoryService.updateCategory(categoryDTO, categoryId);
        return new ResponseEntity<>(savedCategoryDTO, HttpStatus.OK);
    }
}