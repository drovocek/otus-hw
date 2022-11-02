package ru.otus.data.repository;

import org.springframework.data.repository.CrudRepository;
import ru.otus.data.model.Client;

public interface ClientRepository extends CrudRepository<Client, Long> {
}
