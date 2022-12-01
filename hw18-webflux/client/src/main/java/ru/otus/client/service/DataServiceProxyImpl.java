package ru.otus.client.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.web.client.RestTemplate;
import ru.otus.dto.ClientDto;

import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DataServiceProxyImpl implements DataServiceProxy {

    private final RestTemplate restTemplate;

    @Value("${datastore.url}")
    private String dataServiceUrl;

    @Override
    public Collection<ClientDto> getAllClients() {
        ResponseEntity<List<ClientDto>> response = this.restTemplate.exchange(
                dataServiceUrl,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {
                });

        if (response.getStatusCode() != HttpStatus.OK) {
            throw new IllegalStateException(
                    String.format("Received %s error from data server when getting clients", response.getStatusCode()));
        }

        List<ClientDto> clientsDto = response.getBody();

        Assert.notNull(clientsDto, "Clients body response can't be null");

        return clientsDto;
    }

    @Override
    public ClientDto saveClient(ClientDto dto) {
        return this.restTemplate.postForObject(
                dataServiceUrl,
                new HttpEntity<>(dto),
                ClientDto.class);
    }
}
