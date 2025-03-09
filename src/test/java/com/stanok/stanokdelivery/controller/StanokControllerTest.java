package com.stanok.stanokdelivery.controller;

import com.stanok.stanokdelivery.model.Delivery;
import com.stanok.stanokdelivery.model.Stanok;
import com.stanok.stanokdelivery.repository.DeliveryRepository;
import com.stanok.stanokdelivery.repository.StanokRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class StanokControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private StanokRepository stanokRepository;

    @Autowired
    private DeliveryRepository deliveryRepository;

    @Test
    public void addStanok() {

        Stanok stanok = new Stanok();
        stanok.setName("Станок 1");
        stanok.setManufacturer("Производитель 1");
        stanok.setPrice(1000.0);

        Delivery delivery = new Delivery();

        String url = "http://localhost:" + port + "/api/stanok.create";
        ResponseEntity<String> response = restTemplate.postForEntity(url, stanok, String.class);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode(), "Ожидался статус 200 OK");

        Stanok savedStanok = stanokRepository.findAll().get(0);
        Assertions.assertNotNull(savedStanok.getId(), "ID не может быть NULL");

        Assertions.assertEquals("Станок 1", savedStanok.getName(), "Название станка не совпадает");
        Assertions.assertEquals("Производитель 1", savedStanok.getManufacturer(), "Производитель станка не совпадает");
        Assertions.assertEquals(1000.0, savedStanok.getPrice(), "Цена станка не совпадает");

        Delivery savedDelivery = deliveryRepository.findAll().get(0);
        Optional<Delivery> foundDelivery = deliveryRepository.findById(savedDelivery.getId());
        Assertions.assertTrue(foundDelivery.isPresent(), "Заявка на доставку должна быть создана");

        delivery = foundDelivery.get();
        Assertions.assertEquals("CREATE", delivery.getStatus(), "Статус заявки должен быть CREATE");
    }

}
