package com.mycompany.njt_mavenproject.exception;

/**
 * Izuzetak koji se baca kada traženi entitet ne postoji u bazi podataka.
 *
 * @author Bojana
 */
public class EntityNotFoundException extends Exception {

    /**
     * Konstruktor sa porukom o grešci.
     *
     * @param message poruka koja opisuje grešku
     */
    public EntityNotFoundException(String message) {
        super(message);
    }
} 