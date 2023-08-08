package pl.zajavka.util;

import lombok.experimental.UtilityClass;
import pl.zajavka.infrastructure.database.entity.EmployeeEntity;

import java.math.BigDecimal;

@UtilityClass
public class EntityFixtures {

    public static EmployeeEntity someEmployee1(){
        return EmployeeEntity
                .builder()
                .name("Agnieszka")
                .surname("Zajavkowa")
                .salary(new BigDecimal("52322.12"))
                .phone("+48 548 665 441")
                .email("zajavka@zajavka.com")
                .build();
    }
    public static EmployeeEntity someEmployee2(){
        return EmployeeEntity
                .builder()
                .name("Remigiusz")
                .surname("Spring")
                .salary(new BigDecimal("62341.00"))
                .phone("+48 854 115 332")
                .email("mail@mail.com")
                .build();
    }
    public static EmployeeEntity someEmployee3(){
        return EmployeeEntity
                .builder()
                .name("Mariusz")
                .surname("Hibernate")
                .salary(new BigDecimal("53231.00"))
                .phone("+48 745 554 445")
                .email("zajavka@email.com")
                .build();
    }
}
