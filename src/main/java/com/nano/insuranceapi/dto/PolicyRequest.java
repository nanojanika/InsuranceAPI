package com.nano.insuranceapi.dto;

import jakarta.validation.constraints.*;
import java.time.LocalDate;

public record PolicyRequest(
        @NotNull Long productId,
        @NotBlank String customerName,
        @NotNull @Past LocalDate customerDateOfBirth,
        @NotBlank @Email String customerEmail,
        @NotNull @Positive Double coverageAmount
) {}
