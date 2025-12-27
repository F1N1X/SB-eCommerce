package com.ecommerce.project.payload;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDTO {

    @Schema(
            description = "Unique identifier of the order",
            example = "9001"
    )
    private Long orderId;

    @Schema(
            description = "Email address of the user who placed the order",
            example = "customer@example.com"
    )
    private String email;

    @Schema(
            description = "List of items included in the order"
    )
    private List<OrderItemDTO> orderItems;

    @Schema(
            description = "Date when the order was placed",
            example = "2025-01-15"
    )
    private LocalDate orderDate;

    @Schema(
            description = "Payment details associated with this order"
    )
    private PaymentDTO payment;

    @Schema(
            description = "Total amount payable for the order",
            example = "2499.99"
    )
    private Double totalAmount;

    @Schema(
            description = "Current status of the order",
            example = "PLACED"
    )
    private String orderStatus;

    @Schema(
            description = "Identifier of the delivery address associated with the order",
            example = "301"
    )
    private Long addressId;
}
