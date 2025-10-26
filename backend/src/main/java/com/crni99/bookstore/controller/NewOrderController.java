package com.crni99.bookstore.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.crni99.bookstore.model.CustomerBooks;
import com.crni99.bookstore.service.BillingService;

import lombok.RequiredArgsConstructor;

@RequestMapping("/orders")
@RequiredArgsConstructor
public class NewOrderController {
	private final BillingService billingService;

	@GetMapping
	public Page<CustomerBooks> searchOrdersPaginated(
		@RequestParam(defaultValue = "0") int page,
		@RequestParam(defaultValue = "10") int size,
		@RequestParam(defaultValue = "") String term
	) {
		PageRequest pageable = PageRequest.of(page, size);
		return billingService.findPaginated(pageable, term);
	}
	
}
