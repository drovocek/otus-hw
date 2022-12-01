package ru.otus.server.data.model;

import lombok.Getter;
import lombok.ToString;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.relational.core.mapping.Column;

import java.util.UUID;

@Getter
@ToString(callSuper = true)
public class Phone extends BaseEntity {

    @Column(value = "client_id")
    private final UUID clientId;

    @Column(value = "phone_number")
    private final String number;

    public Phone(UUID id, UUID clientId, String number, boolean isNew) {
        super(id, isNew);
        this.clientId = clientId;
        this.number = number;
    }

    @PersistenceCreator
    public Phone(UUID id, UUID clientId, String number) {
        super(id, false);
        this.clientId = clientId;
        this.number = number;
    }
}
