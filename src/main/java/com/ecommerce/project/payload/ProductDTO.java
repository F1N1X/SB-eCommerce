package com.ecommerce.project.payload;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {

    @Schema(
            description = "Unique identifier of the product",
            example = "1001"
    )
    private Long productId;

    @Schema(
            description = "Name of the product",
            example = "iPhone 16 Pro"
    )
    private String productName;

    @Schema(
            description = "URL or filename of the product image"
    )
    private String image;

    @Schema(
            description = "Available quantity of the product in stock",
            example = "50"
    )
    private Integer quantity;

    @Schema(
            description = "Detailed description of the product"
    )
    private String description;

    @Schema(
            description = "Original price of the product before any discount",
            example = "1299.99"
    )
    private double price;

    @Schema(
            description = "Discount applied to the product",
            example = "10.0"
    )
    private double discount;

    @Schema(
            description = "Final price of the product after applying the discount",
            example = "1169.99"
    )
    private double specialPrice;
}
