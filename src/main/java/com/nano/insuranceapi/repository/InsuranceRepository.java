package com.nano.insuranceapi.repository;

import com.nano.insuranceapi.model.InsuranceProduct;
import jakarta.annotation.PostConstruct;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;

import java.sql.Date;
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
                .withCatalogName("INSURANCE_ADMIN_PKG")
                .withFunctionName("CALCULATE_PREMIUM");

        this.createPolicyProc = new SimpleJdbcCall(jdbcTemplate)
                .withSchemaName("INSURANCE_ADMIN")
                .withCatalogName("INSURANCE_ADMIN_PKG")
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
                throw new IllegalArgumentException("Invalid product ID");
            }
            throw e;
        }
    }

    public String createPolicy(Long productId, String name, Date dob, String email, Double coverage) {
        Map<String, Object> result = createPolicyProc.execute(Map.of(
                "p_product_id", productId,
                "p_customer_name", name,
                "p_customer_dob", dob,
                "p_customer_email", email,
                "p_coverage_amount", coverage
        ));
        return (String) result.get("p_policy_number");
    }
}

