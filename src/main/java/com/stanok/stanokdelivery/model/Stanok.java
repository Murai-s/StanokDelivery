package com.stanok.stanokdelivery.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Setter
@Getter
@Entity
public class Stanok {

    @Id
    private UUID id; // Айдишник станка

    private String name; // Название станка
    private String manufacturer; // Производитель станка
    private Double price; // Цена станка

    public Stanok() {
        this.id = UUID.randomUUID();
    }
}
