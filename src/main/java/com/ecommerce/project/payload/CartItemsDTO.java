package com.ecommerce.project.payload;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartItemsDTO {

    @Schema(
            description = "Unique identifier of the cart item",
            example = "501"
    )
    private Long cartItemId;

    @Schema(
            description = "Cart to which this item belongs"
    )
    private CartDTO cart;

    @Schema(
            description = "Product details associated with this cart item"
    )
    private ProductDTO productDTO;

    @Schema(
            description = "Discount applied to this cart item",
            example = "10.5"
    )
    private Double discount;

    @Schema(
            description = "Final price of the product after discount",
            example = "899.99"
    )
    private Double productPrice;
}
