package com.nano.insuranceapi.dto;

import jakarta.validation.constraints.*;
import java.time.LocalDate;

public record PremiumQuoteRequest(
        @NotNull Long productId,
        @NotNull @Past LocalDate customerDateOfBirth,
        @NotNull @Positive Double coverageAmount
) {}
