package com.stanok.stanokdelivery.service;

import com.stanok.stanokdelivery.model.Stanok;
import com.stanok.stanokdelivery.repository.StanokRepository;
import org.springframework.stereotype.Service;

@Service
public class StanokService {

    private final StanokRepository stanokRepository;

    public StanokService(StanokRepository stanokRepository) {
        this.stanokRepository = stanokRepository;
    }

    // Создание станка
    public Stanok createStanok(Stanok stanok) {
        return stanokRepository.save(stanok);
    }

}
