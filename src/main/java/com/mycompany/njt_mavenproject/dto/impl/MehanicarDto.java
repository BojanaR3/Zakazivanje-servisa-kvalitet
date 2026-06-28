package com.mycompany.njt_mavenproject.dto.impl;

import com.mycompany.njt_mavenproject.dto.Dto;
import jakarta.validation.constraints.*;

/**
 * DTO klasa koja predstavlja mehaničara.
 * Koristi se za prenos podataka o mehaničaru između slojeva aplikacije.
 *
 * @author Bojana
 */
public class MehanicarDto implements Dto {

    private Long id;

    @NotBlank(message = "Ime ne sme biti prazno.")
    private String ime;

    @NotBlank(message = "Prezime ne sme biti prazno.")
    private String prezime;

    private String specijalnost;
    private String telefon;

    @NotNull(message = "servisId je obavezan.")
    private Long servisId;

    /**
     * Podrazumevani konstruktor.
     */
    public MehanicarDto() {}

    /**
     * Konstruktor sa svim parametrima.
     *
     * @param id          jedinstveni identifikator mehaničara
     * @param ime         ime mehaničara
     * @param prezime     prezime mehaničara
     * @param specijalnost specijalnost mehaničara
     * @param telefon     kontakt telefon mehaničara
     * @param servisId    ID servisa u kome mehaničar radi
     */
    public MehanicarDto(Long id, String ime, String prezime, String specijalnost, String telefon, Long servisId) {
        this.id = id;
        this.ime = ime;
        this.prezime = prezime;
        this.specijalnost = specijalnost;
        this.telefon = telefon;
        this.servisId = servisId;
    }

    /**
     * Vraća ID mehaničara.
     *
     * @return jedinstveni identifikator
     */
    public Long getId() { return id; }

    /**
     * Postavlja ID mehaničara.
     *
     * @param id jedinstveni identifikator
     */
    public void setId(Long id) { this.id = id; }

    /**
     * Vraća ime mehaničara.
     *
     * @return ime
     */
    public String getIme() { return ime; }

    /**
     * Postavlja ime mehaničara.
     *
     * @param ime ime mehaničara
     */
    public void setIme(String ime) { this.ime = ime; }

    /**
     * Vraća prezime mehaničara.
     *
     * @return prezime
     */
    public String getPrezime() { return prezime; }

    /**
     * Postavlja prezime mehaničara.
     *
     * @param prezime prezime mehaničara
     */
    public void setPrezime(String prezime) { this.prezime = prezime; }

    /**
     * Vraća specijalnost mehaničara.
     *
     * @return specijalnost
     */
    public String getSpecijalnost() { return specijalnost; }

    /**
     * Postavlja specijalnost mehaničara.
     *
     * @param specijalnost specijalnost mehaničara
     */
    public void setSpecijalnost(String specijalnost) { this.specijalnost = specijalnost; }

    /**
     * Vraća telefon mehaničara.
     *
     * @return telefon
     */
    public String getTelefon() { return telefon; }

    /**
     * Postavlja telefon mehaničara.
     *
     * @param telefon kontakt telefon
     */
    public void setTelefon(String telefon) { this.telefon = telefon; }

    /**
     * Vraća ID servisa u kome mehaničar radi.
     *
     * @return ID servisa
     */
    public Long getServisId() { return servisId; }

    /**
     * Postavlja ID servisa mehaničara.
     *
     * @param servisId ID servisa
     */
    public void setServisId(Long servisId) { this.servisId = servisId; }
}