/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.njt_mavenproject.entity.impl;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;


/**
 *
 * @author Korisnik
 */
@Entity
@Table(name = "verification_tokens")

public class VerificationToken {
    
    @Id
    private String token; // čuvamo UUID kao primarni ključ (string), znaci id nije long broj, vec je id sam token (random uuid string)

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "vlasnik_id", nullable = false, unique = true)
    private Vlasnik vlasnik; // tvoj entitet umesto User

    @Column(nullable = false)
    private Instant expiresAt;  //Instant je bolji jer je precizan bez obzira na vremensku zonu korisnika; tačan trenutak u vremenu, sa zonom

    public VerificationToken() {}

    /** Fabrika za kreiranje novog tokena koji važi TTL sekundi */
    public static VerificationToken of(Vlasnik v, long ttlSeconds) {
        VerificationToken t = new VerificationToken();
        t.token = UUID.randomUUID().toString();
        t.vlasnik = v;
        t.expiresAt = Instant.now().plusSeconds(ttlSeconds);
        return t;
    }

    public boolean isExpired() {
        return Instant.now().isAfter(expiresAt);
    }

    // Getteri i setteri
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Vlasnik getVlasnik() {
        return vlasnik;
    }

    public void setVlasnik(Vlasnik vlasnik) {
        this.vlasnik = vlasnik;
    }

    public Instant getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(Instant expiresAt) {
        this.expiresAt = expiresAt;
    }
    
}
