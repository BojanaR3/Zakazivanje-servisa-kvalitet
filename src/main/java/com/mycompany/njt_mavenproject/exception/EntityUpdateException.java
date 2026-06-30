package com.mycompany.njt_mavenproject.exception;

/**
 * Izuzetak koji se baca kada ažuriranje ili obrada postojećeg entiteta ne uspe,
 * najčešće kada entitet sa zadatim ID-jem ne postoji u bazi podataka.
 *
 * @author Bojana
 */
public class EntityUpdateException extends RuntimeException {

    /**
     * Konstruktor sa porukom o grešci.
     *
     * @param message poruka koja opisuje grešku
     */
    public EntityUpdateException(String message) {
        super(message);
    }
}