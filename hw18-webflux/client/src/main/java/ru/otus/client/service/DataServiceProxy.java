package ru.otus.client.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.dto.ClientDto;

public interface DataServiceProxy {

    Flux<ClientDto> getAllClients();

    Mono<ClientDto> saveClient(ClientDto dto);
}
