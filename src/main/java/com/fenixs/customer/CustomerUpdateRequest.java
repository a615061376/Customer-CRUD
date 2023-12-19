package com.fenixs.customer;

public record CustomerUpdateRequest(
        String name,
        String email,
        Integer age
) {
}
