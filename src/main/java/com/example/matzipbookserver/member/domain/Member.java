package com.example.matzipbookserver.member.domain;

import com.example.matzipbookserver.s3.controller.dto.response.S3File;
import com.example.matzipbookserver.member.controller.dto.request.SignUpRequest;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "member")
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

    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE})
    @JoinColumn(name = "memberImageId", referencedColumnName = "id")
    private MemberImage memberImage;
    private LocalDateTime createdAt;
    private String university;

    @Builder
    public Member(String provider, String providerId, String email, String nickname,
                  String birth, String gender, MemberImage memberImage,
                  String university, LocalDateTime createdAt) {
        this.provider = provider;
        this.providerId = providerId;
        this.email = email;
        this.nickname = nickname;
        this.birth = birth;
        this.gender = gender;
        this.memberImage = memberImage;
        this.university = university;
        this.createdAt = createdAt != null ? createdAt : LocalDateTime.now();
    }

    public static Member from(SignUpRequest request, MemberImage profileImage) {
        return Member.builder()
                .provider(request.getProvider())
                .providerId(request.getProviderId())
                .email(request.getEmail())
                .nickname(request.getNickname())
                .birth(request.getBirth())
                .gender(request.getGender())
                .university(request.getUniversity())
                .memberImage(profileImage)
                .createdAt(LocalDateTime.now())
                .build();
    }

    public void changeImage(S3File s3File) {
        this.memberImage = MemberImage.from(s3File);
    }

    public void deleteImage() {
        this.memberImage = null;
    }
}