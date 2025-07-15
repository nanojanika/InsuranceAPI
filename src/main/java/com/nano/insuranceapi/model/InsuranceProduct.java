package com.nano.insuranceapi.model;

public record InsuranceProduct(Long productId, String productName, Double basePremium, String riskFactorLogic) {}
