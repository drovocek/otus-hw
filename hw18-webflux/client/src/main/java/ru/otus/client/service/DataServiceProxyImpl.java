package ru.otus.client.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.dto.ClientDto;

@Service
@RequiredArgsConstructor
public class DataServiceProxyImpl implements DataServiceProxy {

    private final WebClient datastoreClient;

    @Value("${datastore.url}")
    private String dataServiceUrl;

    @Override
    public Flux<ClientDto> getAllClients() {
        return this.datastoreClient
                .get()
                .uri(this.dataServiceUrl)
                .accept(MediaType.APPLICATION_NDJSON)
                .exchangeToFlux(
                        response -> {
                            if (response.statusCode().equals(HttpStatus.OK)) {
                                return response.bodyToFlux(ClientDto.class);
                            } else {
                                return response.createException().flatMapMany(Mono::error);
                            }
                        });
    }

    @Override
    public Mono<ClientDto> saveClient(ClientDto dto) {
        return this.datastoreClient
                .post()
                .uri(this.dataServiceUrl)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(dto)
                .exchangeToMono(
                        response -> {
                            if (response.statusCode().equals(HttpStatus.OK)) {
                                return response.bodyToMono(ClientDto.class);
                            } else {
                                return response.createException().flatMap(Mono::error);
                            }
                        });
    }
}
