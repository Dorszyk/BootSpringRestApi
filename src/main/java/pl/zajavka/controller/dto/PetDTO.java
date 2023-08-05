package pl.zajavka.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;

@Data
@With
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PetDTO {

    private Integer petId;
    private Long petStorePetId;
    private String name;
    private String category;

}
