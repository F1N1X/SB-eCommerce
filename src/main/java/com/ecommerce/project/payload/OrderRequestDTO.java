package com.ecommerce.project.payload;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderRequestDTO {

    @Schema(
            description = "Identifier of the delivery address for the order",
            example = "301"
    )
    private Long addressId;

    @Schema(
            description = "Payment method selected by the user (e.g., COD, CARD, PAYPAL)",
            example = "CARD"
    )
    private String PaymentMethod;

    @Schema(
            description = "Name of the payment gateway used for the transaction",
            example = "Stripe"
    )
    private String pgName;

    @Schema(
            description = "Payment gateway transaction identifier",
            example = "pay_123456789"
    )
    private String pgPaymentId;

    @Schema(
            description = "Raw response returned by the payment gateway",
            example = "Payment successful"
    )
    private String pgResponse;

    @Schema(
            description = "Current status of the payment",
            example = "SUCCESS"
    )
    private String pgStatus;
}
