/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.njt_mavenproject.repository.impl;

import com.mycompany.njt_mavenproject.entity.impl.VerificationToken;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

/**
 * Repozitorijum za upravljanje tokenima za verifikaciju email adrese.
 * Tokeni se kreiraju prilikom registracije korisnika i koriste se
 * za potvrdu naloga putem linka poslatog na email.
 *
 * @author Bojana
 */
@Repository
public class VerificationTokenRepository {

    @PersistenceContext
    private EntityManager em;

    /**
     * Čuva novi token za verifikaciju email adrese u bazi podataka.
     *
     * @param vt token za verifikaciju koji se čuva
     */
    @Transactional
    public void save(VerificationToken vt) {
        em.persist(vt);
    }

    /**
     * Pronalazi token za verifikaciju na osnovu vrednosti tokena.
     *
     * @param token string vrednost tokena koji se traži
     * @return pronađeni token, ili {@code null} ako token ne postoji
     */
    public VerificationToken find(String token) {
        return em.find(VerificationToken.class, token);
    }

    /**
     * Briše token za verifikaciju iz baze podataka.
     * Ako entitet nije u trenutnom persistence kontekstu, najpre se
     * merge-uje pa zatim briše.
     *
     * @param vt token za verifikaciju koji se briše
     */
    @Transactional
    public void delete(VerificationToken vt) {
        em.remove(em.contains(vt) ? vt : em.merge(vt));
    }
}