package com.mycompany.njt_mavenproject.entity.impl;

import com.mycompany.njt_mavenproject.entity.MyEntity;
import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Entitet koji predstavlja račun izdat na osnovu rezervacije.
 * Svaki račun je vezan za tačno jednu rezervaciju.
 *
 * @author Bojana
 */
@Entity
@Table(name = "racun")
public class Racun implements MyEntity {

    /** Jedinstveni identifikator računa. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Broj računa, mora biti jedinstven. */
    @Column(nullable = false, unique = true)
    private String broj;

    /** Datum i vreme izdavanja računa. */
    @Column(nullable = false)
    private LocalDateTime datumIzdavanja;

    /** Ukupan iznos na računu. */
    @Column(nullable = false)
    private Double ukupanIznos;

    /** Status plaćanja računa (npr. PLACENO, NEPLACENO). */
    @Column(nullable = false)
    private String statusPlacanja;

    /** Rezervacija na osnovu koje je račun izdat. */
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "rezervacija_id", nullable = false, unique = true)
    private Rezervacija rezervacija;

    /**
     * Podrazumevani konstruktor.
     */
    public Racun() {}

    /**
     * Konstruktor koji kreira referencu na račun sa zadatim ID-em.
     *
     * @param id jedinstveni identifikator računa
     */
    public Racun(Long id) { this.id = id; }

    /**
     * Vraća ID računa.
     *
     * @return jedinstveni identifikator
     */
    public Long getId() { return id; }

    /**
     * Postavlja ID računa.
     *
     * @param id jedinstveni identifikator
     */
    public void setId(Long id) { this.id = id; }

    /**
     * Vraća broj računa.
     *
     * @return broj računa
     */
    public String getBroj() { return broj; }

    /**
     * Postavlja broj računa.
     *
     * @param broj broj računa (mora biti jedinstven)
     */
    public void setBroj(String broj) { this.broj = broj; }

    /**
     * Vraća datum i vreme izdavanja računa.
     *
     * @return datum izdavanja
     */
    public LocalDateTime getDatumIzdavanja() { return datumIzdavanja; }

    /**
     * Postavlja datum i vreme izdavanja računa.
     *
     * @param datumIzdavanja datum i vreme izdavanja
     */
    public void setDatumIzdavanja(LocalDateTime datumIzdavanja) { this.datumIzdavanja = datumIzdavanja; }

    /**
     * Vraća ukupan iznos računa.
     *
     * @return ukupan iznos
     */
    public Double getUkupanIznos() { return ukupanIznos; }

    /**
     * Postavlja ukupan iznos računa.
     *
     * @param ukupanIznos ukupan iznos
     */
    public void setUkupanIznos(Double ukupanIznos) { this.ukupanIznos = ukupanIznos; }

    /**
     * Vraća status plaćanja računa.
     *
     * @return status plaćanja
     */
    public String getStatusPlacanja() { return statusPlacanja; }

    /**
     * Postavlja status plaćanja računa.
     *
     * @param statusPlacanja status plaćanja (npr. PLACENO, NEPLACENO)
     */
    public void setStatusPlacanja(String statusPlacanja) { this.statusPlacanja = statusPlacanja; }

    /**
     * Vraća rezervaciju vezanu za ovaj račun.
     *
     * @return rezervacija
     */
    public Rezervacija getRezervacija() { return rezervacija; }

    /**
     * Postavlja rezervaciju vezanu za ovaj račun.
     *
     * @param rezervacija rezervacija na osnovu koje je račun izdat
     */
    public void setRezervacija(Rezervacija rezervacija) { this.rezervacija = rezervacija; }
}