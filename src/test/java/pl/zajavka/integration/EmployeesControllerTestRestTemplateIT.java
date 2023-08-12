package pl.zajavka.integration;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;
import pl.zajavka.controller.dto.EmployeesDTO;
import pl.zajavka.integration.configuration.AbstractIntegrationTest;
import pl.zajavka.util.DtoFixtures;


public class EmployeesControllerTestRestTemplateIT extends AbstractIntegrationTest {


    @LocalServerPort
    private int port;
    @Autowired
    public final TestRestTemplate testRestTemplate;
    @Autowired
    public EmployeesControllerTestRestTemplateIT(TestRestTemplate testRestTemplate) {
        this.testRestTemplate = testRestTemplate;
    }


    @Test
    public void thatEmployeesListingWorksCorrectly() {

        String url = "http://localhost:%s/restApi/employees".formatted(port);

        this.testRestTemplate.postForEntity(url, DtoFixtures.someEmployee1(), EmployeesDTO.class);

        ResponseEntity<EmployeesDTO> result = this.testRestTemplate.getForEntity(url, EmployeesDTO.class);
        EmployeesDTO body = result.getBody();
        Assertions.assertThat(body).isNotNull();
        Assertions.assertThat(body.getEmployees()).hasSizeGreaterThan(0);
    }

}
