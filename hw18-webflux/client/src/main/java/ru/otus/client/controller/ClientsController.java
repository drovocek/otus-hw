package ru.otus.client.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.otus.client.service.DataServiceProxy;
import ru.otus.dto.ClientDto;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping(ClientsController.URL)
public class ClientsController {

    public static final String URL = "/clients";

    private final DataServiceProxy dataServiceProxy;

    @GetMapping
    private String getAllClients(Model model) {
        log.info("getAllClients()");
        var clientsDto = this.dataServiceProxy.getAllClients();
        model.addAttribute("clients", clientsDto);
        return "clients";
    }

    @PostMapping
    @ResponseBody
    public ClientDto saveClient(@RequestBody ClientDto dto) {
        log.info("saveClient(clientDto: {})", dto);
        return this.dataServiceProxy.saveClient(dto);
    }
}
