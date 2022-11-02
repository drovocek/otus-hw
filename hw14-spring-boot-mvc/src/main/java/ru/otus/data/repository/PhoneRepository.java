package ru.otus.data.repository;

import org.springframework.data.repository.CrudRepository;
import ru.otus.data.model.Phone;

public interface PhoneRepository extends CrudRepository<Phone, Long> {
}
