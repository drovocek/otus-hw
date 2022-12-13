package ru.otus.server.web.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import ru.otus.dto.ClientDto;
import ru.otus.server.data.model.Client;
import ru.otus.server.data.repository.ClientRepository;
import ru.otus.server.web.util.ClientUtils;

import java.time.Duration;

import static java.time.temporal.ChronoUnit.SECONDS;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(ClientApiController.REST_URL)
public class ClientApiController {

    static final String REST_URL = "/api/clients";

    private final ClientRepository repository;
    private final Scheduler workerPool;

    @GetMapping(produces = MediaType.APPLICATION_NDJSON_VALUE)
    private Flux<ClientDto> getAllClients() {
        log.info("Data server: getAllClients()");
        return this.repository.findAll()
                .delayElements(Duration.of(1, SECONDS), this.workerPool)
                .doOnNext(client -> log.info("Data server: get client request before convert {}", client))
                .map(ClientUtils::asDtoCopy)
                .doOnNext(clientDto -> log.info("Data server: get client request {}", clientDto))
                .subscribeOn(this.workerPool);
    }

    @PostMapping
    private Mono<ClientDto> saveClient(@RequestBody ClientDto dto) {
        log.info("Data server: saveClient(clientDto: {})", dto);
        Client clientForSave = ClientUtils.asDtoForSave(dto);
        return Mono.just(clientForSave)
                .doOnNext(client -> log.info("Data server: save client response:{}", client))
                .flatMap(this.repository::save)
                .publishOn(this.workerPool)
                .map(ClientUtils::asDtoCopy)
                .doOnNext(clientDto -> log.info("Data server: save client request:{}", clientDto))
                .subscribeOn(this.workerPool);
    }
}
