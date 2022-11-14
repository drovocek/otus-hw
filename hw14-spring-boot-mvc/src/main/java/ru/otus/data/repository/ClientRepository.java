package ru.otus.data.repository;

import org.springframework.data.repository.CrudRepository;
import ru.otus.data.model.Client;

import java.util.List;

public interface ClientRepository extends CrudRepository<Client, Long> {

    List<Client> findAll();
}
