package com.ecommerce.project.payload;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartDTO {

    @Schema(
            description = "Unique identifier of the shopping cart",
            example = "2001"
    )
    private Long cartId;

    @Schema(
            description = "Total price of all products in the cart",
            example = "1499.99"
    )
    private Double totalPrice = 0.0;

    @Schema(
            description = "List of products currently added to the cart"
    )
    private List<ProductDTO> products = new ArrayList<>();
}
