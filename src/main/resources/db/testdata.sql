-- insert test data ( AGE_BASED product & FLAT_RATE product):
INSERT INTO insurance_admin.INSURANCE_PRODUCTS (product_name, base_premium, risk_factor_logic)
VALUES ('Basic Health Plan',500.00,'AGE_BASED');

INSERT INTO insurance_admin.INSURANCE_PRODUCTS (product_name, base_premium, risk_factor_logic)
VALUES ('Travel Lite Plan',300.00,'FLAT_RATE');

SELECT * FROM insurance_admin.INSURANCE_PRODUCTS;

-- call procedure:
DECLARE
    v_policy_number VARCHAR2(30);
BEGIN
    insurance_admin_pkg.create_policy(
            p_product_id      => 1,
            p_customer_name   => 'Firstname Lastname',
            p_customer_dob    => TO_DATE('1990-05-10', 'YYYY-MM-DD'),
            p_customer_email  => 'firstname.lastname@insurance.com',
            p_coverage_amount => 200000,
            p_policy_number   => v_policy_number
    );

    DBMS_OUTPUT.PUT_LINE('Created Policy Number: ' || v_policy_number);
END;
