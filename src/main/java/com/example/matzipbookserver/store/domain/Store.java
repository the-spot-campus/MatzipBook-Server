package com.example.matzipbookserver.store.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Entity
public class Store {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String kakaoPlaceId;

    private String categoryName;

    private String address;

    private String roadAddress;

    private String name;

    private String phone;

    private String kakaoPlaceUrl;

    private Double x; //경도
    private Double y; //위도

    @Builder.Default
    private int voteCount = 0;
}