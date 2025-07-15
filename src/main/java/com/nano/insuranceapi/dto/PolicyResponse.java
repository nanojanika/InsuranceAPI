package com.nano.insuranceapi.dto;

import java.time.LocalDate;

public record PolicyResponse(
        String policyNumber,
        Long productId,
        String customerName,
        LocalDate customerDateOfBirth,
        String customerEmail,
        Double coverageAmount,
        LocalDate creationDate
) {}