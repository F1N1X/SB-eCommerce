package com.ecommerce.project.controller;

import com.ecommerce.project.model.Cart;
import com.ecommerce.project.payload.CartDTO;
import com.ecommerce.project.repositories.CartRepository;
import com.ecommerce.project.service.CartService;
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

import java.util.List;

@Tag(
        name = "Cart",
        description = "Endpoints for managing the authenticated user's shopping cart"
)
@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/api")
public class CartController {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartService cartService;

    @Autowired
    private AuthUtil authUtil;

    @Operation(
            summary = "Add product to cart",
            description = "Adds a product to the authenticated user's cart with the specified quantity"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Product added to cart successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid product or quantity")
    })
    @PostMapping("/carts/products/{productId}/quantity/{quantity}")
    public ResponseEntity<CartDTO> addProductToCart(
            @Parameter(description = "Unique identifier of the product to add", example = "10")
            @PathVariable Long productId,

            @Parameter(description = "Quantity to add (must be a positive number)", example = "2")
            @PathVariable Integer quantity) {

        CartDTO cartDTO = cartService.addProductToCart(productId, quantity);
        return new ResponseEntity<>(cartDTO, HttpStatus.CREATED);
    }

    @Operation(
            summary = "Get all carts",
            description = "Returns a list of all carts (typically for admin or internal use)"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Carts retrieved successfully")
    })
    @GetMapping("/carts")
    public ResponseEntity<List<CartDTO>> getCarts() {

        List<CartDTO> cartDTOs = cartService.getAllCarts();
        return new ResponseEntity<>(cartDTOs, HttpStatus.OK);
    }

    @Operation(
            summary = "Get current user's cart",
            description = "Returns the shopping cart for the authenticated user"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cart retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Cart not found")
    })
    @GetMapping("/carts/users/cart")
    public ResponseEntity<CartDTO> getCartById() {

        String emailId = authUtil.loggedInEmail();
        Cart cart = cartRepository.findCartByEmail(emailId);
        Long cartId = cart.getCartId();
        CartDTO cartDTO = cartService.getCart(emailId, cartId);
        return new ResponseEntity<>(cartDTO, HttpStatus.OK);
    }

    @Operation(
            summary = "Update cart item quantity",
            description = "Updates the quantity of a product in the cart based on the given operation (add or delete)"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cart item quantity updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid operation or product")
    })
    @PutMapping("/cart/products/{productId}/quantity/{operation}")
    public ResponseEntity<CartDTO> updateCartProduct(
            @Parameter(description = "Unique identifier of the product to update", example = "10")
            @PathVariable Long productId,

            @Parameter(
                    description = "Quantity operation: use 'add' to increase or 'delete' to decrease",
                    example = "add"
            )
            @PathVariable String operation) {

        CartDTO cartDTO = cartService.updateProductQuantityInCart(
                productId,
                operation.equalsIgnoreCase("delete") ? -1 : 1
        );
        return new ResponseEntity<>(cartDTO, HttpStatus.OK);
    }

    @Operation(
            summary = "Remove product from cart",
            description = "Removes a specific product from the given cart"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Product removed from cart successfully"),
            @ApiResponse(responseCode = "404", description = "Cart or product not found")
    })
    @DeleteMapping("/carts/{cartId}/product/{productId}")
    public ResponseEntity<String> deleteProductFromCart(
            @Parameter(description = "Unique identifier of the cart", example = "7")
            @PathVariable Long cartId,

            @Parameter(description = "Unique identifier of the product to remove", example = "10")
            @PathVariable Long productId) {

        String status = cartService.deleteProductFromCart(cartId, productId);
        return new ResponseEntity<>(status, HttpStatus.OK);
    }
}
