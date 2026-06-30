package com.mycompany.njt_mavenproject.exception;

/**
 * Izuzetak koji se baca kada korisnik sa zadatim korisničkim imenom ne postoji.
 *
 * @author Bojana
 */
public class UserNotFoundException extends Exception {
    public UserNotFoundException(String message) {
        super(message);
    }
}