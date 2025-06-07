package com.example.matzipbookserver.member.domain;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class RefreshToken {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tokenId")
    private Long id;

    @Column
    private String refreshToken;

    @Column
    private boolean isExpired = false;

    @Column
    private LocalDateTime recentLogin = LocalDateTime.now();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memberId")
    private Member member;

    protected RefreshToken() {}

    public RefreshToken(Member member, String refreshToken) {
        this.member = member;
        this.refreshToken = refreshToken;
    }

    public void putRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public void expire() {
        isExpired = true;
    }

    public boolean isExpired() {
        return isExpired;
    }

    public Long getId() {
        return id;
    }

    public void changeRecentLogin() {
        recentLogin = LocalDateTime.now();
    }
}

