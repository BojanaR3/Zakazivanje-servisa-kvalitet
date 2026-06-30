/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.njt_mavenproject.repository.impl;

import com.mycompany.njt_mavenproject.entity.impl.StavkaRezervacije;
import com.mycompany.njt_mavenproject.exception.EntityNotFoundException;
import com.mycompany.njt_mavenproject.repository.MyAppRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import java.util.List;
import org.springframework.stereotype.Repository;

/**
 * Repozitorijum za upravljanje entitetima stavki rezervacije u bazi podataka.
 * Svaka stavka predstavlja jednu uslugu unutar rezervacije,
 * zajedno sa količinom i zaključanom cenom.
 *
 * @author Bojana
 */
@Repository
public class StavkaRezervacijeRepository implements MyAppRepository<StavkaRezervacije, Long> {

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Vraća listu svih stavki rezervacije iz baze podataka.
     *
     * @return lista svih stavki rezervacije
     */
    @Override
    public List<StavkaRezervacije> findAll() {
        return entityManager.createQuery("SELECT s FROM StavkaRezervacije s", StavkaRezervacije.class).getResultList();
    }

    /**
     * Pronalazi stavku rezervacije na osnovu prosleđenog identifikatora.
     *
     * @param id identifikator stavke rezervacije koja se traži
     * @return pronađena stavka rezervacije
     * @throws EntityNotFoundException ako stavka sa datim ID-jem ne postoji
     */
    @Override
    public StavkaRezervacije findById(Long id) throws EntityNotFoundException {
        StavkaRezervacije stavka = entityManager.find(StavkaRezervacije.class, id);
        if (stavka == null) {
            throw new EntityNotFoundException("Stavka rezervacije nije pronađena!");
        }
        return stavka;
    }

    /**
     * Čuva novu stavku rezervacije ili ažurira postojeću u bazi podataka.
     * Ako entitet nema postavljen ID, kreira se novi zapis,
     * u suprotnom se ažurira postojeći.
     *
     * @param entity stavka rezervacije koja se čuva
     */
    @Override
    @Transactional
    public void save(StavkaRezervacije entity) {
        if (entity.getId() == null) {
            entityManager.persist(entity);
        } else {
            entityManager.merge(entity);
        }
    }

    /**
     * Briše stavku rezervacije sa datim identifikatorom iz baze podataka.
     * Ako stavka ne postoji, metoda se završava bez greške.
     *
     * @param id identifikator stavke rezervacije koja se briše
     */
    @Override
    @Transactional
    public void deleteById(Long id) {
        StavkaRezervacije stavka = entityManager.find(StavkaRezervacije.class, id);
        if (stavka != null) {
            entityManager.remove(stavka);
        }
    }
}