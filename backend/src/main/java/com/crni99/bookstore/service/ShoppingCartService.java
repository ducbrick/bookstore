package com.crni99.bookstore.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import jakarta.servlet.http.HttpSession;

import com.crni99.bookstore.model.Book;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ShoppingCartService {

	@Value("${shipping.costs}")
	private String shippingCosts;
	private final HttpSession session;

	public ShoppingCartService(HttpSession session) {
		this.session = session;
	}

    @SuppressWarnings("unchecked")
	public List<Book> getCart() {
		List<Book> cart = (List<Book>) session.getAttribute("cart");
		if (cart == null) {
			cart = new ArrayList<>();
            session.setAttribute("cart", cart);
		}
		return cart;
	}

	public BigDecimal totalPrice() {
		BigDecimal shipping = new BigDecimal(shippingCosts);
        BigDecimal itemTotal = getCart().stream()
                .map(Book::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return itemTotal.add(shipping);
	}

	public void emptyCart() {
        session.removeAttribute("cart");
	}

	public void deleteProductWithId(Long bookId) {
		List<Book> cart = getCart();
        cart.removeIf(book -> book.getId().equals(bookId));
        session.setAttribute("cart", cart);
	}

	public String getshippingCosts() {
		return shippingCosts;
	}

	public HttpSession getSession() {
		return session;
	}

}
