package pl.zajavka.controller.api;


import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import pl.zajavka.controller.dao.PetDao;
import pl.zajavka.controller.dto.EmployeeDTO;
import pl.zajavka.controller.dto.EmployeeMapper;
import pl.zajavka.controller.util.DtoFixtures;
import pl.zajavka.controller.util.EntityFixtures;
import pl.zajavka.infrastructure.database.entity.EmployeeEntity;
import pl.zajavka.infrastructure.database.repository.EmployeeRepository;
import pl.zajavka.infrastructure.database.repository.PetRepository;

import java.math.BigDecimal;
import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;



@WebMvcTest(controllers = EmployeesController.class)
public class EmployeesControllerWebMvcTest {

    @Autowired
    private MockMvc mockMvc;


    @MockBean
    private EmployeeRepository employeeRepository;

    @MockBean
    private EmployeeMapper employeeMapper;

    @MockBean
    private PetDao petDao;

    @MockBean
    private PetRepository petRepository;

    @Test
    public void thatEmployeeCanBeRetrieved() throws Exception {
        //given
        int employeeId = 123;
        EmployeeEntity employeeEntity = EntityFixtures.someEmployee1().withEmployeeId(employeeId);
        EmployeeDTO employeeDTO = DtoFixtures.someEmployee1().withEmployeeId(employeeId);

        when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(employeeEntity));
        when(employeeMapper.map(any(EmployeeEntity.class))).thenReturn(employeeDTO);

        // when, then
        String endpoint = EmployeesController.EMPLOYEES + EmployeesController.EMPLOYEE_ID;
        mockMvc.perform(get(endpoint, employeeId)).andExpect(content().contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andExpect(jsonPath("$.employeeId", is(employeeDTO.getEmployeeId()))).andExpect(jsonPath("$.name", is(employeeDTO.getName()))).andExpect(jsonPath("$.surname", is(employeeDTO.getSurname()))).andExpect(jsonPath("$.salary", is(employeeDTO.getSalary()), BigDecimal.class)).andExpect(jsonPath("$.phone", is(employeeDTO.getPhone()))).andExpect(jsonPath("$.email", is(employeeDTO.getEmail())));

    }
   @Test
   void thatEmailValidationWorksCorrectly() throws Exception {
        //given
        final var request = """
                {
                    "email": "badWpPl"
                }
                """;

        //when, then
        mockMvc.perform(
                post(EmployeesController.EMPLOYEES)
                .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorId", Matchers.notNullValue()));
    }

}
