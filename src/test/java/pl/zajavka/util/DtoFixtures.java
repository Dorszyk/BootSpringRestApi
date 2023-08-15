package pl.zajavka.util;

import lombok.experimental.UtilityClass;
import pl.zajavka.controller.dto.EmployeeDTO;
import pl.zajavka.controller.dto.PetDTO;

import java.math.BigDecimal;

@UtilityClass
public class DtoFixtures {

    public static EmployeeDTO someEmployee1() {
        return EmployeeDTO
                .builder()
                .employeeId(7)
                .name("Agnieszka")
                .surname("Zajavkowa")
                .salary(new BigDecimal("52322.12"))
                .phone("+48 548 665 441")
                .email("zajavka@zajavka.com")
                .build();
    }

    public static EmployeeDTO someEmployee2() {
        return EmployeeDTO
                .builder()
                .employeeId(8)
                .name("Tomasz")
                .surname("Bednarek")
                .salary(new BigDecimal("62341.00"))
                .phone("+48 854 115 332")
                .email("mail2@mail.com")
                .build();
    }

    public static PetDTO somePet() {
        return PetDTO.builder()
                .petId(1)
                .petStorePetId(4L)
                .name("lion")
                .category("Dogs")
                .build();
    }
}
