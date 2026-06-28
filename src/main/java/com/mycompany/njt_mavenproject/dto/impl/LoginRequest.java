package com.mycompany.njt_mavenproject.dto.impl;

import jakarta.validation.constraints.*;

/**
 * DTO klasa koja predstavlja zahtev za prijavu korisnika.
 * Sadrži korisničko ime i lozinku potrebne za autentifikaciju.
 *
 * @author Bojana
 */
public class LoginRequest {

    @NotBlank
    private String username;

    @NotBlank
    private String lozinka;

    /**
     * Vraća korisničko ime.
     *
     * @return korisničko ime
     */
    public String getUsername() { return username; }

    /**
     * Postavlja korisničko ime.
     *
     * @param username korisničko ime
     */
    public void setUsername(String username) { this.username = username; }

    /**
     * Vraća lozinku korisnika.
     *
     * @return lozinka
     */
    public String getLozinka() { return lozinka; }

    /**
     * Postavlja lozinku korisnika.
     *
     * @param lozinka lozinka korisnika
     */
    public void setLozinka(String lozinka) { this.lozinka = lozinka; }
}