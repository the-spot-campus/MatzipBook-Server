package com.example.matzipbookserver.member.domain;

import com.example.matzipbookserver.s3.controller.dto.response.S3File;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "memberimage")
public class MemberImage {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String fileName;
    private String url;

    public static MemberImage from(final S3File imageFile) {
        MemberImage memberImage = new MemberImage();
        memberImage.fileName = imageFile.fileName();
        memberImage.url = imageFile.fileURL();
        return memberImage;
    }
}