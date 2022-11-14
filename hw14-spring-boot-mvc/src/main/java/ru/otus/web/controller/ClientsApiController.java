package ru.otus.web.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.data.model.Client;
import ru.otus.data.repository.ClientRepository;
import ru.otus.web.dto.ClientDto;
import ru.otus.web.util.ClientUtils;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(ClientsApiController.REST_URL)
public class ClientsApiController {

    public static final String REST_URL = "/api/clients";

    private final ClientRepository repository;

    @PostMapping
    public ClientDto saveClient(@RequestBody ClientDto clientDto) {
        log.info("saveClient(clientDto: {})", clientDto);
        Client client = ClientUtils.prepareForSave(clientDto);
        Client saved = this.repository.save(client);
        log.info("saveClient(saved: {})", saved);
        return ClientUtils.asDto(saved);
    }
}
