package com.example.matzipbookserver.member.repository;

import com.example.matzipbookserver.member.domain.Member;
import com.example.matzipbookserver.member.domain.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByMember(Member member);

    void deleteAllByMember(Member member);
}
