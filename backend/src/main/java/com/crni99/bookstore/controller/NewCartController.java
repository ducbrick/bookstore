package com.crni99.bookstore.controller;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.crni99.bookstore.model.Book;
import com.crni99.bookstore.model.CustomerBooks;
import com.crni99.bookstore.service.BillingService;
import com.crni99.bookstore.service.BookService;
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
	private final BillingService billingService;

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

	@PostMapping("/{id}")
	public String addToCart(@PathVariable("id") Long id) {
		List<Book> cart = cartService.getCart();
		Book book = bookService.findBookById(id).get();
		if (book != null) {
			cart.add(book);
			log.info("Added book with id {} to cart", id);
		}
		cartService.getSession().setAttribute("cart", cart);
		return "Added book successfully!";
	}

	
}
