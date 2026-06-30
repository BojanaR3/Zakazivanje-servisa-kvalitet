/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.njt_mavenproject.repository.impl;

import com.mycompany.njt_mavenproject.entity.impl.Rezervacija;
import com.mycompany.njt_mavenproject.exception.EntityNotFoundException;
import com.mycompany.njt_mavenproject.repository.MyAppRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * Repozitorijum za upravljanje entitetima rezervacija u bazi podataka.
 * Implementira osnovne CRUD operacije i pruža dodatne metode za
 * pretragu rezervacija po vlasniku i proveru preklapanja termina.
 *
 * @author Bojana
 */
@Repository
public class RezervacijaRepository implements MyAppRepository<Rezervacija, Long> {

    @PersistenceContext
    private EntityManager em;

    /**
     * Vraća listu svih rezervacija iz baze podataka,
     * sortirano po datumu od najnovije ka najstarijoj.
     *
     * @return lista svih rezervacija
     */
    @Override
    public List<Rezervacija> findAll() {
        return em.createQuery(
                "SELECT r FROM Rezervacija r ORDER BY r.datum DESC",
                Rezervacija.class
        ).getResultList();
    }

    /**
     * Vraća listu svih rezervacija za datog vlasnika,
     * sortirano po datumu od najnovije ka najstarijoj.
     *
     * @param vlasnikId identifikator vlasnika čije se rezervacije traže
     * @return lista rezervacija datog vlasnika
     */
    public List<Rezervacija> findByVlasnikId(Long vlasnikId) {
        return em.createQuery(
                "SELECT r FROM Rezervacija r WHERE r.vlasnik.id = :vid ORDER BY r.datum DESC",
                Rezervacija.class
        )
        .setParameter("vid", vlasnikId)
        .getResultList();
    }

    /**
     * Pronalazi rezervaciju na osnovu prosleđenog identifikatora.
     *
     * @param id identifikator rezervacije koja se traži
     * @return pronađena rezervacija
     * @throws EntityNotFoundException ako rezervacija sa datim ID-jem ne postoji
     */
    @Override
    public Rezervacija findById(Long id) throws EntityNotFoundException { 
        Rezervacija r = em.find(Rezervacija.class, id);
        if (r == null) throw new EntityNotFoundException("Rezervacija nije pronađena: " + id);
        return r;
    }

    /**
     * Čuva novu rezervaciju ili ažurira postojeću u bazi podataka.
     * Ako entitet nema postavljen ID, kreira se novi zapis,
     * u suprotnom se ažurira postojeći.
     *
     * @param entity rezervacija koja se čuva
     */
    @Override
    @Transactional
    public void save(Rezervacija entity) {
        if (entity.getId() == null) em.persist(entity);
        else em.merge(entity);
    }

    /**
     * Briše rezervaciju sa datim identifikatorom iz baze podataka.
     * Ako rezervacija ne postoji, metoda se završava bez greške.
     *
     * @param id identifikator rezervacije koja se briše
     */
    @Override
    @Transactional
    public void deleteById(Long id) {
        Rezervacija r = em.find(Rezervacija.class, id);
        if (r != null) em.remove(r);
    }

    /**
     * Proverava da li postoji preklapanje termina za dati servis u zadatom vremenskom intervalu.
     * Koristi native SQL upit za preciznu proveru: novi termin se smatra zauzetim ako se
     * vremenski interval (start, end) seče sa bilo kojom postojećom rezervacijom.
     *
     * @param servisId identifikator servisa za koji se proverava dostupnost
     * @param start    početak traženog termina
     * @param end      kraj traženog termina
     * @return {@code true} ako je termin zauzet, {@code false} ako je slobodan
     */
    public boolean existsOverlap(Long servisId, LocalDateTime start, LocalDateTime end) {
        String sql =
            "SELECT COUNT(*) FROM rezervacija r " +
            "WHERE r.servis_id = :sid " +
            "  AND r.datum < :end " +
            "  AND TIMESTAMPADD(MINUTE, r.trajanje_min, r.datum) > :start";
        Number n = (Number) em.createNativeQuery(sql)
            .setParameter("sid", servisId)
            .setParameter("start", start)
            .setParameter("end", end)
            .getSingleResult();
        return n != null && n.longValue() > 0L;
    }
}