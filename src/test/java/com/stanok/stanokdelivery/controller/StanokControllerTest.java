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
import java.util.Random;

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

    // Успешно ли добавляется станок в БД
    @Test
    public void addStanok() {

        Stanok stanok = new Stanok();
        stanok.setName("Станок 1");
        stanok.setManufacturer("Производитель 1");
        stanok.setPrice(1000.0);

        String url = "http://localhost:" + port + "/api/stanok.create";
            ResponseEntity<String> response = restTemplate.postForEntity(url, stanok, String.class);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode(), "Ожидался статус 200 OK");

        Stanok savedStanok = stanokRepository.findAll().get(0);
        Assertions.assertNotNull(savedStanok.getId(), "ID не может быть NULL");

        Assertions.assertEquals("Станок 1", savedStanok.getName(), "Название станка не совпадает");
        Assertions.assertEquals("Производитель 1", savedStanok.getManufacturer(), "Производитель станка не совпадает");
        Assertions.assertEquals(1000.0, savedStanok.getPrice(), "Цена станка не совпадает");
    }

    // Меняется ли статус доставки CREATE --> CANCELED спустя время, если не менять статус руками
    @Test
    public void timersAndStatusTest() throws InterruptedException {

        Random random = new Random();

        // 10 запросов
        for (int i = 0; i < 10; i++) {

            Thread.sleep(10_000 + random.nextInt(11_000));

            // Объект станка
            Stanok stanok = new Stanok();
            stanok.setName("Станок " + (i + 1));
            stanok.setManufacturer("Производитель " + (i + 1));
            stanok.setPrice(1000.0);

            // Объект доставки
            Delivery delivery = new Delivery();

            String url = "http://localhost:" + port + "/api/stanok.create";
            ResponseEntity<String> response = restTemplate.postForEntity(url, stanok, String.class);

            // Получили ли статус 200
            Assertions.assertEquals(HttpStatus.OK, response.getStatusCode(), "Ожидался статус 200 OK");

            Stanok savedStanok = stanokRepository.findAll().get(i);
            Assertions.assertNotNull(savedStanok.getId(), "ID не может быть NULL");

            Assertions.assertEquals("Станок " + (i + 1), savedStanok.getName(), "Название станка не совпадает");
            Assertions.assertEquals("Производитель " + (i + 1), savedStanok.getManufacturer(), "Производитель станка не совпадает");
            Assertions.assertEquals(1000.0, savedStanok.getPrice(), "Цена станка не совпадает");

            Delivery savedDelivery = deliveryRepository.findAll().get(i);
            Optional<Delivery> foundDelivery = deliveryRepository.findById(savedDelivery.getId());
            Assertions.assertTrue(foundDelivery.isPresent(), "Заявка на доставку должна существовать");

            delivery = foundDelivery.get();
            Assertions.assertEquals("CREATE", delivery.getStatus(), "Статус заявки при создании должен быть CREATE");

            Thread.sleep(30_000);

            Optional<Delivery> updatedDelivery = deliveryRepository.findById(savedDelivery.getId());
            Assertions.assertTrue(updatedDelivery.isPresent(), "Заявка на доставку должна существовать");
            Assertions.assertEquals("CANCELED", updatedDelivery.get().getStatus(), "Статус заявки должен измениться на CANCELED спустя 20 секунд, если не изменён вручную");
        }
        Thread.sleep(30_000);
    }

}
