package com.example.matzipbookserver.member.repository;

import com.example.matzipbookserver.member.domain.Member;
import com.example.matzipbookserver.member.domain.MemberImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByProviderAndProviderId(String provider, String providerId);

    @Query("SELECT m.memberImage FROM Member m JOIN m.memberImage mi WHERE m.id = :id")
    Optional<MemberImage> findMemberImageById(Long id);
}