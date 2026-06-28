/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.njt_mavenproject.security;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

/**
 * Servis za generisanje, parsiranje i validaciju JWT tokena.
 * Koristi HMAC-SHA256 algoritam za potpisivanje tokena.
 * Tajni ključ i vreme trajanja tokena se konfigurišu kroz application.properties.
 *
 * @author Bojana
 */
@Service
public class JwtService {

    @Value("${app.jwt.secret}")
    private String secret;

    @Value("${app.jwt.expiration-ms}")
    private long expirationMs;

    /**
     * Kreira HMAC ključ na osnovu tajnog stringa iz konfiguracije.
     *
     * @return kriptografski ključ za potpisivanje tokena
     */
    private Key key() {
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Generiše JWT token za datog korisnika sa dodatnim podacima u payload-u.
     *
     * @param user  korisnik za kojeg se generiše token
     * @param extra mapa dodatnih podataka koji se upisuju u token (npr. uloga)
     * @return generisani JWT token kao string
     */
    public String generate(UserDetails user, Map<String, Object> extra) {
        Date now = new Date();
        Date exp = new Date(now.getTime() + expirationMs);
        return Jwts.builder()
                .setSubject(user.getUsername())
                .addClaims(extra)
                .setIssuedAt(now)
                .setExpiration(exp)
                .signWith(key(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Izvlači korisničko ime iz JWT tokena.
     *
     * @param token JWT token iz kojeg se čita korisničko ime
     * @return korisničko ime upisano u token
     */
    public String extractUsername(String token) {
        return Jwts.parserBuilder().setSigningKey(key()).build()
                .parseClaimsJws(token).getBody().getSubject();
    }

    /**
     * Proverava da li je JWT token validan za datog korisnika.
     * Token je validan ako korisničko ime odgovara i token nije istekao.
     *
     * @param token JWT token koji se validira
     * @param user  korisnik za kojeg se proverava token
     * @return {@code true} ako je token validan, {@code false} u suprotnom
     */
    public boolean isValid(String token, UserDetails user) {
        try {
            final String un = extractUsername(token);
            return un.equals(user.getUsername()) && !isExpired(token);
        } catch (JwtException e) {
            return false;
        }
    }

    /**
     * Proverava da li je JWT token istekao.
     *
     * @param token JWT token čiji se rok trajanja proverava
     * @return {@code true} ako je token istekao, {@code false} ako je još uvek validan
     */
    private boolean isExpired(String token) {
        Date exp = Jwts.parserBuilder().setSigningKey(key()).build()
                .parseClaimsJws(token).getBody().getExpiration();
        return exp.before(new Date());
    }
}