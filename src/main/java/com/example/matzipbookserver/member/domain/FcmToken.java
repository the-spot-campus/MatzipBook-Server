package com.example.matzipbookserver.member.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "fcmtoken")
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
}