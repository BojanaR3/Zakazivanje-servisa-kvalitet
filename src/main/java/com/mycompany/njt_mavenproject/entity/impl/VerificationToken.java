package com.mycompany.njt_mavenproject.entity.impl;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;

/**
 * Entitet koji predstavlja token za verifikaciju email adrese korisnika.
 * Token je jednokratan i ima ograničeno vreme trajanja.
 *
 * @author Bojana
 */
@Entity
@Table(name = "verification_tokens")
public class VerificationToken {

    /** UUID token koji služi kao primarni ključ. */
    @Id
    private String token;

    /** Vlasnik naloga čija se email adresa verifikuje. */
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "vlasnik_id", nullable = false, unique = true)
    private Vlasnik vlasnik;

    /** Tačan trenutak isteka tokena, nezavisan od vremenske zone korisnika. */
    @Column(nullable = false)
    private Instant expiresAt;

    /**
     * Podrazumevani konstruktor.
     */
    public VerificationToken() {}

    /**
     * Fabrika metoda za kreiranje novog verifikacionog tokena.
     *
     * @param v          vlasnik naloga čija se email adresa verifikuje
     * @param ttlSeconds vreme trajanja tokena u sekundama
     * @return novi verifikacioni token
     */
    public static VerificationToken of(Vlasnik v, long ttlSeconds) {
        VerificationToken t = new VerificationToken();
        t.token = UUID.randomUUID().toString();
        t.vlasnik = v;
        t.expiresAt = Instant.now().plusSeconds(ttlSeconds);
        return t;
    }

    /**
     * Proverava da li je token istekao.
     *
     * @return true ako je token istekao, false ako je još uvek važeći
     */
    public boolean isExpired() {
        return Instant.now().isAfter(expiresAt);
    }

    /**
     * Vraća vrednost tokena.
     *
     * @return UUID token
     */
    public String getToken() { return token; }

    /**
     * Postavlja vrednost tokena.
     *
     * @param token UUID token
     */
    public void setToken(String token) { this.token = token; }

    /**
     * Vraća vlasnika naloga vezanog za ovaj token.
     *
     * @return vlasnik naloga
     */
    public Vlasnik getVlasnik() { return vlasnik; }

    /**
     * Postavlja vlasnika naloga vezanog za ovaj token.
     *
     * @param vlasnik vlasnik naloga
     */
    public void setVlasnik(Vlasnik vlasnik) { this.vlasnik = vlasnik; }

    /**
     * Vraća vreme isteka tokena.
     *
     * @return vreme isteka
     */
    public Instant getExpiresAt() { return expiresAt; }

    /**
     * Postavlja vreme isteka tokena.
     *
     * @param expiresAt vreme isteka
     */
    public void setExpiresAt(Instant expiresAt) { this.expiresAt = expiresAt; }
}