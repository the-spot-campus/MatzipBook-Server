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
public class MoodCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name; //분위기 카테고리

    @OneToMany(mappedBy = "moodCategory", cascade = CascadeType.ALL)
    private List<StoreMoodCategory> moodCategories = new ArrayList<>();
}
