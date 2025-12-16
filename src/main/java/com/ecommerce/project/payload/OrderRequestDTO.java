package com.ecommerce.project.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderRequestDTO {
    private Long addressId;
    private String PaymentMethod;
    private String pgName;
    private String pgPaymentId;
    private String pgResponse;
    private String pgStatus;
}
