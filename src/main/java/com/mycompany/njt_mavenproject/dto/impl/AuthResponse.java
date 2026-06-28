package com.mycompany.njt_mavenproject.dto.impl;

/**
 * DTO klasa koja predstavlja odgovor na uspešnu autentifikaciju.
 * Sadrži JWT token i osnovne podatke o prijavljenom korisniku.
 *
 * @author Bojana
 */
public class AuthResponse {

    private String token;
    private VlasnikDto user;

    /**
     * Podrazumevani konstruktor.
     */
    public AuthResponse() {}

    /**
     * Konstruktor sa svim parametrima.
     *
     * @param token JWT token generisan nakon uspešne prijave
     * @param user  podaci o prijavljenom korisniku
     */
    public AuthResponse(String token, VlasnikDto user) {
        this.token = token;
        this.user = user;
    }

    /**
     * Vraća JWT token.
     *
     * @return JWT token
     */
    public String getToken() { return token; }

    /**
     * Postavlja JWT token.
     *
     * @param token JWT token
     */
    public void setToken(String token) { this.token = token; }

    /**
     * Vraća podatke o prijavljenom korisniku.
     *
     * @return DTO objekat prijavljenog korisnika
     */
    public VlasnikDto getUser() { return user; }

    /**
     * Postavlja podatke o prijavljenom korisniku.
     *
     * @param user DTO objekat korisnika
     */
    public void setUser(VlasnikDto user) { this.user = user; }
}