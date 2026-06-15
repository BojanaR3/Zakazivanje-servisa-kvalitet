/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.njt_mavenproject.repository.impl;


/**
 *
 * @author Korisnik
 */


import com.mycompany.njt_mavenproject.entity.impl.Vozilo;
import com.mycompany.njt_mavenproject.repository.MyAppRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import java.util.List;
import org.springframework.stereotype.Repository;



import com.mycompany.njt_mavenproject.entity.impl.Vozilo;
import com.mycompany.njt_mavenproject.repository.MyAppRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class VoziloRepository implements MyAppRepository<Vozilo, Long> {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Vozilo> findAll() {
        return entityManager
                .createQuery("SELECT v FROM Vozilo v", Vozilo.class)
                .getResultList();
    }

    @Override
    public Vozilo findById(Long id) throws Exception {
        Vozilo vozilo = entityManager.find(Vozilo.class, id);
        if (vozilo == null) throw new Exception("Vozilo nije pronađeno: " + id);
        return vozilo;
    }

    @Override
    @Transactional
    public void save(Vozilo entity) {
        if (entity.getId() == null) {
            entityManager.persist(entity);
        } else {
            entityManager.merge(entity);
        }
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        Vozilo vozilo = entityManager.find(Vozilo.class, id);
        if (vozilo != null) entityManager.remove(vozilo);
    }

    // ===== DODATO: pronalazak po vlasniku =====

    /** Sva vozila vlasnika po ID-u vlasnika (novija prva). */
    public List<Vozilo> findByVlasnikId(Long vlasnikId) {
        return entityManager.createQuery(
                "SELECT v FROM Vozilo v WHERE v.vlasnik.id = :vid ORDER BY v.id DESC",
                Vozilo.class
        ).setParameter("vid", vlasnikId)
         .getResultList();
    }

    /** Poslednje (najskorije) vozilo vlasnika po ID-u. */
    public Vozilo findLatestByVlasnikId(Long vlasnikId) {
        List<Vozilo> l = entityManager.createQuery(
                "SELECT v FROM Vozilo v WHERE v.vlasnik.id = :vid ORDER BY v.id DESC",
                Vozilo.class
        ).setParameter("vid", vlasnikId)
         .setMaxResults(1)
         .getResultList();
        return l.isEmpty() ? null : l.get(0);
    }

    /** Sva vozila vlasnika po USERNAME-u vlasnika (novija prva). */
    public List<Vozilo> findByVlasnikUsername(String username) {
        return entityManager.createQuery(
                "SELECT v FROM Vozilo v WHERE v.vlasnik.username = :u ORDER BY v.id DESC",
                Vozilo.class
        ).setParameter("u", username)
         .getResultList();
    }
}


