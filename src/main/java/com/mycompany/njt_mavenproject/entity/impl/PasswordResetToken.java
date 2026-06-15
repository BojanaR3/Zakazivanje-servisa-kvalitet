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
@Table(name = "password_reset_tokens")

public class PasswordResetToken {
    
    @Id
    private String token; // čuvamo UUID kao PK

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "vlasnik_id", nullable = false)
    private Vlasnik vlasnik; // zamena za User

    @Column(nullable = false)
    private Instant expiresAt;

    @Column(nullable = false)
    private boolean used = false;

    public PasswordResetToken() {}

    public static PasswordResetToken of(Vlasnik v, long ttlSeconds) {
        PasswordResetToken t = new PasswordResetToken();
        t.token = UUID.randomUUID().toString();
        t.vlasnik = v;
        t.expiresAt = Instant.now().plusSeconds(ttlSeconds);
        t.used = false;
        return t;
    }

    public boolean isExpired() {
        return Instant.now().isAfter(expiresAt);
    }

    // Getteri i setteri
    public String getToken() { return token; }
    public Vlasnik getVlasnik() { return vlasnik; }
    public void setVlasnik(Vlasnik vlasnik) { this.vlasnik = vlasnik; }
    public Instant getExpiresAt() { return expiresAt; }
    public void setExpiresAt(Instant expiresAt) { this.expiresAt = expiresAt; }
    public boolean isUsed() { return used; }
    public void setUsed(boolean used) { this.used = used; }
    
}
