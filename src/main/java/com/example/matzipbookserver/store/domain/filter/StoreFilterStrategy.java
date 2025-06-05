package com.example.matzipbookserver.store.domain.filter;

import java.util.List;

public interface StoreFilterStrategy {
    void validate(List<String> values);
}
