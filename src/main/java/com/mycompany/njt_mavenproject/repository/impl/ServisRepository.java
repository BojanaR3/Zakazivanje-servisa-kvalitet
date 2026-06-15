/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.njt_mavenproject.repository.impl;

import com.mycompany.njt_mavenproject.entity.impl.Servis;
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

public class ServisRepository implements MyAppRepository<Servis, Long>{
    
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Servis> findAll() {
        return entityManager.createQuery("SELECT s FROM Servis s", Servis.class).getResultList();
    }

    @Override
    public Servis findById(Long id) throws Exception {
        Servis servis = entityManager.find(Servis.class, id);
        if (servis == null) {
            throw new Exception("Servis nije pronađen!");
        }
        return servis;
    }
    
    public List<Servis> findAllByIds(List<Long> ids) {
        if (ids == null || ids.isEmpty()) return List.of();
        return entityManager.createQuery(
                "SELECT s FROM Servis s WHERE s.id IN :ids", Servis.class)
            .setParameter("ids", ids)
            .getResultList();
    }

    @Override
    @Transactional
    public void save(Servis entity) {
        if (entity.getId() == null) {
            entityManager.persist(entity);
             
        } else {
            entityManager.merge(entity);
        }
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        Servis servis = entityManager.find(Servis.class, id);
        if (servis != null) {
            entityManager.remove(servis);
        }
    }
    
}
