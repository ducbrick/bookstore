package com.ducbrick.bookstore_frontend;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class BookstoreFrontendController {

    @GetMapping("/")
    public String home() {
        return "index.html";
    }

    @GetMapping("/login")
    public String login() {
        return "login.html";
    }

    @GetMapping("/cart")
    public String cart() {
        return "cart.html";
    }

    @GetMapping("/orders")
    public String orders() {
        return "orders.html";
    }

    @GetMapping("/form")
    public String form() {
        return "form.html";
    }

    @GetMapping("/list")
    public String list() {
        return "list.html";
    }
}