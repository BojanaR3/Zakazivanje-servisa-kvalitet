/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.njt_mavenproject.repository.impl;

import com.mycompany.njt_mavenproject.entity.impl.PasswordResetToken;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

/**
 * Repozitorijum za upravljanje tokenima za resetovanje lozinke.
 * Tokeni se koriste u procesu oporavka naloga kada korisnik
 * zaboravi lozinku.
 *
 * @author Bojana
 */
@Repository
public class PasswordResetTokenRepository {

    @PersistenceContext
    private EntityManager em;

    /**
     * Čuva novi token za resetovanje lozinke u bazi podataka.
     *
     * @param prt token za resetovanje lozinke koji se čuva
     */
    @Transactional
    public void save(PasswordResetToken prt) {
        em.persist(prt);
    }

    /**
     * Pronalazi token za resetovanje lozinke na osnovu vrednosti tokena.
     *
     * @param token string vrednost tokena koji se traži
     * @return pronađeni token, ili {@code null} ako token ne postoji
     */
    public PasswordResetToken find(String token) {
        return em.find(PasswordResetToken.class, token);
    }

    /**
     * Briše token za resetovanje lozinke iz baze podataka.
     * Ako entitet nije u trenutnom persistence kontekstu, najpre se
     * merge-uje pa zatim briše.
     *
     * @param prt token za resetovanje lozinke koji se briše
     */
    @Transactional
    public void delete(PasswordResetToken prt) {
        em.remove(em.contains(prt) ? prt : em.merge(prt));
    }
}