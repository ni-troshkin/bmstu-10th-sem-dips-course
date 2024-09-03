package com.ratingservice.jwt;

import com.auth0.jwk.*;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.ratingservice.entity.RealmRole;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.jackson.io.JacksonDeserializer;
import io.jsonwebtoken.security.Keys;

import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.URL;
import lombok.NonNull;
//import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.RSAPublicKeySpec;
import java.util.Map;

@Component
public class JwtProvider {
    private final Logger log = LoggerFactory.getLogger(JwtProvider.class);

    public boolean validateToken(@NonNull String token) {
//        System.out.println(token);
        try {
            DecodedJWT jwt = JWT.decode(token);
            JwkProvider provider = new JwkProviderBuilder(
                    new URL("http://keycloak/realms/LibraryIdentityProvider/protocol/openid-connect/certs")
            ).rateLimited(false).cached(false).build();
            Jwk jwk = provider.get(jwt.getKeyId());
            System.out.println(jwt.getKeyId());
            System.out.println(jwk.getId());
//            KeyFactory kf = KeyFactory.getInstance("RSA");
//            BigInteger modulus = new BigInteger(
//                    1, Decoders.BASE64.decode(
//                            "pJORkbAQCbR5m-GkO8zu7Y00ErzFCNBQCRDDAEbFP8WsZJd6LBE_baOy" +
//                                    "w96KiRCSWYXSlbuUxQpTBku3iVJIHcYTfKOYOYJdpAtCr8Ftzki3au3FkKYhez" +
//                                    "TLdxaoBCh9hFlAJNcvljc7ZFFV1fR1c4Tp-LieYlkrC7nPmC-QjhK61UlxKPAB3Z" +
//                                    "EBKIdDhisIgtrrfSqou4IwN0uLoZGB-7krXVT6lJTNtbtURPkqUbvGddHJcjysks" +
//                                    "fioaak-O3ia1ZyxLjLjH0enqRTLCSUKD-WRyoejHvWgIRu0wYA9Ir1cRX6N2Kgc6" +
//                                    "tB3ppUlP7_Im7T_Rg410kPiulwBQaVZw"
//                            )
//                    );
//            BigInteger exponent = new BigInteger(1, Decoders.BASE64.decode("AQAB"));
//            PublicKey key = kf.generatePublic(new RSAPublicKeySpec(modulus, exponent));
            PublicKey key = jwk.getPublicKey();
            Algorithm algorithm = Algorithm.RSA256((RSAPublicKey) key, null);
//            JWTVerifier verifier = JWT.require(algorithm)
//                            .withIssuer("http://localhost:8081/realms/LibraryIdentityProvider").build();
            algorithm.verify(jwt);
            Jwts.parserBuilder()
                    .setSigningKey((RSAPublicKey) key)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (SignatureVerificationException sigEx) {
            log.error("Invalid signature", sigEx);
        } catch (ExpiredJwtException expEx) {
            log.error("Token expired", expEx);
        } catch (UnsupportedJwtException unsEx) {
            log.error("Unsupported jwt", unsEx);
        } catch (MalformedJwtException mjEx) {
            log.error("Malformed jwt", mjEx);
        } catch (Exception e) {
            log.error("invalid token", e);
        }
        return false;
    }

    public Claims getClaims(@NonNull String token) throws JwkException, MalformedURLException {
        DecodedJWT jwt = JWT.decode(token);
        JwkProvider provider = new JwkProviderBuilder(
                new URL("http://keycloak/realms/LibraryIdentityProvider/protocol/openid-connect/certs")
        ).rateLimited(false).cached(false).build();
        JacksonDeserializer<Map<String, ?>> des = new JacksonDeserializer<Map<String, ?>>(Map.of("realm_access", RealmRole.class));
        Jwk jwk = provider.get(jwt.getKeyId());
        return Jwts.parserBuilder()
                .setSigningKey((RSAPublicKey)
                        jwk.getPublicKey())
                .deserializeJsonWith(des)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

}
