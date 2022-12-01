package ru.otus.client.service;


import ru.otus.dto.ClientDto;

import java.util.Collection;

public interface DataServiceProxy {

    Collection<ClientDto> getAllClients();

    ClientDto saveClient(ClientDto dto);
}
