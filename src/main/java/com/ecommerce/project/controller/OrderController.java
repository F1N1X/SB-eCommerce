package com.ecommerce.project.controller;

import com.ecommerce.project.payload.OrderDTO;
import com.ecommerce.project.payload.OrderRequestDTO;
import com.ecommerce.project.service.OrderService;
import com.ecommerce.project.util.AuthUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(
        name = "Orders",
        description = "Endpoints for placing and managing customer orders"
)
@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/api")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private AuthUtil authUtil;

    @Operation(
            summary = "Place an order",
            description = "Places a new order for the authenticated user using the selected payment method"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Order placed successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid order request or payment data"),
            @ApiResponse(responseCode = "404", description = "Address not found")
    })
    @PostMapping("/order/users/payments/{paymentMethod}")
    public ResponseEntity<OrderDTO> orderProducts(
            @Parameter(
                    description = "Payment method to use for this order (e.g., COD, CARD, PAYPAL)",
                    example = "CARD"
            )
            @PathVariable String paymentMethod,

            @Parameter(description = "Order details including address and payment gateway information")
            @RequestBody OrderRequestDTO orderRequestDTO) {

        String emailId = authUtil.loggedInEmail();

        OrderDTO orderDTO = orderService.placeOrder(
                emailId,
                orderRequestDTO.getAddressId(),
                paymentMethod,
                orderRequestDTO.getPgName(),
                orderRequestDTO.getPgPaymentId(),
                orderRequestDTO.getPgStatus(),
                orderRequestDTO.getPgResponse()
        );

        return new ResponseEntity<>(orderDTO, HttpStatus.CREATED);
    }
}
