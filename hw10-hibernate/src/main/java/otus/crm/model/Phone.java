package otus.crm.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Optional;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "PHONE")
public class Phone implements Cloneable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "ID")
    private Long id;

    @Column(name = "NUMBER")
    private String number;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CLIENT_ID")
    private Client client;

    public Phone(Long id, String number) {
        this.id = id;
        this.number = number;
    }

    @Override
    public Phone clone() {
        Phone clone;

        try {
            clone = (Phone) super.clone();
        } catch (CloneNotSupportedException e) {
            clone = new Phone(this.id, this.number);
        }

        Client copy = Optional.ofNullable(this.client)
                .map(client-> {
                    Client clientCopy = new Client(client.getId(), client.getName());
                    Optional.ofNullable(client.getAddress()).ifPresent(clientCopy::setAddress);
                    return clientCopy;
                })
                .orElse(null);

        clone.setClient(copy);
        return clone;
    }
}
