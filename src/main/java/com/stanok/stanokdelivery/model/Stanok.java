package com.stanok.stanokdelivery.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Setter
@Getter
@NoArgsConstructor
@Entity
public class Stanok {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id; // Айдишник станка

    private String name; // Название станка
    private String manufacturer; // Производитель станка
    private Double price; // Цена станка

}
