package com.nano.insuranceapi.controller;

import com.nano.insuranceapi.dto.PolicyRequest;
import com.nano.insuranceapi.dto.PremiumQuoteRequest;
import com.nano.insuranceapi.dto.PremiumQuoteResponse;
import com.nano.insuranceapi.model.InsuranceProduct;
import com.nano.insuranceapi.service.InsuranceService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api")
public class InsuranceController {

    private final InsuranceService service;

    public InsuranceController(InsuranceService service) {
        this.service = service;
    }

    @GetMapping("/products")
    public ResponseEntity<List<InsuranceProduct>> getProducts() {
        return ResponseEntity.ok(service.getProducts());
    }

    @PostMapping("/quotes")
    public ResponseEntity<PremiumQuoteResponse> getQuote(@Valid @RequestBody PremiumQuoteRequest request) {
        try {
            return ResponseEntity.ok(service.getQuote(request));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/policies")
    public ResponseEntity<String> createPolicy(@Valid @RequestBody PolicyRequest request) {
        try {
            String policyNumber = service.createPolicy(request);
            return ResponseEntity.created(URI.create("/api/policies/" + policyNumber)).body(policyNumber);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
