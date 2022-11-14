package ru.otus.data.repository;

import org.springframework.data.repository.CrudRepository;
import ru.otus.data.model.Address;

public interface AddressRepository extends CrudRepository<Address, Long> {
}
