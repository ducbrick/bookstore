    package com.crni99.bookstore.controller;

    import com.crni99.bookstore.model.Book;
    import com.crni99.bookstore.model.Customer;
    import com.crni99.bookstore.model.CustomerBooks;
    import com.crni99.bookstore.service.BillingService;
    import org.springframework.data.domain.Page;
    import org.springframework.data.domain.PageRequest;
    import org.springframework.http.HttpStatus;
    import org.springframework.http.ResponseEntity;
    import org.springframework.web.bind.annotation.*;

    import java.util.Collections;
    import java.util.List;

    @RestController
    @RequestMapping("/api")
    public class NewBillingController {
        private final BillingService billingService;

        public NewBillingController(BillingService billingService) {
            this.billingService = billingService;
        }

        @GetMapping("/books")
        public Page<CustomerBooks> getPaginatedBooks(
                @RequestParam(defaultValue = "0") int page,
                @RequestParam(defaultValue = "10") int size,
                @RequestParam(defaultValue = "") String term
        ) {
            PageRequest pageable = PageRequest.of(page, size);
            return billingService.findPaginated(pageable, term);
        }

        @PostMapping("/orders")
        public ResponseEntity<String> createOrder(@RequestBody OrderRequest request) {
            billingService.createOrder(request.getCustomer(), Collections.singletonList((Book) request.getBooks()));
            return ResponseEntity.status(HttpStatus.CREATED).body("Order created successfully");
        }

        @GetMapping("/orders/{customerId}")
        public ResponseEntity<List<CustomerBooks>> getOrdersByCustomer(@PathVariable Long customerId) {
            List<CustomerBooks> orders = billingService.findOrdersByCustomerId(customerId);
            if (orders.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(orders);
        }

        public static class OrderRequest {
            private Customer customer;
            private List<Book> books;

            public Customer getCustomer() {
                return customer;
            }
            public void setCustomer(Customer customer) {
                this.customer = customer;
            }

            public List<Book> getBooks() {
                return books;
            }
            public void setBooks(List<Book> books) {
                this.books = books;
            }
        }

    }
