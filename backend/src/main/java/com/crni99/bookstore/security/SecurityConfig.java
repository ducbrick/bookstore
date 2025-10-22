package com.crni99.bookstore.security;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;

@Configuration
public class SecurityConfig {

    private final DataSource securityDataSource;

    public SecurityConfig(DataSource securityDataSource) {
        this.securityDataSource = securityDataSource;
    }

    /** ✅ Cấu hình Authentication dùng JDBC **/
    @Bean
    public UserDetailsManager userDetailsManager() {
        return new JdbcUserDetailsManager(securityDataSource);
    }

    /** ✅ Dùng Delegating Password Encoder để hỗ trợ {noop} passwords **/
    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    /** ✅ Cấu hình HttpSecurity thay cho WebSecurityConfigurerAdapter **/
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
								.cors(cors -> cors.disable())
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll()
                )
								.formLogin(form -> form
												.loginProcessingUrl("/login")
												.successHandler((req, res, auth) -> {
																res.setStatus(HttpServletResponse.SC_OK);
																res.setContentType("application/json");
																res.getWriter().write("{\"message\": \"Login successful\"}");
												})
												.failureHandler((req, res, ex) -> {
																res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
																res.setContentType("application/json");
																res.getWriter().write("{\"error\": \"Invalid credentials\"}");
												})
								)
								.logout(logout -> logout
												.logoutUrl("/logout")
												.logoutSuccessHandler((req, res, auth) -> {
																res.setStatus(HttpServletResponse.SC_OK);
																res.setContentType("application/json");
																res.getWriter().write("{\"message\": \"Logged out successfully\"}");
												})
								);

        // H2 console hiển thị đúng
        http.headers(headers -> headers.frameOptions(frame -> frame.disable()));

        return http.build();
    }
}
