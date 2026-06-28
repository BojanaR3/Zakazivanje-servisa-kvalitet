/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.njt_mavenproject.servis;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

/**
 * Servis za slanje email poruka korisnicima aplikacije.
 * Podržava slanje običnih tekstualnih poruka i HTML emailova.
 * Konfiguracija email servera i adresa pošiljaoca se čitaju iz application.properties.
 *
 * @author Bojana
 */
@Service
public class MailService {

    /** Spring komponenta za slanje email poruka. */
    private final JavaMailSender mail;

    /** Email adresa sa koje se šalju sve poruke, čita se iz konfiguracije. */
    @Value("${app.mail.from}")
    private String from;

    /**
     * Konstruktor koji injektuje JavaMailSender za slanje emailova.
     *
     * @param mail Spring mail sender komponenta
     */
    public MailService(JavaMailSender mail) {
        this.mail = mail;
    }

    /**
     * Šalje običnu tekstualnu email poruku.
     *
     * @param to      email adresa primaoca
     * @param subject naslov email poruke
     * @param text    tekstualni sadržaj poruke
     */
    public void send(String to, String subject, String text) {
        SimpleMailMessage m = new SimpleMailMessage();
        m.setFrom(from);
        m.setTo(to);
        m.setSubject(subject);
        m.setText(text);
        mail.send(m);
    }

    /**
     * Šalje email poruku sa HTML sadržajem.
     * Koristi se za slanje stilizovanih emailova, kao što su
     * poruke za resetovanje lozinke.
     *
     * @param to      email adresa primaoca
     * @param subject naslov email poruke
     * @param html    HTML sadržaj poruke
     * @throws RuntimeException ako dođe do greške prilikom slanja emaila
     */
    public void sendHtml(String to, String subject, String html) {
        try {
            MimeMessage mm = mail.createMimeMessage();
            MimeMessageHelper h = new MimeMessageHelper(mm, "UTF-8");
            h.setFrom(from);
            h.setTo(to);
            h.setSubject(subject);
            h.setText(html, true);
            mail.send(mm);
        } catch (MessagingException e) {
            throw new RuntimeException("Slanje HTML mejla neuspešno", e);
        }
    }
}