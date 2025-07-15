package com.nano.insuranceapi.service;

import com.nano.insuranceapi.dto.PolicyRequest;
import com.nano.insuranceapi.dto.PolicyResponse;
import com.nano.insuranceapi.dto.PremiumQuoteRequest;
import com.nano.insuranceapi.dto.PremiumQuoteResponse;
import com.nano.insuranceapi.model.InsuranceProduct;
import com.nano.insuranceapi.repository.InsuranceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.util.List;

@Service
public class InsuranceService {

    private final InsuranceRepository repository;

    public InsuranceService(InsuranceRepository repository) {
        this.repository = repository;
    }

    public List<InsuranceProduct> getProducts() {
        return repository.getAllProducts();
    }

    public PremiumQuoteResponse getQuote(PremiumQuoteRequest request) {
        double premium = repository.calculatePremium(
                request.productId(),
                Date.valueOf(request.customerDateOfBirth()),
                request.coverageAmount()
        );
        return new PremiumQuoteResponse(premium);
    }

    @Transactional
    public PolicyResponse createPolicy(PolicyRequest request) {
        return repository.createPolicy(
                request.productId(),
                request.customerName(),
                Date.valueOf(request.customerDateOfBirth()),
                request.customerEmail(),
                request.coverageAmount()
        );
    }
}
