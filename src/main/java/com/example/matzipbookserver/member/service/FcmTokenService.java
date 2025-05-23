package com.example.matzipbookserver.member.service;

import com.example.matzipbookserver.member.domain.FcmToken;
import com.example.matzipbookserver.member.domain.Member;
import com.example.matzipbookserver.member.repository.FcmTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FcmTokenService {
    private final FcmTokenRepository fcmTokenRepository;

    public void saveOrUpdate(Member member,String fcmToken) {

        //해당 유저의 fcmToken이 존재하면 갱신, 없으면 새로 저장
        fcmTokenRepository.findByMember(member)
                .ifPresentOrElse(
                        existing -> {
                            existing.update(fcmToken);
                            fcmTokenRepository.save(existing);
                        },
                        () -> {
                            fcmTokenRepository.save(new FcmToken(member, fcmToken));
                        });

    }
}
