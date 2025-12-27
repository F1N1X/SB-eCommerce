package com.ecommerce.project.controller;

import com.ecommerce.project.config.AppConstants;
import com.ecommerce.project.payload.ProductDTO;
import com.ecommerce.project.payload.ProductResponse;
import com.ecommerce.project.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
@Tag(
        name = "Products",
        description = "Public endpoints for browsing products and admin endpoints for managing products"
)
@RestController
@RequestMapping("/api")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Operation(
            summary = "Create a product",
            description = "Creates a new product under the given category (admin access required)"
    )
    @PostMapping("/admin/categories/{categoryId}/product")
    public ResponseEntity<ProductDTO> addProduct(@Valid @RequestBody ProductDTO productDTO,
                                                 @PathVariable Long categoryId) {
       ProductDTO savedProductDTO =  productService.addProduct(categoryId, productDTO);
       return new ResponseEntity<>(savedProductDTO, HttpStatus.CREATED);
    }

    @Operation(
            summary = "Get all products",
            description = "Returns a paginated and sortable list of products"
    )
    @GetMapping("/public/products")
    public ResponseEntity<ProductResponse> getAllProducts(
            @RequestParam(name="pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(name="pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(name="sortBy", defaultValue = AppConstants.SORT_PRODUCT_BY, required = false) String sortBy,
            @RequestParam(name="sortOrder", defaultValue = AppConstants.SORT_DIR, required = false) String sortOrder
    ) {
        ProductResponse productResponse = productService.getAllProducts(pageNumber, pageNumber, sortBy, sortOrder);
        return new ResponseEntity<>(productResponse, HttpStatus.OK);
    }

    @Operation(
            summary = "Get products by category",
            description = "Returns a paginated and sortable list of products for the given category"
    )
    @GetMapping("/public/categories/{categoryId}/products")
    public ResponseEntity<ProductResponse> getProductsByCategory(
            @PathVariable Long categoryId,
            @RequestParam(name="pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(name="pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(name="sortBy", defaultValue = AppConstants.SORT_PRODUCT_BY, required = false) String sortBy,
            @RequestParam(name="sortOrder", defaultValue = AppConstants.SORT_DIR, required = false) String sortOrder
    ) {
        ProductResponse productResponse = productService.searchByCategory(categoryId,pageNumber, pageNumber, sortBy, sortOrder);
        return new ResponseEntity<>(productResponse, HttpStatus.OK);
    }

    @Operation(
            summary = "Search products by keyword",
            description = "Returns a paginated and sortable list of products matching the given keyword"
    )
    @GetMapping("/public/products/keyword/{keyword}")
    public ResponseEntity<ProductResponse> getProductsByKeyword(
            @PathVariable String keyword,
            @RequestParam(name="pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(name="pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(name="sortBy", defaultValue = AppConstants.SORT_PRODUCT_BY, required = false) String sortBy,
            @RequestParam(name="sortOrder", defaultValue = AppConstants.SORT_DIR, required = false) String sortOrder) {
        ProductResponse productResponse = productService.searchProductByKeyword(keyword,pageNumber, pageNumber, sortBy, sortOrder);
        return new ResponseEntity<>(productResponse, HttpStatus.FOUND);
    }

    @Operation(
            summary = "Update a product",
            description = "Updates an existing product identified by its ID (admin access required)"
    )
    @PutMapping("/admin/products/{productId}")
    public ResponseEntity<ProductDTO> updateProduct(
            @PathVariable Long productId,
            @Valid @RequestBody ProductDTO productDTO) {
        ProductDTO updatedProductDTO = productService.updateProduct(productId,productDTO);
        return new ResponseEntity<>(updatedProductDTO, HttpStatus.OK);
    }

    @Operation(
            summary = "Delete a product",
            description = "Deletes a product identified by its ID (admin access required)"
    )
    @DeleteMapping("/admin/products/{productId}")
    public ResponseEntity<ProductDTO> deleteProduct(@PathVariable Long productId) {
        ProductDTO deletedProduct = productService.deleteProduct(productId);
        return new ResponseEntity<>(deletedProduct, HttpStatus.OK);
    }

    @Operation(
            summary = "Update product image",
            description = "Uploads and assigns an image to the product identified by its ID (admin access required)"
    )
    @PutMapping("/products/{productId}/image")
    public ResponseEntity<ProductDTO> updateProductImage(@PathVariable Long productId,
                                                         @RequestParam("image")MultipartFile image) throws IOException {
        ProductDTO updateProduct = productService.updateProductImage(productId, image);
        return new ResponseEntity<>(updateProduct, HttpStatus.OK);
    }

}
