/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.njt_mavenproject.repository.impl;

import com.mycompany.njt_mavenproject.entity.impl.Vozilo;
import com.mycompany.njt_mavenproject.repository.MyAppRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import java.util.List;
import org.springframework.stereotype.Repository;

/**
 * Repozitorijum za upravljanje entitetima vozila u bazi podataka.
 * Implementira osnovne CRUD operacije i pruža dodatne metode za
 * pretragu vozila po vlasniku.
 *
 * @author Bojana
 */
@Repository
public class VoziloRepository implements MyAppRepository<Vozilo, Long> {

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Vraća listu svih vozila iz baze podataka.
     *
     * @return lista svih vozila
     */
    @Override
    public List<Vozilo> findAll() {
        return entityManager
                .createQuery("SELECT v FROM Vozilo v", Vozilo.class)
                .getResultList();
    }

    /**
     * Pronalazi vozilo na osnovu prosleđenog identifikatora.
     *
     * @param id identifikator vozila koje se traži
     * @return pronađeno vozilo
     * @throws Exception ako vozilo sa datim ID-jem ne postoji
     */
    @Override
    public Vozilo findById(Long id) throws Exception {
        Vozilo vozilo = entityManager.find(Vozilo.class, id);
        if (vozilo == null) throw new Exception("Vozilo nije pronađeno: " + id);
        return vozilo;
    }

    /**
     * Čuva novo vozilo ili ažurira postojeće u bazi podataka.
     * Ako entitet nema postavljen ID, kreira se novi zapis,
     * u suprotnom se ažurira postojeći.
     *
     * @param entity vozilo koje se čuva
     */
    @Override
    @Transactional
    public void save(Vozilo entity) {
        if (entity.getId() == null) {
            entityManager.persist(entity);
        } else {
            entityManager.merge(entity);
        }
    }

    /**
     * Briše vozilo sa datim identifikatorom iz baze podataka.
     * Ako vozilo ne postoji, metoda se završava bez greške.
     *
     * @param id identifikator vozila koje se briše
     */
    @Override
    @Transactional
    public void deleteById(Long id) {
        Vozilo vozilo = entityManager.find(Vozilo.class, id);
        if (vozilo != null) entityManager.remove(vozilo);
    }

    /**
     * Vraća listu svih vozila datog vlasnika, sortiranu po ID-u od najnovijeg.
     *
     * @param vlasnikId identifikator vlasnika čija se vozila traže
     * @return lista vozila datog vlasnika
     */
    public List<Vozilo> findByVlasnikId(Long vlasnikId) {
        return entityManager.createQuery(
                "SELECT v FROM Vozilo v WHERE v.vlasnik.id = :vid ORDER BY v.id DESC",
                Vozilo.class
        ).setParameter("vid", vlasnikId)
         .getResultList();
    }

    /**
     * Pronalazi poslednje dodato vozilo datog vlasnika.
     *
     * @param vlasnikId identifikator vlasnika
     * @return poslednje vozilo vlasnika, ili {@code null} ako vlasnik nema vozila
     */
    public Vozilo findLatestByVlasnikId(Long vlasnikId) {
        List<Vozilo> l = entityManager.createQuery(
                "SELECT v FROM Vozilo v WHERE v.vlasnik.id = :vid ORDER BY v.id DESC",
                Vozilo.class
        ).setParameter("vid", vlasnikId)
         .setMaxResults(1)
         .getResultList();
        return l.isEmpty() ? null : l.get(0);
    }

    /**
     * Vraća listu svih vozila vlasnika pronađenog po korisničkom imenu,
     * sortiranu po ID-u od najnovijeg.
     *
     * @param username korisničko ime vlasnika čija se vozila traže
     * @return lista vozila datog vlasnika
     */
    public List<Vozilo> findByVlasnikUsername(String username) {
        return entityManager.createQuery(
                "SELECT v FROM Vozilo v WHERE v.vlasnik.username = :u ORDER BY v.id DESC",
                Vozilo.class
        ).setParameter("u", username)
         .getResultList();
    }
}