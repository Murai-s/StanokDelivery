package com.stanok.stanokdelivery.service;

import com.stanok.stanokdelivery.model.Delivery;
import com.stanok.stanokdelivery.model.Stanok;
import com.stanok.stanokdelivery.repository.DeliveryRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

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

    // todo
    public Delivery changeDeliveryStatus() {
        return null;
    }

}
