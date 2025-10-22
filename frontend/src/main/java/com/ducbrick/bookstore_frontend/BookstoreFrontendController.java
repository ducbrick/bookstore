package com.ducbrick.bookstore_frontend;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class BookstoreFrontendController {

    @GetMapping("/login")
    public String login() {
        return "login.html";
    }
}