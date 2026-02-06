package com.allforone.starvestop.domain.store.service;

import com.allforone.starvestop.domain.store.dto.StoreRedisDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.geo.*;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.domain.geo.GeoReference;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StoreRedisService {

    private final StringRedisTemplate redisTemplate;
    private static final String redisKey = "STORES_GEO";

    //매장 좌표 저장
    public void create(Long storeId, Double longitude, Double latitude) {
        Point point = new Point(longitude, latitude);

        redisTemplate.opsForGeo().add(redisKey, point, String.valueOf(storeId));
    }

    //매장 목록 조회 (거리 기반 커서)
    public List<StoreRedisDto> get(Double longitude, Double latitude, double lastDistance, long lastId, int size) {

        RedisGeoCommands.GeoRadiusCommandArgs args =
                RedisGeoCommands.GeoRadiusCommandArgs.newGeoRadiusArgs()
                        .includeDistance()      //거리
                        .includeCoordinates()   //좌표
                        .sortAscending()        //오름차순
                        .limit(1000);       //결과 수

        GeoResults<RedisGeoCommands.GeoLocation<String>> search = redisTemplate.opsForGeo().search(redisKey,
                GeoReference.fromCoordinate(longitude, latitude),
                new Distance(5, Metrics.KILOMETERS),
                args);

        if (search == null) return null;

        return search.getContent().stream()
                .filter(
                        r -> {
                            double d = r.getDistance().getValue();
                            long id = Long.parseLong(r.getContent().getName());

                            return d > lastDistance
                                    || (Double.compare(d, lastDistance) == 0 && id > lastId);
                        })
                .limit(size * 10L)
                .map(r -> new StoreRedisDto(
                        Long.parseLong(r.getContent().getName()),
                        r.getContent().getPoint(),
                        r.getDistance().getValue()
                ))
                .toList();
    }

    //매장 좌표 삭제
    public void delete(Long storeId) {
        redisTemplate.opsForZSet().remove(redisKey, String.valueOf(storeId));
    }
}