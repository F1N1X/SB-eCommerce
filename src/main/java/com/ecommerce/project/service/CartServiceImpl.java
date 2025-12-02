package com.ecommerce.project.service;

import com.ecommerce.project.exceptions.APIException;
import com.ecommerce.project.exceptions.ResourceNotFoundException;
import com.ecommerce.project.model.Cart;
import com.ecommerce.project.model.CartItem;
import com.ecommerce.project.model.Product;
import com.ecommerce.project.payload.CartDTO;
import com.ecommerce.project.payload.ProductDTO;
import com.ecommerce.project.repositories.CartItemRepository;
import com.ecommerce.project.repositories.CartRepository;
import com.ecommerce.project.repositories.ProductRepository;
import com.ecommerce.project.util.AuthUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Stream;

@Service
public class CartServiceImpl implements CartService{

    @Autowired
    CartRepository cartRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    CartItemRepository cartItemRepository;

    @Autowired
    ModelMapper modelmapper;

    @Autowired
    AuthUtil authUtil;

    @Override
    public CartDTO addProductToCart(Long productId, Integer quantity) {
        Cart cart = createCart();

        Product product = productRepository.findById(productId)
                .orElseThrow( () -> new ResourceNotFoundException("Product","productId",productId));

        CartItem cartItem = cartItemRepository.findCartItemByProductIdAndCartId(
                productId,
                cart.getCartId()
        );

        if (cartItem != null)
            throw new APIException("Product " + product.getProductName() + "already exist in the cart");

        if (product.getQuantity() <= 0)
            throw new APIException("Product " + product.getProductName() + "is not available");

        if (product.getQuantity() < quantity)
            throw new APIException("Please make an order of the " + product.getProductName() +
                    "less than or equal to " + product.getQuantity() + ".");

        CartItem newCartItem = new CartItem();
        newCartItem.setProduct(product);
        newCartItem.setCart(cart);
        newCartItem.setQuantity(quantity);
        newCartItem.setProductPrice(product.getPrice());
        newCartItem.setDiscount(product.getDiscount());

        cartItemRepository.save(newCartItem);
        product.setQuantity(product.getQuantity());
        cart.setTotalPrice(cart.getTotalPrice() + (product.getSpecialPrice() * quantity));

        cartRepository.save(cart);

        CartDTO cartDTO = modelmapper.map(cart, CartDTO.class);

        List<CartItem> cartItems = cart.getCartItems();

        Stream<ProductDTO> productDTOStream = cartItems.stream()
                .map(item -> {
                    ProductDTO map = modelmapper.map(item, ProductDTO.class);
                    map.setQuantity(item.getQuantity());
                    return map;
                });

        cartDTO.setProducts(productDTOStream.toList());
        return null;
    }

    private Cart createCart() {
        Cart userCart = cartRepository.findCartByEmail(authUtil.loggedInEmail());

        if (userCart != null)
            return userCart;

        Cart cart = new Cart();
        cart.setTotalPrice(0.00);
        cart.setUser(authUtil.loggedInUser());
        return cartRepository.save(cart);
    }
}
