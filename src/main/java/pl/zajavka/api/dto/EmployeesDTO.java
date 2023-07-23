package pl.zajavka.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;

import java.util.List;

@Data
@With
@Builder
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
public class EmployeesDTO {

    private List<EmployeeDTO> employees;
}
