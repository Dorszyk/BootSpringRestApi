package pl.zajavka.infrastructure.configuaration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.http.codec.json.Jackson2JsonEncoder;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import pl.zajavka.infrastructure.petstore.ApiClient;
import pl.zajavka.infrastructure.petstore.api.PetApi;

@Configuration
public class WebClientConfiguration {

    @Value("${api.petStore.url}")
    private String petStoreUrl;


    @Bean
    public ApiClient petStoreApiClient(final ObjectMapper objectMapper) {
      /*  final var httpClient = HttpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, TIMEOUT)
                .responseTimeout(Duration.ofMillis(TIMEOUT))
                .doOnConnected(
                        connection ->
                                connection.addHandlerLast(new ReadTimeoutHandler(TIMEOUT,
                                                TimeUnit.MILLISECONDS))
                                        .addHandlerLast(new WriteTimeoutHandler(TIMEOUT,
                                                TimeUnit.MILLISECONDS))); */
        final var exchangeStrategies = ExchangeStrategies
                .builder()
                .codecs(clientCodecConfigurer -> {
                    clientCodecConfigurer
                            .defaultCodecs()
                            .jackson2JsonEncoder(
                                    new Jackson2JsonEncoder(
                                            objectMapper,
                                            MediaType.APPLICATION_JSON
                                    )
                            );
                    clientCodecConfigurer
                            .defaultCodecs()
                            .jackson2JsonDecoder(
                                    new Jackson2JsonDecoder(
                                            objectMapper,
                                            MediaType.APPLICATION_JSON
                                    )
                            );
                })
                .build();

        final var webClient = WebClient.builder()
                .exchangeStrategies(exchangeStrategies)
                .build();

        ApiClient apiClient = new ApiClient(webClient);
        apiClient.setBasePath(petStoreUrl);
        return apiClient;
    }

    @Bean
    public PetApi petApi(final ObjectMapper objectMapper){
        return new PetApi(petStoreApiClient(objectMapper));
    }
}
