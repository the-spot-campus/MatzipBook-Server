package com.example.matzipbookserver.global.jwt;

import com.example.matzipbookserver.global.exception.RestApiException;
import com.example.matzipbookserver.global.response.error.AuthErrorCode;
import com.example.matzipbookserver.global.response.error.MemberErrorCode;
import com.example.matzipbookserver.member.domain.Member;
import com.example.matzipbookserver.member.repository.MemberRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtTokenProvider {
    private final MemberRepository memberRepository;
    @Value("${jwt.secret}")
    private String secret;

    private SecretKey secretKey;

    public JwtTokenProvider(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @PostConstruct
    public void init() {
        byte[] decodedKey = Base64.getDecoder().decode(secret);
        this.secretKey = Keys.hmacShaKeyFor(decodedKey);
    }

    public String createAccessToken(String provider, String providerId) {
        return Jwts.builder()
                .setSubject(provider + ":" + providerId)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 3600 * 1000))
                .signWith(secretKey)
                .compact();
    }

    public String resolveToken(HttpServletRequest request) {
        String bearer = request.getHeader("Authorization");
        if (bearer != null && bearer.startsWith("Bearer ")) {
            return bearer.substring(7);
        }
        return null;
    }

    public String getSubject(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token).getBody().getSubject();
    }

    public Member getMemberFromToken(String token) {
        String subject = getSubject(token); // ex) "kakao:12345"
        String[] parts = subject.split(":");
        if (parts.length != 2) {
            throw new RestApiException(AuthErrorCode.INVALID_TOKEN);
        }

        String provider = parts[0];
        String providerId = parts[1];

        return memberRepository.findByProviderAndProviderId(provider,providerId)
                .orElseThrow(() -> new RestApiException(MemberErrorCode.MEMBER_NOT_FOUND));
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}