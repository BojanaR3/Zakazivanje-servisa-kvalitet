/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.njt_mavenproject.repository.impl;

import com.mycompany.njt_mavenproject.entity.impl.Usluga;
import com.mycompany.njt_mavenproject.repository.MyAppRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import java.util.List;
import org.springframework.stereotype.Repository;

/**
 * Repozitorijum za upravljanje entitetima usluga u bazi podataka.
 * Implementira osnovne CRUD operacije za usluge koje servisi mogu nuditi.
 *
 * @author Bojana
 */
@Repository
public class UslugaRepository implements MyAppRepository<Usluga, Long> {

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Vraća listu svih usluga iz baze podataka.
     *
     * @return lista svih usluga
     */
    @Override
    public List<Usluga> findAll() {
        return entityManager.createQuery("SELECT u FROM Usluga u", Usluga.class).getResultList();
    }

    /**
     * Pronalazi uslugu na osnovu prosleđenog identifikatora.
     *
     * @param id identifikator usluge koja se traži
     * @return pronađena usluga
     * @throws Exception ako usluga sa datim ID-jem ne postoji
     */
    @Override
    public Usluga findById(Long id) throws Exception {
        Usluga usluga = entityManager.find(Usluga.class, id);
        if (usluga == null) {
            throw new Exception("Usluga nije pronađena!");
        }
        return usluga;
    }

    /**
     * Čuva novu uslugu ili ažurira postojeću u bazi podataka.
     * Ako entitet nema postavljen ID, kreira se novi zapis,
     * u suprotnom se ažurira postojeći.
     *
     * @param entity usluga koja se čuva
     */
    @Override
    @Transactional
    public void save(Usluga entity) {
        if (entity.getId() == null) {
            entityManager.persist(entity);
        } else {
            entityManager.merge(entity);
        }
    }

    /**
     * Briše uslugu sa datim identifikatorom iz baze podataka.
     * Ako usluga ne postoji, metoda se završava bez greške.
     *
     * @param id identifikator usluge koja se briše
     */
    @Override
    @Transactional
    public void deleteById(Long id) {
        Usluga usluga = entityManager.find(Usluga.class, id);
        if (usluga != null) {
            entityManager.remove(usluga);
        }
    }
}