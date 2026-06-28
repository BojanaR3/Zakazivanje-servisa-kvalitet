package com.mycompany.njt_mavenproject.dto.impl;

import com.mycompany.njt_mavenproject.dto.Dto;
import com.mycompany.njt_mavenproject.entity.impl.Uloga;

/**
 * DTO klasa koja predstavlja vlasnika vozila.
 * Koristi se za prenos osnovnih podataka o korisniku između slojeva aplikacije.
 *
 * @author Bojana
 */
public class VlasnikDto implements Dto {

    private Long id;
    private String ime;
    private String prezime;
    private String username;
    private String email;
    private Uloga uloga;

    /**
     * Podrazumevani konstruktor.
     */
    public VlasnikDto() {}

    /**
     * Konstruktor sa svim parametrima.
     *
     * @param id       jedinstveni identifikator vlasnika
     * @param ime      ime vlasnika
     * @param prezime  prezime vlasnika
     * @param username korisničko ime
     * @param email    email adresa vlasnika
     * @param uloga    uloga korisnika u sistemu (VLASNIK ili ADMIN)
     */
    public VlasnikDto(Long id, String ime, String prezime, String username, String email, Uloga uloga) {
        this.id = id;
        this.ime = ime;
        this.prezime = prezime;
        this.username = username;
        this.email = email;
        this.uloga = uloga;
    }

    /**
     * Vraća ID vlasnika.
     *
     * @return jedinstveni identifikator
     */
    public Long getId() { return id; }

    /**
     * Postavlja ID vlasnika.
     *
     * @param id jedinstveni identifikator
     */
    public void setId(Long id) { this.id = id; }

    /**
     * Vraća ime vlasnika.
     *
     * @return ime
     */
    public String getIme() { return ime; }

    /**
     * Postavlja ime vlasnika.
     *
     * @param ime ime vlasnika
     */
    public void setIme(String ime) { this.ime = ime; }

    /**
     * Vraća prezime vlasnika.
     *
     * @return prezime
     */
    public String getPrezime() { return prezime; }

    /**
     * Postavlja prezime vlasnika.
     *
     * @param prezime prezime vlasnika
     */
    public void setPrezime(String prezime) { this.prezime = prezime; }

    /**
     * Vraća korisničko ime vlasnika.
     *
     * @return korisničko ime
     */
    public String getUsername() { return username; }

    /**
     * Postavlja korisničko ime vlasnika.
     *
     * @param username korisničko ime
     */
    public void setUsername(String username) { this.username = username; }

    /**
     * Vraća email adresu vlasnika.
     *
     * @return email adresa
     */
    public String getEmail() { return email; }

    /**
     * Postavlja email adresu vlasnika.
     *
     * @param email email adresa
     */
    public void setEmail(String email) { this.email = email; }

    /**
     * Vraća ulogu korisnika u sistemu.
     *
     * @return uloga (VLASNIK ili ADMIN)
     */
    public Uloga getUloga() { return uloga; }

    /**
     * Postavlja ulogu korisnika u sistemu.
     *
     * @param uloga uloga korisnika
     */
    public void setUloga(Uloga uloga) { this.uloga = uloga; }
}