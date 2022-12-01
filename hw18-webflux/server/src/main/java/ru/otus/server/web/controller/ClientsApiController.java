package ru.otus.server.web.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.otus.dto.ClientDto;
import ru.otus.server.data.model.Client;
import ru.otus.server.data.repository.ClientRepository;
import ru.otus.server.web.util.ClientUtils;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(ClientsApiController.REST_URL)
public class ClientsApiController {

    public static final String REST_URL = "/api/clients";

    private final ClientRepository repository;

    @GetMapping
    public List<ClientDto> getAllClients() {
        log.info("getAllClients()");
        List<Client> clients = this.repository.findAll();
        return ClientUtils.asDto(clients);
    }

    @PostMapping
    public ClientDto saveClient(@RequestBody ClientDto clientDto) {
        log.info("saveClient(clientDto: {})", clientDto);
        Client client = ClientUtils.prepareForSave(clientDto);
        Client saved = this.repository.save(client);
        log.info("saveClient(saved: {})", saved);
        return ClientUtils.asDto(saved);
    }
}
