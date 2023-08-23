package pl.zajavka.integration;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import pl.zajavka.controller.dto.EmployeeDTO;
import pl.zajavka.controller.dto.EmployeesDTO;
import pl.zajavka.integration.configuration.RestAssuredIntegrationTestBase;
import pl.zajavka.integration.support.EmployeesControllerTestSupport;
import pl.zajavka.integration.support.WireMockTestSupport;
import pl.zajavka.util.DtoFixtures;

import java.util.Set;
import java.util.regex.Pattern;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class EmployeesControllerRestAssuredIT
        extends RestAssuredIntegrationTestBase
        implements EmployeesControllerTestSupport, WireMockTestSupport {

    @Test
    void thatEmployeesListCanBeRetrievedCorrectly() {
        //given
        EmployeeDTO employee1 = DtoFixtures.someEmployee1();
        EmployeeDTO employee2 = DtoFixtures.someEmployee2();

        //when
        saveEmployee(employee1);
        saveEmployee(employee2);

        EmployeesDTO employeesDTO = listEmployees();
        //then
        Assertions.assertThat(employeesDTO.getEmployees())
                .usingRecursiveFieldByFieldElementComparatorIgnoringFields("employeeId")
                .containsAnyOf(employee1.withPets(Set.of()), employee2.withPets(Set.of()));
    }

    @Test
    void thatEmployeeCanBeCreatedCorrectly() {
        //given
        EmployeeDTO employee1 = DtoFixtures.someEmployee1();

        //when
        ExtractableResponse<Response> response = saveEmployee(employee1);

        //then

        String responseAsString = response.body().asString();
        assertThat(responseAsString).isEmpty();
        assertThat(response.headers().get("Location").getValue())
                .matches(Pattern.compile("/employees/\\d"));
    }

    @Test
    void thatCreatedEmployeeCanBeRetrievedCorrectly() {
        //given
        EmployeeDTO employee1 = DtoFixtures.someEmployee1();

        //when
        ExtractableResponse<Response> response = saveEmployee(employee1);
        String employeeDetailsPath = response.headers().get("Location").getValue();

        EmployeeDTO employee = getEmployee(employeeDetailsPath);

        //then
        assertThat(employee)
                .usingRecursiveComparison()
                .ignoringFields("employeeId")
                .isEqualTo(employee1.withPets(Set.of()));
    }

    @Test
    void thatEmployeesCanBeUpdateWithPetCorrectly() {
        //given
        long petId = 4;
        EmployeeDTO employee1 = DtoFixtures.someEmployee1();
        ExtractableResponse<Response> response = saveEmployee(employee1);
        String employeeDetailsPath = response.headers().get("Location").getValue();
        EmployeeDTO retrievedEmployee = getEmployee(employeeDetailsPath);

        stubForPet(wireMockServer, petId);

        //when
        updateEmployeeByPet(retrievedEmployee.getEmployeeId(), petId);

        //then
        EmployeeDTO employeeWithPet = getEmployeeById(retrievedEmployee.getEmployeeId());

        assertThat(employeeWithPet)
                .usingRecursiveComparison()
                .ignoringFields("employeeId", "petId")
                .isEqualTo(employee1.withPets(Set.of(DtoFixtures.somePet())));

    }

}
