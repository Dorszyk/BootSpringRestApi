package pl.zajavka.controller.api;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import pl.zajavka.controller.dao.PetDao;
import pl.zajavka.controller.dto.EmployeeDTO;
import pl.zajavka.controller.dto.EmployeeMapper;
import pl.zajavka.controller.util.DtoFixtures;
import pl.zajavka.controller.util.EntityFixtures;
import pl.zajavka.infrastructure.database.entity.EmployeeEntity;
import pl.zajavka.infrastructure.database.repository.EmployeeRepository;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class EmployeesControllerMockitoTest {
    @Mock
    private EmployeeRepository employeeRepository;
    @Mock
    private EmployeeMapper employeeMapper;
    @Mock
    private PetDao petDao;

    @InjectMocks
    private EmployeesController employeesController;

    @Test
    void thatRetrievingEmployeeWorksCorrectly(){
        // given
        Integer employeeId = 10;
        EmployeeEntity employeeEntity = EntityFixtures.someEmployee1();
        when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(employeeEntity));
        when(employeeMapper.map(employeeEntity)).thenReturn(DtoFixtures.someEmployee1());

        //when
        EmployeeDTO result = employeesController.employeeDetails(employeeId);

        //then
        assertThat(result).isEqualTo(DtoFixtures.someEmployee1());
    }

    @Test
    void thatSavingEmployeeWorksCorrectly(){
        //given
        when(employeeRepository.save(any(EmployeeEntity.class)))
                .thenReturn(EntityFixtures.someEmployee1().withEmployeeId(123));

        // when
        ResponseEntity<?> result = employeesController.addEmployee(DtoFixtures.someEmployee1());

        //then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @Test
    void testGetEmployeeDetails() {
        EmployeeEntity entity = new EmployeeEntity();
        EmployeeDTO dto = new EmployeeDTO();
        when(employeeRepository.findById(any())).thenReturn(Optional.of(entity));
        when(employeeMapper.map(any())).thenReturn(dto);

        EmployeeDTO result = employeesController.employeeDetails(1);
        assertThat(result).isSameAs(dto);
    }

    @Test
    void testAddEmployee() {
        EmployeeDTO dto = new EmployeeDTO();
        EmployeeEntity entity = new EmployeeEntity();
        entity.setEmployeeId(1);
        when(employeeRepository.save(any())).thenReturn(entity);

        ResponseEntity<EmployeeDTO> result = employeesController.addEmployee(dto);
        assertThat(result.getStatusCodeValue()).isEqualTo(201);
        assertThat(result.getHeaders().getLocation().getPath()).endsWith("/1");
    }

    @Test
    void testUpdateEmployeeSalary() {
        EmployeeEntity entity = new EmployeeEntity();
        when(employeeRepository.findById(any())).thenReturn(Optional.of(entity));
        when(employeeRepository.save(any())).thenReturn(entity);

        ResponseEntity<?> result = employeesController.updateEmployeeSalary(1, BigDecimal.valueOf(1000));
        assertThat(result.getStatusCodeValue()).isEqualTo(200);
    }

}
