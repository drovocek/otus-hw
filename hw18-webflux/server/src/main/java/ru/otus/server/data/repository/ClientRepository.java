package ru.otus.server.data.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import ru.otus.server.data.model.Client;

import java.util.UUID;

public interface ClientRepository extends ReactiveCrudRepository<Client, UUID> {
}
