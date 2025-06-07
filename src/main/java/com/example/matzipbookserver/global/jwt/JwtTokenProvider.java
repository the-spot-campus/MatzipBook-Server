package com.example.matzipbookserver.global.jwt;

import com.example.matzipbookserver.global.exception.RestApiException;
import com.example.matzipbookserver.global.response.error.AuthErrorCode;
import com.example.matzipbookserver.global.response.error.MemberErrorCode;
import com.example.matzipbookserver.member.domain.LoginMember;
import com.example.matzipbookserver.member.domain.Member;
import com.example.matzipbookserver.member.repository.MemberRepository;
import io.jsonwebtoken.Claims;
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

    @Value("${jwt.access-token-valid-time}")
    private long accessTokenValidTime;

    @Value("${jwt.refresh-token-valid-time}")
    private Long refreshTokenValidTime;

    private SecretKey secretKey;

    public JwtTokenProvider(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @PostConstruct
    public void init() {
        byte[] decodedKey = Base64.getDecoder().decode(secret);
        this.secretKey = Keys.hmacShaKeyFor(decodedKey);
    }

    public String generateAccessToken(LoginMember loginMember) {
        return Jwts.builder()
                .setSubject(loginMember.provider()+ ":" + loginMember.providerId())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + accessTokenValidTime))
                .signWith(secretKey)
                .compact();
    }

    public String generateRefreshToken(LoginMember loginMember, long tokenId) {
        return Jwts.builder()
                .setSubject(loginMember.provider() + ":" + loginMember.providerId())
                .claim("tokenId",tokenId)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + refreshTokenValidTime))
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

    public Long getTokenIdFromToken(String token) {
        try {
            Claims cliams = Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            return cliams.get("tokenId",Long.class);
        } catch (Exception e) {
            throw new RestApiException(AuthErrorCode.INVALID_TOKEN);
        }
    }

    public LoginMember getLoginMemberFromToken(String token) {
        String subject = getSubject(token);
        String[] parts = subject.split(":");
        if (parts.length != 2) {
            throw new RestApiException(AuthErrorCode.INVALID_TOKEN);
        }
        return new LoginMember(parts[0], parts[1]);
    }

    public boolean isNotExpiredToken(String token) {
        try {
            Date expiration = Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token).getBody().getExpiration();
            return expiration.after(new Date());
        } catch (Exception e) {
            return false;
        }
    }
}