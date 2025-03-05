package com.stanok.stanokdelivery.service;

import com.stanok.stanokdelivery.model.Stanok;
import com.stanok.stanokdelivery.repository.StanokRepository;
import org.springframework.stereotype.Service;

@Service
public class StanokService {

    private final StanokRepository stanokRepository;
    private final DeliveryService deliveryService;

    public StanokService(StanokRepository stanokRepository, DeliveryService deliveryService) {
        this.stanokRepository = stanokRepository;
        this.deliveryService = deliveryService;
    }

    // Добавление станка в БД
    public void addStanok(Stanok stanok) {

        Stanok savedStanok = stanokRepository.save(stanok);

        deliveryService.addDelivery(savedStanok);
    }

}
