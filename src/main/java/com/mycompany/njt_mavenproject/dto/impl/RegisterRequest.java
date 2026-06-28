package com.mycompany.njt_mavenproject.dto.impl;

import jakarta.validation.constraints.*;

/**
 * DTO klasa koja predstavlja zahtev za registraciju novog korisnika.
 * Sadrži sve podatke potrebne za kreiranje korisničkog naloga.
 *
 * @author Bojana
 */
public class RegisterRequest {

    @NotBlank @Size(min = 3, max = 50)
    private String username;

    @NotBlank @Email
    private String email;

    @NotBlank @Size(min = 6, max = 100)
    private String lozinka;

    @NotBlank @Size(min = 2, max = 50)
    private String ime;

    @NotBlank @Size(min = 2, max = 50)
    private String prezime;

    /**
     * Vraća korisničko ime.
     *
     * @return korisničko ime
     */
    public String getUsername() { return username; }

    /**
     * Postavlja korisničko ime.
     *
     * @param username korisničko ime (3-50 karaktera)
     */
    public void setUsername(String username) { this.username = username; }

    /**
     * Vraća email adresu korisnika.
     *
     * @return email adresa
     */
    public String getEmail() { return email; }

    /**
     * Postavlja email adresu korisnika.
     *
     * @param email validna email adresa
     */
    public void setEmail(String email) { this.email = email; }

    /**
     * Vraća lozinku korisnika.
     *
     * @return lozinka
     */
    public String getLozinka() { return lozinka; }

    /**
     * Postavlja lozinku korisnika.
     *
     * @param lozinka lozinka (6-100 karaktera)
     */
    public void setLozinka(String lozinka) { this.lozinka = lozinka; }

    /**
     * Vraća ime korisnika.
     *
     * @return ime
     */
    public String getIme() { return ime; }

    /**
     * Postavlja ime korisnika.
     *
     * @param ime ime korisnika (2-50 karaktera)
     */
    public void setIme(String ime) { this.ime = ime; }

    /**
     * Vraća prezime korisnika.
     *
     * @return prezime
     */
    public String getPrezime() { return prezime; }

    /**
     * Postavlja prezime korisnika.
     *
     * @param prezime prezime korisnika (2-50 karaktera)
     */
    public void setPrezime(String prezime) { this.prezime = prezime; }
}