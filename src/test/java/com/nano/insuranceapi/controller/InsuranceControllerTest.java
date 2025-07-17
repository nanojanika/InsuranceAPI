package com.nano.insuranceapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nano.insuranceapi.dto.PolicyRequest;
import com.nano.insuranceapi.dto.PolicyResponse;
import com.nano.insuranceapi.dto.PremiumQuoteRequest;
import com.nano.insuranceapi.dto.PremiumQuoteResponse;
import com.nano.insuranceapi.exception.ProductNotFoundException;
import com.nano.insuranceapi.model.InsuranceProduct;
import com.nano.insuranceapi.service.InsuranceService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = InsuranceController.class)
public class InsuranceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private InsuranceService service;

    @Test
    void getProducts_shouldReturnListOfProducts() throws Exception {
        // Arrange
        List<InsuranceProduct> products = List.of(
                new InsuranceProduct(1L, "Basic Health Plan", 500.0, "AGE_BASED"),
                new InsuranceProduct(2L, "Travel Lite Plan", 300.0, "FLAT_RATE")
        );
        when(service.getProducts()).thenReturn(products);

        // Act & Assert
        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].productId", is(1)))
                .andExpect(jsonPath("$[0].productName", is("Basic Health Plan")))
                .andExpect(jsonPath("$[0].basePremium", is(500.0)))
                .andExpect(jsonPath("$[0].riskFactorLogic", is("AGE_BASED")))
                .andExpect(jsonPath("$[1].productId", is(2)))
                .andExpect(jsonPath("$[1].productName", is("Travel Lite Plan")))
                .andExpect(jsonPath("$[1].basePremium", is(300.0)))
                .andExpect(jsonPath("$[1].riskFactorLogic", is("FLAT_RATE")));
    }

    @Test
    void getQuote_withValidRequest_shouldReturnQuote() throws Exception {
        // Arrange
        PremiumQuoteRequest request = new PremiumQuoteRequest(1L, LocalDate.of(1990, 1, 1), 200000.0);
        PremiumQuoteResponse response = new PremiumQuoteResponse(510.0);

        when(service.getQuote(any(PremiumQuoteRequest.class))).thenReturn(response);

        // Act & Assert
        mockMvc.perform(post("/api/quotes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.calculatedPremium", is(510.0)));
    }

    @Test
    void getQuote_withInvalidProduct_shouldReturnNotFound() throws Exception {
        // Arrange
        PremiumQuoteRequest request = new PremiumQuoteRequest(999L, LocalDate.of(1990, 1, 1), 200000.0);

        when(service.getQuote(any(PremiumQuoteRequest.class)))
                .thenThrow(new ProductNotFoundException("Product not found with ID: 999"));

        // Act & Assert
        mockMvc.perform(post("/api/quotes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(content().string(containsString("Product not found with ID: 999")));
    }

    @Test
    void getQuote_withInvalidRequest_shouldReturnBadRequest() throws Exception {
        // Arrange
        String invalidRequest = "{\"productId\": null, \"customerDateOfBirth\": \"1990-01-01\", \"coverageAmount\": 200000.0}";

        // Act & Assert
        mockMvc.perform(post("/api/quotes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidRequest))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createPolicy_withValidRequest_shouldReturnCreated() throws Exception {
        // Arrange
        PolicyRequest request = new PolicyRequest(
                1L, 
                "John Doe", 
                LocalDate.of(1985, 5, 20), 
                "john@example.com", 
                100000.0
        );

        PolicyResponse response = new PolicyResponse(
                "POL-2025-1001",
                1L,
                "John Doe",
                LocalDate.of(1985, 5, 20),
                "john@example.com",
                100000.0,
                LocalDate.now()
        );

        when(service.createPolicy(any(PolicyRequest.class))).thenReturn(response);

        // Act & Assert
        mockMvc.perform(post("/api/policies")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.policyNumber", is("POL-2025-1001")))
                .andExpect(jsonPath("$.productId", is(1)))
                .andExpect(jsonPath("$.customerName", is("John Doe")))
                .andExpect(jsonPath("$.customerEmail", is("john@example.com")))
                .andExpect(jsonPath("$.coverageAmount", is(100000.0)));
    }

    @Test
    void createPolicy_withInvalidProduct_shouldReturnNotFound() throws Exception {
        // Arrange
        PolicyRequest request = new PolicyRequest(
                999L, 
                "John Doe", 
                LocalDate.of(1985, 5, 20), 
                "john@example.com", 
                100000.0
        );

        when(service.createPolicy(any(PolicyRequest.class)))
                .thenThrow(new ProductNotFoundException("Product not found with ID: 999"));

        // Act & Assert
        mockMvc.perform(post("/api/policies")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(content().string(containsString("Product not found with ID: 999")));
    }

    @Test
    void createPolicy_withInvalidRequest_shouldReturnBadRequest() throws Exception {
        // Arrange
        String invalidRequest = "{\"productId\": 1, \"customerName\": \"\", \"customerDateOfBirth\": \"1985-05-20\", \"customerEmail\": \"invalid-email\", \"coverageAmount\": 100000.0}";

        // Act & Assert
        mockMvc.perform(post("/api/policies")
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidRequest))
                .andExpect(status().isBadRequest());
    }
}
