package ru.otus.server.data.model;

import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;

import java.util.UUID;

@Getter
public abstract class BaseEntity implements Persistable<UUID> {

    @Id
    @Column(value = "id")
    protected final UUID id;

    @Transient
    protected final boolean isNew;

    public BaseEntity(UUID id, boolean isNew) {
        this.id = id;
        this.isNew = isNew;
    }

    @Override
    public boolean isNew() {
        return this.isNew;
    }
}
