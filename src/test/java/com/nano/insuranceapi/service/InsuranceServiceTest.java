package com.nano.insuranceapi.service;

import com.nano.insuranceapi.dto.PolicyRequest;
import com.nano.insuranceapi.dto.PolicyResponse;
import com.nano.insuranceapi.dto.PremiumQuoteRequest;
import com.nano.insuranceapi.dto.PremiumQuoteResponse;
import com.nano.insuranceapi.repository.InsuranceRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Date;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class InsuranceServiceTest {

    @Mock
    private InsuranceRepository repository;

    @InjectMocks
    private InsuranceService service;

    @Test
    void testGetQuote_success() {
        Long productId = 1L;
        String dateOfBirth = "1990-01-01";
        Double coverageAmount = 200000.0;
        Double expectedPremium = 510.0;

        when(repository.calculatePremium(eq(productId), eq(Date.valueOf(dateOfBirth)), eq(coverageAmount)))
                .thenReturn(expectedPremium);

        PremiumQuoteRequest request =
                new PremiumQuoteRequest(productId, Date.valueOf(dateOfBirth).toLocalDate(), coverageAmount);

        PremiumQuoteResponse response = service.getQuote(request);

        assertNotNull(response);
        assertEquals(expectedPremium, response.calculatedPremium());
        verify(repository).calculatePremium(productId, Date.valueOf(dateOfBirth), coverageAmount);
    }

    @Test
    void testCreatePolicy_success() {
        Long productId = 1L;
        String customerName = "John Doe";
        LocalDate dateOfBirth = LocalDate.parse("1985-05-20");
        String customerEmail = "john@example.com";
        Double coverageAmount = 100000.0;
        String expectedPolicyNumber = "POL-2025-1001";

        PolicyRequest request = new PolicyRequest(productId, customerName, dateOfBirth, customerEmail, coverageAmount);

        PolicyResponse expectedResponse = new PolicyResponse(
                expectedPolicyNumber,
                productId,
                customerName,
                dateOfBirth,
                customerEmail,
                coverageAmount,
                LocalDate.now()
        );

        when(repository.createPolicy(eq(productId), eq(customerName), eq(Date.valueOf(dateOfBirth)), eq(customerEmail),
                eq(coverageAmount))).thenReturn(expectedResponse);

        PolicyResponse actualResponse = service.createPolicy(request);

        assertNotNull(actualResponse);
        assertEquals(expectedPolicyNumber, actualResponse.policyNumber());
        assertEquals(productId, actualResponse.productId());
        assertEquals(customerName, actualResponse.customerName());
        assertEquals(dateOfBirth, actualResponse.customerDateOfBirth());
        assertEquals(customerEmail, actualResponse.customerEmail());
        assertEquals(coverageAmount, actualResponse.coverageAmount());
        verify(repository)
                .createPolicy(productId, customerName, Date.valueOf(dateOfBirth), customerEmail, coverageAmount);
    }
}
