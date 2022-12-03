package ru.otus.server.data.model;

import lombok.Getter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@ToString
@Getter
@Table("client")
public class Client implements Persistable<UUID> {

    @Id
    private final UUID id;
    @Column("name")
    private final String name;
    @Column("street")
    private final String street;
    @Column("phone")
    private final String phone;
    @Transient
    private final boolean isNew;

    @PersistenceCreator
    public Client(UUID id, String name, String street, String phone) {
        this.id = id;
        this.name = name;
        this.street = street;
        this.phone = phone;
        this.isNew = false;
    }

    public Client(UUID id, String name, String street, String phone, boolean isNew) {
        this.id = id;
        this.name = name;
        this.street = street;
        this.phone = phone;
        this.isNew = isNew;
    }
}

