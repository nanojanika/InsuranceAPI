package com.nano.insuranceapi.repository;

import com.nano.insuranceapi.dto.PolicyResponse;
import com.nano.insuranceapi.exception.ProductNotFoundException;
import com.nano.insuranceapi.model.InsuranceProduct;
import jakarta.annotation.PostConstruct;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Repository
public class InsuranceRepository {

    private final JdbcTemplate jdbcTemplate;
    private SimpleJdbcCall calculatePremiumCall;
    private SimpleJdbcCall createPolicyProc;

    public InsuranceRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @PostConstruct
    private void init() {
        this.calculatePremiumCall = new SimpleJdbcCall(jdbcTemplate)
                .withSchemaName("INSURANCE_ADMIN")
                .withCatalogName("INSURANCE_ENGINE")
                .withFunctionName("CALCULATE_PREMIUM");

        this.createPolicyProc = new SimpleJdbcCall(jdbcTemplate)
                .withSchemaName("INSURANCE_ADMIN")
                .withCatalogName("INSURANCE_ENGINE")
                .withProcedureName("CREATE_POLICY");
    }

    public List<InsuranceProduct> getAllProducts() {
        return jdbcTemplate.query("SELECT * FROM INSURANCE_PRODUCTS", (rs, rowNum) ->
                new InsuranceProduct(
                        rs.getLong("product_id"),
                        rs.getString("product_name"),
                        rs.getDouble("base_premium"),
                        rs.getString("risk_factor_logic")
                )
        );
    }

    public double calculatePremium(Long productId, Date dob, Double coverage) {
        try {
            Number result = calculatePremiumCall.executeFunction(Number.class, Map.of(
                    "p_product_id", productId,
                    "p_date_of_birth", dob,
                    "p_coverage_amount", coverage
            ));
            return result.doubleValue();
        } catch (Exception e) {
            if (e.getMessage().contains("ORA-20001")) {
                throw new ProductNotFoundException("Product not found with ID: " + productId);
            }
            throw e;
        }
    }

    public PolicyResponse createPolicy(Long productId, String name, Date dob, String email, Double coverage) {
        try {
            Map<String, Object> result = createPolicyProc.execute(Map.of(
                    "p_product_id", productId,
                    "p_customer_name", name,
                    "p_customer_dob", dob,
                    "p_customer_email", email,
                    "p_coverage_amount", coverage
            ));

            String policyNumber = (String) result.get("p_policy_number".toUpperCase());

            return new PolicyResponse(
                    policyNumber,
                    productId,
                    name,
                    dob.toLocalDate(),
                    email,
                    coverage,
                    LocalDate.now()
            );
        } catch (Exception e) {
            if (e.getMessage().contains("ORA-20001")) {
                throw new ProductNotFoundException("Product not found with ID: " + productId);
            }
            throw e;
        }
    }
}
