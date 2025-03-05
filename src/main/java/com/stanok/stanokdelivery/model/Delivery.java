package com.stanok.stanokdelivery.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Entity
public class Delivery {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id; // Айдишник заявки на доставку

    @OneToOne
    private Stanok stanok; // Связь один к одному со станками

    private String status; // Статус заявки (CREATE, IN_DELIVERY, DELIVERED, CANCELED)
    private LocalDateTime createdAt; // Время создания заявки на доставку
    private LocalDateTime statusChangedAt; // Время смены статуса заявки
}
