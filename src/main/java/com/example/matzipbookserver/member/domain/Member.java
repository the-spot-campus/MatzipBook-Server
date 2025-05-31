package com.example.matzipbookserver.member.domain;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;

import java.time.LocalDateTime;

@Entity
@Getter
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String provider;
    private String providerId;
    private String email;

    private String nickname;
    private String birth;
    private String gender;
    private String profileImagePath;

    private LocalDateTime createdAt;
    private String university;

    protected Member() {}

    public Member(String provider, String providerId, String email, String nickname, String birth, String gender, String profileImagePath, String university) {
        this.provider = provider;
        this.providerId = providerId;
        this.email = email;
        this.nickname = nickname;
        this.birth = birth;
        this.gender = gender;
        this.profileImagePath = profileImagePath;
        this.university = university;
        this.createdAt = LocalDateTime.now();
    }
}
