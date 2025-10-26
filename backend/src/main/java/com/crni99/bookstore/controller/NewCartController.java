package com.crni99.bookstore.controller;

import java.math.BigDecimal;
import java.util.List;

import com.crni99.bookstore.service.BookService;
import org.springframework.web.bind.annotation.*;

import com.crni99.bookstore.model.Book;
import com.crni99.bookstore.service.ShoppingCartService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/carts")
@RequiredArgsConstructor
@Slf4j
public class NewCartController {

	private final ShoppingCartService cartService;
    private final BookService bookService;

	@GetMapping
	public List<Book> getCart() {
		log.info("Get cart");
		return cartService.getCart();
	}

    @PostMapping("/{id}")
    public void addProductToCart(@PathVariable("id") Long bookId) {
        log.info("Add product to cart with id {}", bookId);

        Book book = bookService.findBookById(bookId).orElse(null);

        if (book != null) {
            List<Book> cart = cartService.getCart();
            cart.add(book);
            cartService.getSession().setAttribute("cart", cart);
        } else {
            log.warn("Attempted to add non-existent book with id {}", bookId);
        }
    }

	@GetMapping("/total")
	public BigDecimal getTotalPrice() {
		log.info("Get total cart price");
		return cartService.totalPrice();
	}

	@DeleteMapping
	public void emptyCart() {
		log.info("Empty cart");
		cartService.emptyCart();;
	}

	@DeleteMapping("/{id}")
	public void deleteProductWithId(@PathVariable("id") Long bookId) {
		log.info("Delete product in cart with id {}", bookId);
		cartService.deleteProductWithId(bookId);
	}

	@GetMapping("/shipping-cost")
	public String getShippingCost() {
		log.info("Get shipping cost");
		return cartService.getshippingCosts();
	}
}
