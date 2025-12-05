package com.ducbrick.bookstore_frontend;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class BookstoreFrontendController {

    private final RestTemplate restTemplate;

    @Value("${auth0.domain}")
    private String auth0Domain;

    @Value("${auth0.client-id}")
    private String clientId;

    @Value("${auth0.client-secret}")
    private String clientSecret;

    @Value("${auth0.redirect-uri}")
    private String redirectUri;

    public BookstoreFrontendController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

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

    @GetMapping("/oops")
    public String error() {
        return "error.html";
    }

    @GetMapping("/list")
    public String list() {
        return "list.html";
    }

    @GetMapping("/book")
    public String book() {
        return "list.html";
    }

    @GetMapping("/code")
    public String handleAuthCallback(@RequestParam("code") String code, HttpServletResponse response) {
        try {

            // Set headers
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            // Create request body
            String requestBody = String.format(
                "{\"grant_type\":\"authorization_code\",\"client_id\":\"%s\",\"client_secret\":\"%s\",\"code\":\"%s\",\"redirect_uri\":\"%s\"}",
                clientId, clientSecret, code, redirectUri
            );

            // Create HTTP entity
            HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

            // Make POST request to Auth0
            String tokenUrl = auth0Domain + "/oauth/token";
            ResponseEntity<String> tokenResponse = restTemplate.postForEntity(tokenUrl, entity, String.class);

            if (tokenResponse.getStatusCode().is2xxSuccessful()) {
                // Parse JSON response to get access_token
                String responseBody = tokenResponse.getBody();
                // Simple JSON parsing - extract access_token value
                int tokenStart = responseBody.indexOf("\"access_token\":\"") + 16;
                int tokenEnd = responseBody.indexOf("\"", tokenStart);
                String accessToken = responseBody.substring(tokenStart, tokenEnd);

                // Set cookie with access token (using raw header to allow spaces in name)
                response.addHeader("Set-Cookie", "access token=" + accessToken + "; Path=/");

                // Redirect to home page
                return "redirect:/";
            } else {
                // Token exchange failed, redirect to login
                return "redirect:/login";
            }
        } catch (Exception e) {
            // Error occurred, redirect to login
            return "redirect:/login";
        }
    }
}
