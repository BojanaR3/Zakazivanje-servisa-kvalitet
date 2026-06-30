package com.mycompany.njt_mavenproject.exception;

/**
 * Izuzetak koji se baca kada slanje email poruke ne uspe.
 *
 * @author Bojana
 */
public class MailSendException extends RuntimeException {

    /**
     * Konstruktor sa porukom i uzrokom greške.
     *
     * @param message poruka koja opisuje grešku
     * @param cause   originalni izuzetak koji je prouzrokovao grešku
     */
    public MailSendException(String message, Throwable cause) {
        super(message, cause);
    }
}