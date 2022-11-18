package com.anagracetech.customer;

public record CustomerRegisterationRequest(
        String firstName,
        String lastName,
        String email) {
}
