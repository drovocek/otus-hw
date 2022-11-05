package ru.otus.crm.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Getter
@Setter
@NoArgsConstructor
@Entity
@ToString
@Table(name = "CLIENT")
public class Client implements Cloneable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "ID")
    private Long id;

    @Column(name = "NAME")
    private String name;

    @OneToOne(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    @JoinColumn(name = "ADDRESS_ID")
    private Address address;

    @OneToMany(mappedBy = "client", cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    private List<Phone> phones = new ArrayList<>();

    public Client(Long id, String name, Address address, List<Phone> phones) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.phones = phones;
        this.phones.forEach(phone -> phone.setClient(this));
    }

    public Client(String name) {
        this.id = null;
        this.name = name;
    }

    public Client(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public Client clone() {
        Client clone;

        try {
            clone = (Client) super.clone();
        } catch (CloneNotSupportedException e) {
            clone = new Client(this.id, this.name);
        }

        Address address = Optional.ofNullable(this.address).map(Address::clone).orElse(null);
        Client finalClone = clone;
        List<Phone> phones = this.phones.stream()
                .map(phone -> {
                    Phone cloned = phone.clone();
                    cloned.setClient(finalClone);
                    return cloned;
                })
                .toList();

        clone.setAddress(address);
        clone.setPhones(phones);

        return clone;
    }
}
