package com.mycompany.njt_mavenproject.entity.impl;

import com.mycompany.njt_mavenproject.entity.MyEntity;
import jakarta.persistence.*;

/**
 * Entitet koji predstavlja vezu između servisa i usluge sa definisanom cenom.
 * Kombinacija servis_id i usluga_id mora biti jedinstvena.
 *
 * @author Bojana
 */
@Entity
@Table(name = "servis_usluga",
       uniqueConstraints = @UniqueConstraint(columnNames = {"servis_id", "usluga_id"}))
public class ServisUsluga implements MyEntity {

    /** Jedinstveni identifikator veze između servisa i usluge. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Servis koji nudi uslugu. */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "servis_id", nullable = false)
    private Servis servis;

    /** Usluga koju servis nudi. */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "usluga_id", nullable = false)
    private Usluga usluga;

    /** Cena usluge u konkretnom servisu. */
    @Column(nullable = false)
    private Double cena;

    /**
     * Podrazumevani konstruktor.
     */
    public ServisUsluga() {}

    /**
     * Konstruktor sa svim parametrima.
     *
     * @param servis servis koji nudi uslugu
     * @param usluga usluga koju servis nudi
     * @param cena   cena usluge u ovom servisu
     */
    public ServisUsluga(Servis servis, Usluga usluga, Double cena) {
        this.servis = servis;
        this.usluga = usluga;
        this.cena = cena;
    }

    /**
     * Vraća ID veze između servisa i usluge.
     *
     * @return jedinstveni identifikator
     */
    public Long getId() { return id; }

    /**
     * Vraća servis koji nudi uslugu.
     *
     * @return servis
     */
    public Servis getServis() { return servis; }

    /**
     * Postavlja servis koji nudi uslugu.
     *
     * @param servis servis
     */
    public void setServis(Servis servis) { this.servis = servis; }

    /**
     * Vraća uslugu koju servis nudi.
     *
     * @return usluga
     */
    public Usluga getUsluga() { return usluga; }

    /**
     * Postavlja uslugu koju servis nudi.
     *
     * @param usluga usluga
     */
    public void setUsluga(Usluga usluga) { this.usluga = usluga; }

    /**
     * Vraća cenu usluge u ovom servisu.
     *
     * @return cena
     */
    public Double getCena() { return cena; }

    /**
     * Postavlja cenu usluge u ovom servisu.
     *
     * @param cena cena usluge
     */
    public void setCena(Double cena) { this.cena = cena; }
}