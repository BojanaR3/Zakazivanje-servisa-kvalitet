package com.mycompany.njt_mavenproject.exception;

/**
 * Izuzetak koji se baca kada registracija korisnika ne uspe
 * zbog već zauzetog korisničkog imena ili email adrese.
 *
 * @author Bojana
 */
public class RegistrationException extends Exception {
    public RegistrationException(String message) {
        super(message);
    }
}