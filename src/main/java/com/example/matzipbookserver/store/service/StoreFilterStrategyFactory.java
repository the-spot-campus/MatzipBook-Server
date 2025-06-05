package com.example.matzipbookserver.store.service;

import com.example.matzipbookserver.store.domain.filter.FoodFilterStrategy;
import com.example.matzipbookserver.store.domain.filter.MoodFilterStrategy;
import com.example.matzipbookserver.store.domain.filter.StoreFilterStrategy;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class StoreFilterStrategyFactory {

    private final FoodFilterStrategy foodFilterStrategy;
    private final MoodFilterStrategy moodFilterStrategy;

    private final Map<String, StoreFilterStrategy> strategyMap = new HashMap<>();

    @PostConstruct
    public void init() {
        strategyMap.put("foods", foodFilterStrategy);
        strategyMap.put("moods", moodFilterStrategy);
    }

    public StoreFilterStrategy getStrategy(String key) {
        return strategyMap.get(key);
    }
}
