package com.example.matzipbookserver.store.domain;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity
public class FoodCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name; //음식 카테고리

    @OneToMany(mappedBy = "foodCategory", cascade = CascadeType.ALL)
    private List<StoreFoodCategory> foodCategories = new ArrayList<>();

}
