package com.example.matzipbookserver.member.domain;

import com.example.matzipbookserver.s3.controller.dto.response.S3FileResponse;
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

    public static MemberImage from(final S3FileResponse imageFile) {
        MemberImage memberImage = new MemberImage();
        memberImage.fileName = imageFile.fileName();
        memberImage.url = imageFile.fileURL();
        return memberImage;
    }
}