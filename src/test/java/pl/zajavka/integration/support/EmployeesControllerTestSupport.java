package pl.zajavka.integration.support;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.springframework.http.HttpStatus;
import pl.zajavka.controller.api.EmployeesController;
import pl.zajavka.controller.dto.EmployeeDTO;
import pl.zajavka.controller.dto.EmployeesDTO;

public interface EmployeesControllerTestSupport {

    RequestSpecification requestSpecification();

    default EmployeesDTO listEmployees() {
        return requestSpecification()
                .get(EmployeesController.EMPLOYEES)
                .then()
                .statusCode(HttpStatus.OK.value())
                .and()
                .extract()
                .as(EmployeesDTO.class);
    }

    default EmployeeDTO getEmployee(final String path) {
        return requestSpecification()
                .get(path)
                .then()
                .statusCode(HttpStatus.OK.value())
                .and()
                .extract()
                .as(EmployeeDTO.class);
    }


    default EmployeeDTO getEmployeeById(final Integer employeeId) {
        return requestSpecification()
                .get(EmployeesController.EMPLOYEES + EmployeesController.EMPLOYEE_ID, employeeId)
                .then()
                .statusCode(HttpStatus.OK.value())
                .and()
                .extract()
                .as(EmployeeDTO.class);
    }

    default ExtractableResponse<Response> saveEmployee(final EmployeeDTO employeeDTO) {
        return requestSpecification()
                .body(employeeDTO)
                .post(EmployeesController.EMPLOYEES)
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .and()
                .extract();
    }

    default void updateEmployeeByPet(final Integer employeeId, final Long petId) {
        String endpoint = EmployeesController.EMPLOYEES + EmployeesController.EMPLOYEE_UPDATE_PET;
        requestSpecification()
                .patch(endpoint, employeeId, petId)
                .then()
                .statusCode(HttpStatus.OK.value());


    }
    default void deleteEmployeeById(final Integer employeeId) {
        requestSpecification()
                .delete(EmployeesController.EMPLOYEES + EmployeesController.EMPLOYEE_ID, employeeId)
                .then()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }
}
