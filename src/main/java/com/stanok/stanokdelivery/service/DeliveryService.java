package com.stanok.stanokdelivery.service;

import com.stanok.stanokdelivery.model.Delivery;
import com.stanok.stanokdelivery.model.Stanok;
import com.stanok.stanokdelivery.repository.DeliveryRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

@Service
public class DeliveryService{

    private final DeliveryRepository deliveryRepository;

    // Пул из одного потока
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(3);

    public DeliveryService(DeliveryRepository deliveryRepository) {
        this.deliveryRepository = deliveryRepository;
    }

    public void addDelivery(Stanok stanok) {

        Delivery delivery = new Delivery();

        delivery.setStanok(stanok);
        delivery.setStatus("CREATE");
        delivery.setCreatedAt(LocalDateTime.now());

        deliveryRepository.save(delivery);

        // Метод который выполнится через 20 секунд и сменит статус заявки
        scheduleStatusChange(delivery.getId(), 20);
    }

    // Метод который изменит статус заявки спустя время
    private void scheduleStatusChange(UUID deliveryId, long delay) {

        scheduler.schedule(() -> {

            Delivery delivery = deliveryRepository.findById(deliveryId)
                    .orElseThrow(() -> new RuntimeException("Delivery not found"));

            if (delivery.getStatus().equals("CREATE")) {
                delivery.setStatus("CANCELED");
                delivery.setStatusChangedAt(LocalDateTime.now());
                deliveryRepository.save(delivery);
            }
        }, delay, TimeUnit.SECONDS);
    }

    // При запуске сервера вызывает этот метод. Нужен для корректной работе таймера при перезапуске.
    @PostConstruct
    public void restoreTimers() {

        // Список заявок со статусом CREATE
        List<Delivery> deliveriesWithCreateStatus = deliveryRepository.findByStatus("CREATE");

        for (Delivery delivery : deliveriesWithCreateStatus) {

            LocalDateTime now = LocalDateTime.now();
            LocalDateTime createdAt = delivery.getCreatedAt(); // Время создания заявки
            LocalDateTime cancelTime = createdAt.plusSeconds(20); // Время когда заявка должна отмениться

            if (cancelTime.isAfter(now)) {
                long remainingTime = Duration.between(now, cancelTime).toSeconds(); // Остаток времени в сек
                scheduleStatusChange(delivery.getId(), remainingTime);
            } else {
                // Если время уже истекло
                delivery.setStatus("CANCELED");
                delivery.setStatusChangedAt(now);
                deliveryRepository.save(delivery);
            }
        }
    }


    // todo
    public Delivery changeDeliveryStatus() {
        return null;
    }

}
