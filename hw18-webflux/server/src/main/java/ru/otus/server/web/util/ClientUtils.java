package ru.otus.server.web.util;

import lombok.experimental.UtilityClass;
import ru.otus.dto.ClientDto;
import ru.otus.server.data.model.Address;
import ru.otus.server.data.model.Client;
import ru.otus.server.data.model.Phone;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@UtilityClass
public class ClientUtils {

    public List<ClientDto> asDto(List<Client> clients) {
        return clients.stream()
                .map(ClientUtils::asDto)
                .toList();
    }

    public ClientDto asDto(Client client) {
        return ClientDto.builder()
                .id(client.getId())
                .name(client.getName())
                .number(client.getPhones()
                        .stream()
                        .map(Phone::getNumber)
                        .collect(Collectors.joining(",")))
                .street(client.getAddress().getStreet())
                .build();
    }

    public Client prepareForSave(ClientDto clientDto) {
        UUID clientId = UUID.randomUUID();

        String street = clientDto.getStreet();
        UUID streetId = UUID.randomUUID();
        Address address = new Address(streetId, clientId, street, true);

        String number = clientDto.getNumber();
        UUID phoneId = UUID.randomUUID();
        Phone phone = new Phone(phoneId, clientId, number, true);

        return new Client(clientId, clientDto.getName(), address, Set.of(phone), true);
    }
}
