package pl.zajavka.api.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;

import java.math.BigDecimal;

@Data
@With
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeDTO {

    private Integer employeeId;
    private String name;
    private String surname;
    private BigDecimal salary;

    @Size(min = 7, max = 15)
    @Pattern(regexp = "^[+]\\d{2}\\s\\d{3}\\s\\d{3}\\s\\d{3}$")
    private String phone;
    @Email
    private String email;
}
