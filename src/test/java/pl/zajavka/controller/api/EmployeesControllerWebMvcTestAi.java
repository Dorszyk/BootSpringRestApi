package pl.zajavka.controller.api;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.server.ResponseStatusException;
import pl.zajavka.controller.dao.PetDao;
import pl.zajavka.controller.dto.EmployeeDTO;
import pl.zajavka.controller.dto.EmployeeMapper;
import pl.zajavka.infrastructure.database.entity.EmployeeEntity;
import pl.zajavka.infrastructure.database.entity.PetEntity;
import pl.zajavka.infrastructure.database.repository.EmployeeRepository;
import pl.zajavka.infrastructure.database.repository.PetRepository;
import pl.zajavka.infrastructure.petstore.Pet;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(EmployeesController.class)
public class EmployeesControllerWebMvcTestAi {

    private EmployeeDTO mockEmployeeDTO;
    private EmployeeEntity mockEmployeeEntity;

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

    @BeforeEach
    public void setUp() {
        mockEmployeeDTO = new EmployeeDTO();
        mockEmployeeDTO.setName("John");
        mockEmployeeDTO.setSurname("Doe");
        mockEmployeeDTO.setSalary(new BigDecimal("1000"));
        mockEmployeeDTO.setPhone("+12 345 678 999");
        mockEmployeeDTO.setEmail("john.doe@example.com");

        mockEmployeeEntity = new EmployeeEntity();
        mockEmployeeEntity.setEmployeeId(1);
        mockEmployeeEntity.setName("John");
        mockEmployeeEntity.setSurname("Doe");
        mockEmployeeEntity.setSalary(new BigDecimal("1000"));
        mockEmployeeEntity.setPhone("+12 345 678 999");
        mockEmployeeEntity.setEmail("john.doe@example.com");
    }

    @Test
    public void testEmployeesList() throws Exception {
        when(employeeRepository.findAll()).thenReturn(List.of(mockEmployeeEntity));
        when(employeeMapper.map(mockEmployeeEntity)).thenReturn(mockEmployeeDTO);

        String expectedJson = "{ \"employees\": [ { \"name\": \"John\", \"surname\": \"Doe\", \"salary\": 1000, \"phone\": \"+12 345 678 999\", \"email\": \"john.doe@example.com\" } ] }";

        mockMvc.perform(MockMvcRequestBuilders.get(EmployeesController.EMPLOYEES))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedJson));

        verify(employeeRepository).findAll();
        verify(employeeMapper).map(mockEmployeeEntity);
    }

    @Test
    public void testEmployeeDetails() throws Exception {
        when(employeeRepository.findById(1)).thenReturn(Optional.of(mockEmployeeEntity));
        when(employeeMapper.map(mockEmployeeEntity)).thenReturn(mockEmployeeDTO);

        mockMvc.perform(MockMvcRequestBuilders.get(EmployeesController.EMPLOYEES + "/1"))
                .andExpect(status().isOk())
                .andExpect(content().json("{'name':'John','surname':'Doe','salary':1000,'phone':'+12 345 678 999','email':'john.doe@example.com'}"));

        verify(employeeRepository).findById(1);
        verify(employeeMapper).map(mockEmployeeEntity);
    }

    @Test
    public void testEmployeeDetailsNotFound() throws Exception {
        when(employeeRepository.findById(1)).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders.get(EmployeesController.EMPLOYEES + "/1"))
                .andExpect(status().isNotFound());

        verify(employeeRepository).findById(1);
    }

    @Test
    public void testAddEmployee() throws Exception {
        when(employeeRepository.save(any(EmployeeEntity.class))).thenReturn(mockEmployeeEntity);

        mockMvc.perform(MockMvcRequestBuilders.post(EmployeesController.EMPLOYEES)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"John\",\"surname\":\"Doe\",\"salary\":1000,\"phone\":\"+12 345 678 999\",\"email\":\"john.doe@example.com\"}"))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", EmployeesController.EMPLOYEES + "/1"));

        verify(employeeRepository).save(any(EmployeeEntity.class));
    }

    @Test
    public void testUpdateEmployee() throws Exception {
        when(employeeRepository.findById(1)).thenReturn(Optional.of(mockEmployeeEntity));

        mockMvc.perform(MockMvcRequestBuilders.put(EmployeesController.EMPLOYEES + "/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"John Updated\",\"surname\":\"Doe Updated\",\"salary\":1100,\"phone\":\"+98 765 432 111\",\"email\":\"john.updated@example.com\"}"))
                .andExpect(status().isOk());

        verify(employeeRepository).findById(1);
        verify(employeeRepository).save(any(EmployeeEntity.class));
    }

    @Test
    public void testDeleteEmployee() throws Exception {
        when(employeeRepository.findById(1)).thenReturn(Optional.of(mockEmployeeEntity));

        mockMvc.perform(MockMvcRequestBuilders.delete(EmployeesController.EMPLOYEES + "/1"))
                .andExpect(status().isNoContent());

        verify(employeeRepository).findById(1);
        verify(employeeRepository).deleteById(1);
    }

    @Test
    public void testUpdateEmployeeSalary() throws Exception {
        when(employeeRepository.findById(1)).thenReturn(Optional.of(mockEmployeeEntity));

        mockMvc.perform(MockMvcRequestBuilders.patch(EmployeesController.EMPLOYEES + "/1/salary")
                        .param("newSalary", "1200"))
                .andExpect(status().isOk());

        verify(employeeRepository).findById(1);
        verify(employeeRepository).save(any(EmployeeEntity.class));
    }

    @Test
    public void testUpdateEmployeePet() throws Exception {
        Pet mockPet = new Pet();
        mockPet.setId(10L);
        mockPet.setName("Buddy");
        mockPet.setCategory("Dog");

        when(employeeRepository.findById(1)).thenReturn(Optional.of(mockEmployeeEntity));
        when(petDao.getPet(10L)).thenReturn(Optional.of(mockPet));

        mockMvc.perform(MockMvcRequestBuilders.patch(EmployeesController.EMPLOYEES + "/1/pet/10"))
                .andExpect(status().isOk());

        verify(employeeRepository).findById(1);
        verify(petDao).getPet(10L);
        verify(petRepository).save(any(PetEntity.class));
    }

    @Test
    public void testTestHeader() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(EmployeesController.EMPLOYEES + "/test-header")
                        .header("Accept", "application/json")
                        .header("httpStatus", "204"))
                .andExpect(status().isNoContent())
                .andExpect(header().string("x-my-header", "application/json"))
                .andExpect(content().string("Accepted: application/json"));
    }

    // ... Previous tests ...

    @Test
    public void testAddEmployeeWithInvalidData() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(EmployeesController.EMPLOYEES)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"\",\"surname\":\"\",\"salary\":-100,\"phone\":\"invalid_phone\",\"email\":\"invalid_email\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testAddEmployeeWithMissingData() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(EmployeesController.EMPLOYEES)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void testEmployeeDetailsWithNonExistentId() throws Exception {
        when(employeeRepository.findById(999)).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders.get(EmployeesController.EMPLOYEES + "/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testUpdateEmployeeWithInvalidData() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put(EmployeesController.EMPLOYEES + "/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"\",\"surname\":\"\",\"salary\":-100,\"phone\":\"invalid_phone\",\"email\":\"invalid_email\"}"))
                .andExpect(status().isBadRequest());
    }


    @Test
    public void testUpdateEmployeePetWithNonExistentPetId() throws Exception {
        when(employeeRepository.findById(1)).thenReturn(Optional.of(mockEmployeeEntity));
        when(petDao.getPet(999L)).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders.patch(EmployeesController.EMPLOYEES + "/1/pet/999"))
                .andExpect(status().isInternalServerError())
                .andExpect(result -> assertFalse(result.getResolvedException() instanceof ResponseStatusException))
                .andExpect(result -> assertEquals("Pet not found, petId: [999]", result.getResolvedException().getMessage()));
    }


    @Test
    public void testTestHeaderWithInvalidHttpStatus() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(EmployeesController.EMPLOYEES + "/test-header")
                        .header("Accept", "application/json")
                        .header("httpStatus", "invalid_status"))
                .andExpect(status().isBadRequest());
    }

}