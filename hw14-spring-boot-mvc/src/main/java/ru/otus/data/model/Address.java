package ru.otus.data.model;

import lombok.Getter;
import lombok.ToString;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.relational.core.mapping.Column;

import java.util.UUID;

@Getter
@ToString(callSuper = true)
public class Address extends BaseEntity {

    @Column(value = "client_id")
    private final UUID clientId;

    @Column(value = "street")
    private final String street;

    public Address(UUID id, UUID clientId, String street, boolean isNew) {
        super(id, isNew);
        this.clientId = clientId;
        this.street = street;
    }

    @PersistenceCreator
    public Address(UUID id, UUID clientId, String street) {
        super(id, false);
        this.clientId = clientId;
        this.street = street;
    }
}
