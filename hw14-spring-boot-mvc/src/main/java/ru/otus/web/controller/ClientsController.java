package ru.otus.web.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.otus.data.model.Client;
import ru.otus.data.repository.ClientRepository;
import ru.otus.web.dto.ClientDto;
import ru.otus.web.util.ClientUtils;

import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping(ClientsController.URL)
public class ClientsController {

    public static final String URL = "/clients";

    private final ClientRepository repository;

    @GetMapping
    private String getAllClients(Model model) {
        log.info("getAllClients()");
        var clients = this.repository.findAll();
        var clientsDto = StreamSupport.stream(clients.spliterator(), false)
                .map(ClientUtils::asDto)
                .collect(Collectors.toList());
        System.out.println(clientsDto);
        model.addAttribute("clients", clientsDto);
        return "clients";
    }

    @ResponseBody
    @PostMapping("/save")
    public ClientDto saveClient(@RequestBody ClientDto clientDto) {
        log.info("saveClient(clientDto: {})", clientDto);
        Client client = ClientUtils.prepareForSave(clientDto);
        Client saved = this.repository.save(client);
        log.info("saveClient(saved: {})", saved);
        return ClientUtils.asDto(saved);
    }
}
