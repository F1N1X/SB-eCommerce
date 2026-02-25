package com.ecommerce.project.controller;

import com.ecommerce.project.config.AppConstants;
import com.ecommerce.project.payload.ProductDTO;
import com.ecommerce.project.payload.ProductResponse;
import com.ecommerce.project.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    // ---------- ADMIN ----------

    @Operation(
            summary = "Create a product",
            description = "Creates a new product under the given category (admin access required)"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Product created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid product data")
    })
    @SecurityRequirement(name = "bearerAuth")
    @PostMapping("/admin/categories/{categoryId}/product")
    public ResponseEntity<ProductDTO> addProduct(
            @Parameter(
                    description = "Unique identifier of the category the product belongs to",
                    example = "3"
            )
            @PathVariable Long categoryId,

            @Valid @RequestBody ProductDTO productDTO) {

        ProductDTO savedProductDTO = productService.addProduct(categoryId, productDTO);
        return new ResponseEntity<>(savedProductDTO, HttpStatus.CREATED);
    }

    @Operation(
            summary = "Update a product",
            description = "Updates an existing product identified by its ID (admin access required)"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Product updated successfully"),
            @ApiResponse(responseCode = "404", description = "Product not found")
    })
    @SecurityRequirement(name = "bearerAuth")
    @PutMapping("/admin/products/{productId}")
    public ResponseEntity<ProductDTO> updateProduct(
            @Parameter(
                    description = "Unique identifier of the product to update",
                    example = "10"
            )
            @PathVariable Long productId,

            @Valid @RequestBody ProductDTO productDTO) {

        ProductDTO updatedProductDTO =
                productService.updateProduct(productId, productDTO);
        return new ResponseEntity<>(updatedProductDTO, HttpStatus.OK);
    }

    @Operation(
            summary = "Delete a product",
            description = "Deletes a product identified by its ID (admin access required)"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Product deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Product not found")
    })
    @SecurityRequirement(name = "bearerAuth")
    @DeleteMapping("/admin/products/{productId}")
    public ResponseEntity<ProductDTO> deleteProduct(
            @Parameter(
                    description = "Unique identifier of the product to delete",
                    example = "10"
            )
            @PathVariable Long productId) {

        ProductDTO deletedProduct = productService.deleteProduct(productId);
        return new ResponseEntity<>(deletedProduct, HttpStatus.OK);
    }

    @Operation(
            summary = "Update product image",
            description = "Uploads and assigns an image to the product identified by its ID (admin access required)"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Product image updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid image file")
    })
    @SecurityRequirement(name = "bearerAuth")
    @PutMapping("/products/{productId}/image")
    public ResponseEntity<ProductDTO> updateProductImage(
            @Parameter(
                    description = "Unique identifier of the product",
                    example = "10"
            )
            @PathVariable Long productId,

            @Parameter(
                    description = "Image file to upload (JPG, PNG)",
                    required = true
            )
            @RequestParam("image") MultipartFile image) throws IOException {

        ProductDTO updatedProduct =
                productService.updateProductImage(productId, image);
        return new ResponseEntity<>(updatedProduct, HttpStatus.OK);
    }

    // ---------- PUBLIC ----------

    @Operation(
            summary = "Get all products",
            description = "Returns a paginated and sortable list of products"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Products retrieved successfully")
    })
    @GetMapping("/public/products")
    public ResponseEntity<ProductResponse> getAllProducts(
            @RequestParam(name= "keyword", required = false) String keyword,
            @RequestParam(name = "category", required = false) String category,
            @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_PRODUCT_BY, required = false) String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_DIR, required = false) String sortOrder) {

        ProductResponse productResponse =
                productService.getAllProducts(pageNumber, pageSize, sortBy, sortOrder, keyword, category);
        return new ResponseEntity<>(productResponse, HttpStatus.OK);
    }

    @Operation(
            summary = "Get products by category",
            description = "Returns a paginated and sortable list of products for the given category"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Products retrieved successfully")
    })
    @GetMapping("/public/categories/{categoryId}/products")
    public ResponseEntity<ProductResponse> getProductsByCategory(
            @Parameter(
                    description = "Unique identifier of the category",
                    example = "3"
            )
            @PathVariable Long categoryId,

            @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_PRODUCT_BY, required = false) String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_DIR, required = false) String sortOrder) {

        ProductResponse productResponse =
                productService.searchByCategory(categoryId, pageNumber, pageSize, sortBy, sortOrder);
        return new ResponseEntity<>(productResponse, HttpStatus.OK);
    }

    @Operation(
            summary = "Search products by keyword",
            description = "Returns a paginated and sortable list of products matching the given keyword"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Products retrieved successfully")
    })
    @GetMapping("/public/products/keyword/{keyword}")
    public ResponseEntity<ProductResponse> getProductsByKeyword(
            @Parameter(
                    description = "Search keyword used to match product name or description",
                    example = "laptop"
            )
            @PathVariable String keyword,

            @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_PRODUCT_BY, required = false) String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_DIR, required = false) String sortOrder) {

        ProductResponse productResponse =
                productService.searchProductByKeyword(keyword, pageNumber, pageSize, sortBy, sortOrder);
        return new ResponseEntity<>(productResponse, HttpStatus.OK);
    }
}
