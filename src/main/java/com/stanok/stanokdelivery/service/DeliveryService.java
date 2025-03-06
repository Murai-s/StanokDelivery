package com.stanok.stanokdelivery.service;

import com.stanok.stanokdelivery.model.Delivery;
import com.stanok.stanokdelivery.model.Stanok;
import com.stanok.stanokdelivery.repository.DeliveryRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

@Service
public class DeliveryService {

    private final DeliveryRepository deliveryRepository;

    // Пул из одного потока
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public DeliveryService(DeliveryRepository deliveryRepository) {
        this.deliveryRepository = deliveryRepository;
    }

    public void addDelivery(Stanok stanok) {

        Delivery delivery = new Delivery();

        delivery.setStanok(stanok);
        delivery.setStatus("CREATE");
        delivery.setCreatedAt(LocalDateTime.now());

        deliveryRepository.save(delivery);

        scheduleStatusChange(delivery.getId(), 20, TimeUnit.SECONDS);
    }

    private void scheduleStatusChange(UUID deliveryId, long delay, TimeUnit unit) {

        scheduler.schedule(() -> {

            Delivery delivery = deliveryRepository.findById(deliveryId)
                    .orElseThrow(() -> new RuntimeException("Delivery not found"));

            if (delivery.getStatus().equals("CREATE")) {
                delivery.setStatus("CANCELED");
                delivery.setStatusChangedAt(LocalDateTime.now());
                deliveryRepository.save(delivery);
            }
        }, delay, unit);
    }




    // todo
    public Delivery changeDeliveryStatus() {
        return null;
    }

}
