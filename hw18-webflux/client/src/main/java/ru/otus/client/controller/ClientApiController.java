package ru.otus.client.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import ru.otus.client.service.DataServiceProxy;
import ru.otus.dto.ClientDto;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(ClientApiController.REST_URL)
public class ClientApiController {

    static final String REST_URL = "/api/clients";

    private final DataServiceProxy dataServiceProxy;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    private Mono<ClientDto> saveClient(@RequestBody ClientDto dto) {
        log.info("Front server: saveClient(clientDto: {})", dto);
        return this.dataServiceProxy.saveClient(dto);
    }
}
