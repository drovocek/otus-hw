package ru.otus.server.web.util;

import lombok.experimental.UtilityClass;
import ru.otus.dto.ClientDto;
import ru.otus.server.data.model.Client;

import java.util.UUID;

@UtilityClass
public class ClientUtils {

    public ClientDto asDtoCopy(Client client) {
        return ClientDto.builder()
                .id(client.getId())
                .name(client.getName())
                .phone(client.getPhone())
                .street(client.getStreet())
                .build();
    }

    public Client asDtoForSave(ClientDto dto) {
        return new Client(
                UUID.randomUUID(),
                dto.getName(),
                dto.getStreet(),
                dto.getPhone(),
                true);
    }
}
