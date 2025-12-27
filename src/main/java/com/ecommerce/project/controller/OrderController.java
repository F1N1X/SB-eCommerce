package com.ecommerce.project.controller;

import com.ecommerce.project.payload.OrderDTO;
import com.ecommerce.project.payload.OrderRequestDTO;
import com.ecommerce.project.service.OrderService;
import com.ecommerce.project.util.AuthUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@RequestMapping("api")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private AuthUtil authUtil;


    @Operation(
            summary = "Place an order",
            description = "Places a new order for the logged-in user using the selected payment method"
    )
    @PostMapping("/order/users/payments/{paymentMethod}")
    public ResponseEntity<OrderDTO> orderProducts(
            @PathVariable String paymentMethod,
            @RequestBody OrderRequestDTO orderRequestDTO
            ) {
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
