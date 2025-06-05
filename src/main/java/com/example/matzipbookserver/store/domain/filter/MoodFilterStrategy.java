package com.example.matzipbookserver.store.domain.filter;

import com.example.matzipbookserver.global.exception.RestApiException;
import com.example.matzipbookserver.global.response.error.StoreErrorCode;
import com.example.matzipbookserver.store.domain.repository.MoodCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class MoodFilterStrategy implements StoreFilterStrategy {

    private final MoodCategoryRepository moodCategoryRepository;

    @Override
    public void validate(List<String> moods) {
        if (moods == null)
            return;

        List<String> validMoods = moodCategoryRepository.findAllNames();
        boolean invalid = moods.stream().anyMatch(mood -> !validMoods.contains(mood));

        if (invalid) {
            throw new RestApiException(StoreErrorCode.INVALID_FILTER_CONDITION);
        }
    }
}
