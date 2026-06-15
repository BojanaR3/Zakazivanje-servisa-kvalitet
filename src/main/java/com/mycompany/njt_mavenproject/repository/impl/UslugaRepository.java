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
 *
 * @author Korisnik
 */
@Repository

public class UslugaRepository implements MyAppRepository<Usluga, Long>{
    
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Usluga> findAll() {
        return entityManager.createQuery("SELECT u FROM Usluga u", Usluga.class).getResultList();
    }

    @Override
    public Usluga findById(Long id) throws Exception {
        Usluga usluga = entityManager.find(Usluga.class, id);
        if (usluga == null) {
            throw new Exception("Usluga nije pronađena!");
        }
        return usluga;
    }

    @Override
    @Transactional
    public void save(Usluga entity) {
        if (entity.getId() == null) {
            entityManager.persist(entity);
             
        } else {
            entityManager.merge(entity);
        }
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        Usluga usluga = entityManager.find(Usluga.class, id);
        if (usluga != null) {
            entityManager.remove(usluga);
        }
    }
    
}
