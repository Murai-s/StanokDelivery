package com.stanok.stanokdelivery.controller;

import com.stanok.stanokdelivery.model.Stanok;
import com.stanok.stanokdelivery.repository.StanokRepository;
import com.stanok.stanokdelivery.service.DeliveryService;
import com.stanok.stanokdelivery.service.StanokService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api")
public class StanokController {

    private final StanokService stanokService;

    public StanokController(StanokService stanokService) {
        this.stanokService = stanokService;
    }

    // Обработка POST запроса на добавление станка в БД
    @PostMapping("/stanok.create")
    public ResponseEntity<String> addStanok(@RequestBody Stanok stanok) {

        stanokService.addStanok(stanok);

        return ResponseEntity.ok("Stanok with ID " + stanok.getId() + " successfully added!");
    }


}
