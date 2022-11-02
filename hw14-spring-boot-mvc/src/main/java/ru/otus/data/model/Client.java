package ru.otus.data.model;

import lombok.Getter;
import lombok.ToString;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.MappedCollection;

import java.util.Set;
import java.util.UUID;

@Getter
@ToString(callSuper = true)
public class Client extends BaseEntity {

    @Column(value = "name")
    private final String name;

    @MappedCollection(idColumn = "client_id")
    private final Address address;

    @MappedCollection(idColumn = "client_id")
    private final Set<Phone> phones;

    public Client(UUID id, String name, Address address, Set<Phone> phones, boolean isNew) {
        super(id, isNew);
        this.name = name;
        this.address = address;
        this.phones = phones;
    }

    @PersistenceCreator
    public Client(UUID id, String name, Address address, Set<Phone> phones) {
        super(id, false);
        this.name = name;
        this.address = address;
        this.phones = phones;
    }
}
