package com.mycompany.njt_mavenproject.entity.impl;

/**
 * Enumeracija koja predstavlja moguće statuse rezervacije.
 *
 * @author Bojana
 */
public enum StatusRezervacije {

    /** Rezervacija je kreirana i čeka potvrdu. */
    CREATED,

    /** Rezervacija je potvrđena od strane servisa. */
    CONFIRMED,

    /** Rezervacija je otkazana. */
    CANCELED,

    /** Rezervacija je završena. */
    COMPLETED
}