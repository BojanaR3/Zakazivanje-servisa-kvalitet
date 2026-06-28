package com.mycompany.njt_mavenproject.entity.impl;

/**
 * Enumeracija koja predstavlja moguće uloge korisnika u sistemu.
 *
 * @author Bojana
 */
public enum Uloga {

    /** Administrator sistema sa punim pristupom svim funkcionalnostima. */
    ADMIN,

    /** Vlasnik vozila koji može da kreira i upravlja sopstvenim rezervacijama. */
    VLASNIK
}