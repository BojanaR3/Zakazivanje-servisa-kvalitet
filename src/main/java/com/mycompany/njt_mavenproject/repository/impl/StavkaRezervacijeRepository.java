/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.njt_mavenproject.repository.impl;

import com.mycompany.njt_mavenproject.entity.impl.StavkaRezervacije;
import com.mycompany.njt_mavenproject.repository.MyAppRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import java.util.List;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Korisnik
 */
@Repository

public class StavkaRezervacijeRepository implements MyAppRepository<StavkaRezervacije, Long>{
    
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<StavkaRezervacije> findAll() {
        return entityManager.createQuery("SELECT s FROM StavkaRezervacije s", StavkaRezervacije.class).getResultList();
    }

    @Override
    public StavkaRezervacije findById(Long id) throws Exception {
        StavkaRezervacije stavka = entityManager.find(StavkaRezervacije.class, id);
        if (stavka == null) {
            throw new Exception("Stavka rezervacije nije pronađena!");
        }
        return stavka;
    }

    @Override
    @Transactional
    public void save(StavkaRezervacije entity) {
        if (entity.getId() == null) {
            entityManager.persist(entity);
        } else {
            entityManager.merge(entity);
        }
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        StavkaRezervacije stavka = entityManager.find(StavkaRezervacije.class, id);
        if (stavka != null) {
            entityManager.remove(stavka);
        }
    }
    
}
