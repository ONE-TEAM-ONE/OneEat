package com.sparta.oneeat.store.controller;

import com.sparta.oneeat.store.service.DeliveryService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class DeliveryController {

    private final DeliveryService deliveryService;

    public DeliveryController(DeliveryService deliveryService) {
        this.deliveryService = deliveryService;
    }

    @GetMapping("/check-delivery")
    public Map<String, Object> checkDeliveryAvailability(@RequestParam String storeAddress,
                                                         @RequestParam String customerAddress,
                                                         @RequestParam double radiusKm) {
        return deliveryService.checkDeliveryAvailability(storeAddress, customerAddress, radiusKm);
    }

    @GetMapping("/check-delivery-simple")
    public boolean checkDeliverySimple(@RequestParam("address") String deliveryAddress,
                                       @RequestParam("storeId") UUID storeId) {
        return deliveryService.isDeliveryAvailable(deliveryAddress, storeId);
    }
}








