package pl.zajavka.controller.dto;


import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import pl.zajavka.controller.dto.EmployeeDTO;
import pl.zajavka.infrastructure.database.entity.EmployeeEntity;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface EmployeeMapper {
    EmployeeDTO map(EmployeeEntity employeeEntity);
}
