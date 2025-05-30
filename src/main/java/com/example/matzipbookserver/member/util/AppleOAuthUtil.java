package com.example.matzipbookserver.member.util;

import com.example.matzipbookserver.member.util.dto.AppleIdTokenPayload;
import com.example.matzipbookserver.member.util.dto.AppleTokenResponse;
import org.springframework.core.io.Resource;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.text.ParseException;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class AppleOAuthUtil {

    private final RestTemplate restTemplate;
    @Value("${apple.team-id}")
    private String teamId;

    @Value("${apple.client-id}")
    private String clientId;

    @Value("${apple.key-id}")
    private String keyId;

    @Value("${apple.private-key}")
    private String privateKeyPem;

    @Value("${apple.redirect-uri}")
    private String redirectUri;

    private final static String APPLE_AUTH_URL = "https://appleid.apple.com";

    public AppleTokenResponse requestToken(String code) {
        String clientSecret = createClientSecret();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("client_id", clientId);
        body.add("client_secret", clientSecret);
        body.add("grant_type", "authorization_code");
        body.add("code", code);
        body.add("redirect_uri", redirectUri);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);

        ResponseEntity<AppleTokenResponse> response = restTemplate.exchange(
                APPLE_AUTH_URL + "/auth/token",
                HttpMethod.POST,
                request,
                AppleTokenResponse.class
        );
        return response.getBody();
    }

    public AppleIdTokenPayload parseIdToken(String idToken) throws ParseException, java.text.ParseException, IOException {
        SignedJWT signedJWT = SignedJWT.parse(idToken);
        JWTClaimsSet claims = signedJWT.getJWTClaimsSet();

        return new AppleIdTokenPayload (
                claims.getSubject(),
                claims.getStringClaim("email")
        );
    }

    private String createClientSecret() {
        Instant now = Instant.now();
        Instant exp = now.plusSeconds(180); // 3분짜리 client_secret

        return Jwts.builder()
                .setHeaderParam("alg","ES256")
                .setHeaderParam("kid",keyId)
                .setIssuer(teamId)
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(exp))
                .setAudience("https://appleid.apple.com")
                .setSubject(clientId)
                .signWith(SignatureAlgorithm.ES256, getPrivateKey())
                .compact();
    }

    private PrivateKey getPrivateKey() {
        try {
            String keyContent = privateKeyPem
                    .replace("-----BEGIN PRIVATE KEY-----", "")
                    .replace("-----END PRIVATE KEY-----", "")
                    .replaceAll("\\s", "");

            byte[] pkcs8EncodedBytes = Base64.getDecoder().decode(keyContent);
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(pkcs8EncodedBytes);
            KeyFactory keyFactory = KeyFactory.getInstance("EC");
            return keyFactory.generatePrivate(keySpec);
        } catch (Exception e) {
            throw new RuntimeException("애플 private key 로드에 실패했습니다.",e);
        }
    }


}
