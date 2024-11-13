package com.sparta.oneeat.store.service;

import com.sparta.oneeat.store.repository.DeliveryRegionRepository;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class DeliveryService {

    private final DeliveryRegionRepository deliveryRegionRepository;
    private final GeocodingService geocodingService;
    private static final Logger logger = LoggerFactory.getLogger(DeliveryService.class);

    public DeliveryService(DeliveryRegionRepository deliveryRegionRepository, GeocodingService geocodingService) {
        this.deliveryRegionRepository = deliveryRegionRepository;
        this.geocodingService = geocodingService;
    }

    public Map<String, Object> checkDeliveryAvailability(String storeAddress, String customerAddress, double radiusKm) {
        Map<String, Object> response = new HashMap<>();

        try {
            // 테스트 코드 시작
            JSONObject storeCoordinates = geocodingService.getCoordinates(storeAddress);
            JSONObject customerCoordinates = geocodingService.getCoordinates(customerAddress);

            double storeLat = storeCoordinates.getDouble("y");
            double storeLon = storeCoordinates.getDouble("x");
            double customerLat = customerCoordinates.getDouble("y");
            double customerLon = customerCoordinates.getDouble("x");

            boolean isDeliverable = isWithinDeliveryRadius(storeLat, storeLon, customerLat, customerLon, radiusKm);

            response.put("isDeliverable", isDeliverable);
            response.put("storeLatitude", storeLat);
            response.put("storeLongitude", storeLon);
            response.put("customerLatitude", customerLat);
            response.put("customerLongitude", customerLon);
            // 테스트 코드 끝
        } catch (Exception e) {
            e.printStackTrace();
            response.put("isDeliverable", false);
            response.put("error", e.getMessage());
        }

        return response;
    }

    private static final double EARTH_RADIUS = 6371; // 지구의 반지름 (km)

    public boolean isWithinDeliveryRadius(double lat1, double lon1, double lat2, double lon2, double radiusKm) {
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);

        double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLon/2) * Math.sin(dLon/2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        double distance = EARTH_RADIUS * c;

        return distance <= radiusKm;
    }

    public boolean isDeliveryAvailable(String deliveryAddress, UUID storeId) {
        try {
            String region = extractRegion(deliveryAddress);
            logger.info("Extracted region: {}", region);

            return deliveryRegionRepository.findByStoreId(storeId)
                    .map(deliveryRegion -> {
                        String[] regions = deliveryRegion.getDeliveryRegions();
                        if (regions == null) {
                            logger.warn("Delivery regions is null for store ID: {}", storeId);
                            return false;
                        }
                        logger.info("Delivery available: {}", Arrays.asList(regions));
                        boolean contains = Arrays.asList(regions).contains(region);
                        logger.info("Delivery available: {}", contains);
                        return contains;
                    })
                    .orElseGet(() -> {
                        logger.warn("No delivery region found for store ID: {}", storeId);
                        return false;
                    });
        } catch (RuntimeException e) {
            logger.error("Error in isDeliveryAvailable: ", e);
            return false;
        }
    }

    private String extractRegion(String address) {
        logger.info("Extracting region for address: {}", address);
        try {
            JSONObject result = geocodingService.getCoordinates(address);

            if (result.has("address")) {
                JSONObject addressInfo = result.getJSONObject("address");
                String region = addressInfo.getString("region_3depth_name");
                logger.info("Extracted region_3depth_name: {}", region);
                return region;
            } else {
                logger.warn("No address information found for address: {}", address);
                throw new RuntimeException("주소에 대한 정보를 찾을 수 없습니다: " + address);
            }
        } catch (Exception e) {
            logger.error("Error while extracting region: ", e);
            throw new RuntimeException("주소 변환 중 오류 발생: " + e.getMessage(), e);
        }
    }

}