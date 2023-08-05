package pl.zajavka.infrastructure.petstore;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import pl.zajavka.controller.dao.PetDao;
import pl.zajavka.infrastructure.petstore.api.PetApi;

import java.util.Optional;

@AllArgsConstructor
@Component
public class PetClientImpl implements PetDao {


    private final PetApi petApi;
    private final PetMapper petMapper;

    @Override
    public Optional<Pet> getPet(Long petId) {
        try {
            final var  available = petApi.findPetsByStatusWithHttpInfo("available").block().getBody();
            return Optional.ofNullable(petApi.getPetById(petId).block()).map(petMapper::map);

        } catch (Exception e) {
            return Optional.empty();
        }
    }
}
