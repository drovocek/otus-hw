package ru.otus.web.dto;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ClientDto {

    private UUID id;

    private String name;

    private String street;

    private String number;
}
