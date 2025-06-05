package com.example.matzipbookserver.store.domain.filter;

import com.example.matzipbookserver.global.exception.RestApiException;
import com.example.matzipbookserver.global.response.error.StoreErrorCode;
import com.example.matzipbookserver.store.domain.repository.FoodCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class FoodFilterStrategy implements StoreFilterStrategy {

    private final FoodCategoryRepository foodCategoryRepository;

    @Override
    public void validate(List<String> foods) {
        if (foods == null)
            return;

        List<String> validFoods = foodCategoryRepository.findAllNames();
        boolean invalid = foods.stream().anyMatch(food -> !validFoods.contains(food));

        if (invalid) {
            throw new RestApiException(StoreErrorCode.INVALID_FILTER_CONDITION);
        }
    }
}
