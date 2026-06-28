package com.mycompany.njt_mavenproject.entity.impl;

import com.mycompany.njt_mavenproject.entity.MyEntity;
import jakarta.persistence.*;

/**
 * Entitet koji predstavlja uslugu koju servis nudi.
 * Sadrži informacije o nazivu, trajanju i referentnoj ceni usluge.
 *
 * @author Bojana
 */
@Entity
@Table(name = "usluga")
public class Usluga implements MyEntity {

    /** Jedinstveni identifikator usluge. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Naziv usluge. */
    private String naziv;

    /** Trajanje usluge izraženo u zadatoj jedinici mere. */
    private Integer trajanje;

    /** Jedinica mere trajanja usluge (npr. min, h). */
    private String jedinicaMere;

    /** Referentna cena usluge. Ne koristi se za obračun, samo informativno. */
    @Column
    private Double cena;

    /**
     * Podrazumevani konstruktor.
     */
    public Usluga() {}

    /**
     * Konstruktor koji kreira referencu na uslugu sa zadatim ID-em.
     *
     * @param id jedinstveni identifikator usluge
     */
    public Usluga(Long id) { this.id = id; }

    /**
     * Konstruktor sa osnovnim parametrima.
     *
     * @param id           jedinstveni identifikator usluge
     * @param naziv        naziv usluge
     * @param trajanje     trajanje usluge
     * @param jedinicaMere jedinica mere trajanja
     */
    public Usluga(Long id, String naziv, Integer trajanje, String jedinicaMere) {
        this.id = id;
        this.naziv = naziv;
        this.trajanje = trajanje;
        this.jedinicaMere = jedinicaMere;
    }

    /**
     * Vraća ID usluge.
     *
     * @return jedinstveni identifikator
     */
    public Long getId() { return id; }

    /**
     * Postavlja ID usluge.
     *
     * @param id jedinstveni identifikator
     */
    public void setId(Long id) { this.id = id; }

    /**
     * Vraća naziv usluge.
     *
     * @return naziv usluge
     */
    public String getNaziv() { return naziv; }

    /**
     * Postavlja naziv usluge.
     *
     * @param naziv naziv usluge
     */
    public void setNaziv(String naziv) { this.naziv = naziv; }

    /**
     * Vraća trajanje usluge.
     *
     * @return trajanje usluge
     */
    public Integer getTrajanje() { return trajanje; }

    /**
     * Postavlja trajanje usluge.
     *
     * @param trajanje trajanje usluge
     */
    public void setTrajanje(Integer trajanje) { this.trajanje = trajanje; }

    /**
     * Vraća jedinicu mere trajanja usluge.
     *
     * @return jedinica mere (npr. min, h)
     */
    public String getJedinicaMere() { return jedinicaMere; }

    /**
     * Postavlja jedinicu mere trajanja usluge.
     *
     * @param jedinicaMere jedinica mere trajanja
     */
    public void setJedinicaMere(String jedinicaMere) { this.jedinicaMere = jedinicaMere; }

    /**
     * Vraća referentnu cenu usluge.
     *
     * @return referentna cena
     */
    public Double getCena() { return cena; }

    /**
     * Postavlja referentnu cenu usluge.
     *
     * @param cena referentna cena usluge
     */
    public void setCena(Double cena) { this.cena = cena; }
}