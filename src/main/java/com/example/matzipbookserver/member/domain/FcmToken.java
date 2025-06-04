package com.example.matzipbookserver.member.domain;

import jakarta.persistence.*;

@Entity
public class FcmToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fcmToken;

    @OneToOne(fetch = FetchType.LAZY)
    private Member member;

    public FcmToken( Member member, String fcmToken) {
        this.fcmToken = fcmToken;
        this.member = member;
    }

    public void update(String newFcmToken) {
        this.fcmToken = newFcmToken;
    }

    protected FcmToken() {} //JPA 기본 생성자
}


