package com.nano.insuranceapi;

import com.nano.insuranceapi.repository.InsuranceRepository;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBootTest(properties = {
    "spring.datasource.url=jdbc:oracle:thin:@localhost:1521/XEPDB1",
    "spring.datasource.driver-class-name=oracle.jdbc.OracleDriver",
    "spring.datasource.username=fake_user",
    "spring.datasource.password=fake_password",
    "spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration,org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration"
})
class InsuranceapiApplicationTests {

    @MockitoBean
    private InsuranceRepository insuranceRepository;

    @Test
    void loadApplicationContext() {
        System.out.println("Insurance API Application Test - Context Loaded Successfully");
    }

}
