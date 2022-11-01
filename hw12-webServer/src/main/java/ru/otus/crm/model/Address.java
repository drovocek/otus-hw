package ru.otus.crm.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "ADDRESS")
public class Address implements Cloneable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "ID")
    private Long id;

    @Column(name = "STREET")
    private String street;

    @Override
    public Address clone() {
        Address clone;

        try {
            clone = (Address) super.clone();
        } catch (CloneNotSupportedException e) {
            clone = new Address(this.id, this.street);
        }

        return clone;
    }
}
