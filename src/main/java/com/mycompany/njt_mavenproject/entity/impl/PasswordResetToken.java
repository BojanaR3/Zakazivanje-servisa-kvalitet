package com.mycompany.njt_mavenproject.entity.impl;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;

/**
 * Entitet koji predstavlja token za resetovanje lozinke.
 * Token je jednokratan i ima ograničeno vreme trajanja.
 *
 * @author Bojana
 */
@Entity
@Table(name = "password_reset_tokens")
public class PasswordResetToken {

    /** UUID token koji služi kao primarni ključ. */
    @Id
    private String token;

    /** Vlasnik naloga za koga je token kreiran. */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "vlasnik_id", nullable = false)
    private Vlasnik vlasnik;

    /** Vreme isteka tokena. */
    @Column(nullable = false)
    private Instant expiresAt;

    /** Oznaka da li je token već iskorišćen. */
    @Column(nullable = false)
    private boolean used = false;

    /**
     * Podrazumevani konstruktor.
     */
    public PasswordResetToken() {
    	 // Prazan konstruktor zahteva JPA specifikacija za entitete
    }

    /**
     * Fabrika metoda za kreiranje novog tokena za resetovanje lozinke.
     *
     * @param v          vlasnik naloga za koga se kreira token
     * @param ttlSeconds vreme trajanja tokena u sekundama
     * @return novi token za resetovanje lozinke
     */
    public static PasswordResetToken of(Vlasnik v, long ttlSeconds) {
        PasswordResetToken t = new PasswordResetToken();
        t.token = UUID.randomUUID().toString();
        t.vlasnik = v;
        t.expiresAt = Instant.now().plusSeconds(ttlSeconds);
        t.used = false;
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

    /**
     * Proverava da li je token već iskorišćen.
     *
     * @return true ako je token iskorišćen, false ako nije
     */
    public boolean isUsed() { return used; }

    /**
     * Postavlja oznaku iskorišćenosti tokena.
     *
     * @param used true ako je token iskorišćen
     */
    public void setUsed(boolean used) { this.used = used; }
}