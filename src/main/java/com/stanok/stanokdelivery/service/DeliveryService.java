package com.stanok.stanokdelivery.service;

import com.stanok.stanokdelivery.model.Delivery;
import com.stanok.stanokdelivery.model.Stanok;
import com.stanok.stanokdelivery.repository.DeliveryRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class DeliveryService {

    private final DeliveryRepository deliveryRepository;

    public DeliveryService(DeliveryRepository deliveryRepository) {
        this.deliveryRepository = deliveryRepository;
    }

    public void addDelivery(Stanok stanok) {

        Delivery delivery = new Delivery();

        delivery.setStanok(stanok);
        delivery.setStatus("CREATE");
        delivery.setCreatedAt(LocalDateTime.now());

        deliveryRepository.save(delivery);
    }

    // Метод вызывается каждую секунду и проверяет статус заявки
    @Scheduled(fixedRate = 1000)
    public void checkAndCanceledDeliveries() {

        List<Delivery> deliveries = deliveryRepository.findByStatus("CREATE");

        for (Delivery delivery : deliveries) {
            if (delivery.getCreatedAt().plusSeconds(10).isBefore(LocalDateTime.now())) {
                delivery.setStatus("CANCELED");
                delivery.setStatusChangedAt(LocalDateTime.now());
                deliveryRepository.save(delivery);
            }
        }
    }

    // todo
    public Delivery changeDeliveryStatus() {
        return null;
    }

}
