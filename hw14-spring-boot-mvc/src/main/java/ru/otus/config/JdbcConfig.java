package ru.otus.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.relational.core.mapping.event.BeforeConvertCallback;
import ru.otus.data.model.Client;

import java.util.UUID;

@Configuration
public class JdbcConfig {

//    @Bean
//    BeforeConvertCallback<Client> beforeConvertCallback() {
//        return (client) -> {
//            System.out.println("!!!!!!!!");
//            if (client.getId() == null) {
//                UUID uuid = UUID.randomUUID();
//                client.setId(UUID.randomUUID());
//                client.getAddress().setClientId(uuid);
//                client.getPhones().forEach(phone -> phone.setClientId(uuid));
//            }
//            return client;
//        };
//    }
}
