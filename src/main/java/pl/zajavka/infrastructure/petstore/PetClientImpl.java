package pl.zajavka.infrastructure.petstore;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import pl.zajavka.controller.dao.PetDao;

import java.util.Optional;

@AllArgsConstructor
@Component
public class PetClientImpl implements PetDao {

    private final WebClient webClient;

    @Override
    public Optional<Pet> getPet(Long id) {
        try {
            Pet result = webClient
                    .get()
                    .uri("/pet/" + id)
                    .retrieve()
                    .bodyToMono(Pet.class)
                    .block();
            return Optional.ofNullable(result);
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}
