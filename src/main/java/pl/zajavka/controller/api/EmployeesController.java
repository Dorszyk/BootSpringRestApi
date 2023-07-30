package pl.zajavka.controller.api;


import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.zajavka.controller.dao.PetDao;
import pl.zajavka.controller.dto.EmployeeDTO;
import pl.zajavka.controller.dto.EmployeesDTO;
import pl.zajavka.controller.dto.EmployeeMapper;
import pl.zajavka.infrastructure.database.entity.EmployeeEntity;
import pl.zajavka.infrastructure.database.entity.PetEntity;
import pl.zajavka.infrastructure.database.repository.EmployeeRepository;
import pl.zajavka.infrastructure.database.repository.PetRepository;
import pl.zajavka.infrastructure.petstore.Pet;

import java.math.BigDecimal;
import java.net.URI;

@RestController
@RequestMapping(EmployeesController.EMPLOYEES)
@AllArgsConstructor
class EmployeesController {

    public static final String EMPLOYEES = "/employees";
    public static final String EMPLOYEE_ID = "/{employeeId}";

    public static final String EMPLOYEE_UPDATE_SALARY = "/{employeeId}/salary";
    public static final String EMPLOYEE_ID_RESULT = "/%s";

    public static final String EMPLOYEE_UPDATE_PET = "/{employeeId}/pet/{petId}";


    private EmployeeRepository employeeRepository;
    private EmployeeMapper employeeMapper;
    private PetDao petDao;
    private PetRepository petRepository;

    @GetMapping
    public EmployeesDTO employeesList() {
        return EmployeesDTO.of(employeeRepository
                .findAll()
                .stream()
                .map(employeeMapper::map)
                .toList());
    }

    @GetMapping(
            value = EMPLOYEE_ID,
            produces = {
                    MediaType.APPLICATION_JSON_VALUE,
                    MediaType.APPLICATION_XML_VALUE
            }
    )
    public EmployeeDTO employeeDetails(@PathVariable Integer employeeId) {
        return employeeRepository.findById(employeeId)
                .map(employeeMapper::map)
                .orElseThrow(() -> new EntityNotFoundException(
                        "EmployeeEntity not found, employeeId: [%s]".formatted(employeeId)
                ));

    }

    @PostMapping
    @Transactional
    public ResponseEntity<EmployeeDTO> addEmployee(
            @Valid @RequestBody EmployeeDTO employeeDTO
    ) {
        EmployeeEntity employeeEntity = EmployeeEntity.builder()
                .name(employeeDTO.getName())
                .surname(employeeDTO.getSurname())
                .salary(employeeDTO.getSalary())
                .phone(employeeDTO.getPhone())
                .email(employeeDTO.getEmail())
                .build();
        EmployeeEntity create = employeeRepository.save(employeeEntity);
        return ResponseEntity
                .created(URI.create(EMPLOYEES + EMPLOYEE_ID_RESULT.formatted(create.getEmployeeId())))
                .build();
    }

    @PutMapping(EMPLOYEE_ID)
    @Transactional
    public ResponseEntity<?> updateEmployee(
            @PathVariable Integer employeeId,
            @Valid @RequestBody EmployeeDTO employeeDTO
    ) {
        EmployeeEntity existingEmployee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "EmployeeEntity not found, employeeId: [%s]".formatted(employeeId)
                ));
        existingEmployee.setName(employeeDTO.getName());
        existingEmployee.setSurname(employeeDTO.getSurname());
        existingEmployee.setSalary(employeeDTO.getSalary());
        existingEmployee.setPhone(employeeDTO.getPhone());
        existingEmployee.setEmail(employeeDTO.getEmail());
        employeeRepository.save(existingEmployee);

        return ResponseEntity.ok().build();
    }

    //curl -i -X DELETE http://localhost:8190/restApi/employees/22

    @DeleteMapping(EMPLOYEE_ID)
    @Transactional
    public ResponseEntity<?> deleteEmployee(
            @PathVariable Integer employeeId
    ) {
        EmployeeEntity existingEmployee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "EmployeeEntity not found, employeeId: [%s]".formatted(employeeId)
                ));
        employeeRepository.deleteById(existingEmployee.getEmployeeId());
        return ResponseEntity.noContent().build();
    }

    //curl -i -X PATCH http://localhost:8190/restApi/employees/40/salary?newSalary=19221.00
    @PatchMapping(EMPLOYEE_UPDATE_SALARY)
    public ResponseEntity<?> updateEmployeeSalary(
            @PathVariable Integer employeeId,
            @RequestParam BigDecimal newSalary
    ) {
        EmployeeEntity existingEmployee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "EmployeeEntity not found, employeeId: [%s]".formatted(employeeId)
                ));
        existingEmployee.setSalary(newSalary);
        EmployeeEntity updatedEmployee = employeeRepository.save(existingEmployee);
        return ResponseEntity.ok().build();
    }

    //curl -i --location --request PATCH 'http://localhost:8190/restApi/employees/2/pet/10'
    @PatchMapping(EMPLOYEE_UPDATE_PET)
    @Transactional
    public ResponseEntity<?> updateEmployeePet(
            @PathVariable Integer employeeId,
            @PathVariable Long petId
           ) {
        EmployeeEntity existingEmployee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "EmployeeEntity not found, employeeId: [%s]".formatted(employeeId)
                ));

        Pet petFromStore= petDao.getPet(petId)
                .orElseThrow(() -> new RuntimeException(
                        "Pet not found, petId: [%s]".formatted(petId)
                ));

        PetEntity newPet = PetEntity.builder()
                .petStorePetId(petFromStore.getId())
                .name(petFromStore.getName())
                .status(petFromStore.getStatus())
                .employee(existingEmployee)
                .build();
        petRepository.save(newPet);

        return ResponseEntity.ok().build();
    }

    //curl -i -H "Accept: application/json" -H "httpStatus: 204" -X GET http://localhost:8190/restApi/employees/test-header
    //curl -i -X GET http://localhost:8190/restApi/employees/test-header
    @GetMapping(value = "test-header")
    public ResponseEntity<String> testHeader(
            @RequestHeader(value = HttpHeaders.ACCEPT, defaultValue = "application/json") MediaType accept,
            @RequestHeader(value = "httpStatus", defaultValue = "200", required = true) int httpStatus
    ) {
        return ResponseEntity
                .status(httpStatus)
                .header("x-my-header", accept.toString())
                .body("Accepted: " + accept);
    }
}
