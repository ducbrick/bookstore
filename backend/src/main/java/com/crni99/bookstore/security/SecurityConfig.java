package com.crni99.bookstore.security;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;

import org.springframework.web.cors.CorsConfiguration;
import java.util.Arrays;

@Configuration
public class SecurityConfig {

    private final DataSource securityDataSource;

    public SecurityConfig(DataSource securityDataSource) {
        this.securityDataSource = securityDataSource;
    }

    /** ✅ Cấu hình HttpSecurity thay cho WebSecurityConfigurerAdapter **/
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
								.cors(cors -> cors.configurationSource(request -> {
                    CorsConfiguration config = new CorsConfiguration();
                    config.setAllowedOrigins(Arrays.asList("http://localhost:8081"));
                    config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE"));
                    config.setAllowedHeaders(Arrays.asList("*"));
                    config.setAllowCredentials(true);
                    return config;
                }))
                .authorizeHttpRequests(auth -> auth
												.requestMatchers(HttpMethod.GET, "/books/**").permitAll()
												.requestMatchers("/books/**").hasAuthority("SCOPE_admin")
												.requestMatchers("/api/books").hasAuthority("SCOPE_admin")
												.requestMatchers("/api/orders/*").hasAuthority("SCOPE_admin")
												.requestMatchers("/orders").hasAuthority("SCOPE_admin")
                        .anyRequest().permitAll()
                )
								.oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()));

        // H2 console hiển thị đúng
        http.headers(headers -> headers.frameOptions(frame -> frame.disable()));

        return http.build();
    }
}
