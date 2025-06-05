package com.example.matzipbookserver.member.repository;

import com.example.matzipbookserver.member.domain.FcmToken;
import com.example.matzipbookserver.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FcmTokenRepository extends JpaRepository<FcmToken, Long> {
    Optional<FcmToken> findByMember(Member member);
}
