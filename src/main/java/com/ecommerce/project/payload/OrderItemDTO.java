package com.ecommerce.project.payload;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemDTO {

    @Schema(
            description = "Unique identifier of the order item",
            example = "12001"
    )
    private Long orderItemId;

    @Schema(
            description = "Product details for the ordered item"
    )
    private ProductDTO product;

    @Schema(
            description = "Quantity of the product ordered",
            example = "2"
    )
    private Integer quantity;

    @Schema(
            description = "Discount applied to this order item",
            example = "5.0"
    )
    private double discount;

    @Schema(
            description = "Final price of the product at the time of order after discount",
            example = "499.99"
    )
    private double orderedProductPrice;
}
