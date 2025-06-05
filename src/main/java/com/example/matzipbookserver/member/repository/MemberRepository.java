package com.example.matzipbookserver.member.repository;

import com.example.matzipbookserver.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByProviderAndProviderId(String provider, String providerId);

    Optional<String> findUrlById(Long memberId);

    @Modifying
    @Query("update Member m set m.profileImagePath = :newProfileImage where m.id = :memberId")
    void updateUrlById(@Param("newProfileImage") String key, @Param("memberId") Long memberId);
}