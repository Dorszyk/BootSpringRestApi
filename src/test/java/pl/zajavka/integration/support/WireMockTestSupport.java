package pl.zajavka.integration.support;

import com.github.tomakehurst.wiremock.WireMockServer;

import java.util.Map;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;

public interface WireMockTestSupport {

    default void stubForPet(WireMockServer wireMockServer, final Long petId){
        wireMockServer.stubFor(get(urlPathEqualTo("/pet/%s".formatted(petId)))
                .willReturn(aResponse()
                        .withHeader("Content-Type","application/json")
                        .withBodyFile("wiremock/petById.json")
                        .withTransformerParameters(Map.of("petId",petId))
                        .withTransformers("response-template")));

    }
}
