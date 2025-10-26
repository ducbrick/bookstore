package com.crni99.bookstore.controller;

import com.crni99.bookstore.dto.OrderRequest;
import com.crni99.bookstore.model.Book;
import com.crni99.bookstore.service.BillingService;
import com.crni99.bookstore.service.EmailService;
import com.crni99.bookstore.service.ShoppingCartService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/checkout") // Changed to match your new checkout.html JavaScript
public class NewCheckoutController {

    private final BillingService billingService;
    private final EmailService emailService;
    private final ShoppingCartService shoppingCartService;

    public NewCheckoutController(BillingService billingService, EmailService emailService,
                              ShoppingCartService shoppingCartService) {
        this.billingService = billingService;
        this.emailService = emailService;
        this.shoppingCartService = shoppingCartService;
    }

    /**
     * This method handles the POST request from your new checkout.html.
     * It receives the customer and payment data as JSON.
     */
    @PostMapping("/placeOrder")
    public ResponseEntity<?> placeOrder(@Valid @RequestBody OrderRequest orderRequest, BindingResult result) {

        // 1. Handle Validation Errors
        if (result.hasErrors()) {
            // Convert errors to a simple map to send back as JSON
            Map<String, String> errors = result.getFieldErrors().stream()
                    .collect(Collectors.toMap(
                            fieldError -> fieldError.getField(),
                            fieldError -> fieldError.getDefaultMessage()
                    ));
            return ResponseEntity.badRequest().body(errors);
        }

        // 2. Get cart from session
        List<Book> cart = shoppingCartService.getCart();
        if (cart.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Your cart is empty."));
        }

        // 3. Process the order using your existing services
        // Note: You may need to update createOrder to also accept paymentMethod
        billingService.createOrder(orderRequest.customer(), cart);

        // 4. Send email
        emailService.sendEmail(
                orderRequest.customer().getEmail(),
                "bookstore - Order Confirmation",
                "Your order has been confirmed."
        );

        // 5. Empty the cart
        shoppingCartService.emptyCart();

        // 6. Send a success (200 OK) response
        return ResponseEntity.ok().build();
    }
}