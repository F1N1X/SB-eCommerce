package com.ecommerce.project.payload;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentDTO {

    @Schema(
            description = "Unique identifier of the payment",
            example = "7001"
    )
    private Long paymentId;

    @Schema(
            description = "Payment method used for the transaction (e.g., COD, CARD, PAYPAL)",
            example = "CARD"
    )
    private String paymentMethod;

    @Schema(
            description = "Payment gateway transaction identifier",
            example = "pay_987654321"
    )
    private String pgPaymentId;

    @Schema(
            description = "Current status of the payment",
            example = "SUCCESS"
    )
    private String pgStatus;

    @Schema(
            description = "Response message returned by the payment gateway",
            example = "Payment completed successfully"
    )
    private String pgResponseMessage;

    @Schema(
            description = "Name of the payment gateway used",
            example = "Stripe"
    )
    private String pgName;
}
