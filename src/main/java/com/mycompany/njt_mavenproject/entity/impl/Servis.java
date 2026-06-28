package com.mycompany.njt_mavenproject.entity.impl;

import com.mycompany.njt_mavenproject.entity.MyEntity;
import jakarta.persistence.*;

/**
 * Entitet koji predstavlja servis za vozila.
 * Sadrži osnovne informacije o servisu kao što su naziv, adresa i telefon.
 *
 * @author Bojana
 */
@Entity
@Table(name = "servis")
public class Servis implements MyEntity {

    /** Jedinstveni identifikator servisa. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Naziv servisa. */
    private String naziv;

    /** Adresa servisa. */
    private String adresa;

    /** Kontakt telefon servisa. */
    private String telefon;

    /**
     * Podrazumevani konstruktor.
     */
    public Servis() {}

    /**
     * Konstruktor koji kreira referencu na servis sa zadatim ID-em.
     *
     * @param id jedinstveni identifikator servisa
     */
    public Servis(Long id) { this.id = id; }

    /**
     * Konstruktor sa svim parametrima.
     *
     * @param id      jedinstveni identifikator servisa
     * @param naziv   naziv servisa
     * @param adresa  adresa servisa
     * @param telefon kontakt telefon servisa
     */
    public Servis(Long id, String naziv, String adresa, String telefon) {
        this.id = id;
        this.naziv = naziv;
        this.adresa = adresa;
        this.telefon = telefon;
    }

    /**
     * Vraća ID servisa.
     *
     * @return jedinstveni identifikator
     */
    public Long getId() { return id; }

    /**
     * Postavlja ID servisa.
     *
     * @param id jedinstveni identifikator
     */
    public void setId(Long id) { this.id = id; }

    /**
     * Vraća naziv servisa.
     *
     * @return naziv servisa
     */
    public String getNaziv() { return naziv; }

    /**
     * Postavlja naziv servisa.
     *
     * @param naziv naziv servisa
     */
    public void setNaziv(String naziv) { this.naziv = naziv; }

    /**
     * Vraća adresu servisa.
     *
     * @return adresa servisa
     */
    public String getAdresa() { return adresa; }

    /**
     * Postavlja adresu servisa.
     *
     * @param adresa adresa servisa
     */
    public void setAdresa(String adresa) { this.adresa = adresa; }

    /**
     * Vraća kontakt telefon servisa.
     *
     * @return telefon
     */
    public String getTelefon() { return telefon; }

    /**
     * Postavlja kontakt telefon servisa.
     *
     * @param telefon kontakt telefon
     */
    public void setTelefon(String telefon) { this.telefon = telefon; }
}