package ru.otus.web.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.otus.data.repository.ClientRepository;
import ru.otus.web.util.ClientUtils;

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
        var clientsDto = ClientUtils.asDto(clients);
        model.addAttribute("clients", clientsDto);
        return "clients";
    }
}
