package com.crni99.bookstore.dto;

import com.crni99.bookstore.model.Customer;
import javax.validation.Valid;

/**
 * This record acts as a Data Transfer Object (DTO) to receive the
 * JSON payload from the static checkout.html form.
 */
public record OrderRequest(

        @Valid // This enables validation on the nested Customer object
        Customer customer,

        String paymentMethod
) {}