package ru.otus.client.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.result.view.Rendering;
import org.thymeleaf.spring5.context.webflux.IReactiveDataDriverContextVariable;
import org.thymeleaf.spring5.context.webflux.ReactiveDataDriverContextVariable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.client.service.DataServiceProxy;
import ru.otus.dto.ClientDto;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping(ClientController.URL)
public class ClientController {

    static final String URL = "/clients";

    private final DataServiceProxy dataServiceProxy;

    @GetMapping
    private String getAllClients(Model model) {
        log.info("Front server: getAllClients()");
        IReactiveDataDriverContextVariable reactiveDataDrivenMode =
                new ReactiveDataDriverContextVariable(this.dataServiceProxy.getAllClients(), 1);

        model.addAttribute("clients", reactiveDataDrivenMode);

        return "clients";
    }
}
