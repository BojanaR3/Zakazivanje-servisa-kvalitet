/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.njt_mavenproject.repository.impl;

import com.mycompany.njt_mavenproject.entity.impl.Vlasnik;
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

public class VlasnikRepository implements MyAppRepository<Vlasnik, Long>{

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<Vlasnik> findAll() {
        return em.createQuery("SELECT v FROM Vlasnik v", Vlasnik.class).getResultList();
    }

    @Override
    public Vlasnik findById(Long id) throws Exception {
        Vlasnik v = em.find(Vlasnik.class, id);
        if (v == null) throw new Exception("Vlasnik nije pronađen!");
        return v;
    }

    @Transactional
    @Override
    public void save(Vlasnik entity) {
        if (entity.getId() == null) em.persist(entity);
        else em.merge(entity);
    }

    @Transactional
    @Override
    public void deleteById(Long id) {
        Vlasnik v = em.find(Vlasnik.class, id);
        if (v != null) em.remove(v);
    }

    // --- DODATO: finder-i i exists-ovi ---

    public Vlasnik findByEmail(String email) {
        List<Vlasnik> r = em.createQuery(
                "SELECT v FROM Vlasnik v WHERE v.email = :e", Vlasnik.class)
                .setParameter("e", email)
                .setMaxResults(1)
                .getResultList();
        return r.isEmpty() ? null : r.get(0);
    }

    public Vlasnik findByUsername(String username) {
        List<Vlasnik> r = em.createQuery(
                "SELECT v FROM Vlasnik v WHERE v.username = :u", Vlasnik.class)
                .setParameter("u", username)
                .setMaxResults(1)
                .getResultList();
        return r.isEmpty() ? null : r.get(0);
    }

    public boolean existsByEmail(String email) {
        Long cnt = em.createQuery(
                "SELECT COUNT(v) FROM Vlasnik v WHERE v.email = :e", Long.class)
                .setParameter("e", email)
                .getSingleResult();
        return cnt != null && cnt > 0;
    }

    public boolean existsByUsername(String username) {
        Long cnt = em.createQuery(
                "SELECT COUNT(v) FROM Vlasnik v WHERE v.username = :u", Long.class)
                .setParameter("u", username)
                .getSingleResult();
        return cnt != null && cnt > 0;
    }

    @Transactional
    public int updateEnabled(Long id, boolean enabled) {
        return em.createQuery("UPDATE Vlasnik v SET v.enabled = :en WHERE v.id = :id")
                 .setParameter("en", enabled)
                 .setParameter("id", id)
                 .executeUpdate();
    }

    @Transactional
    public int updatePassword(Long id, String passwordHash) {
        return em.createQuery("UPDATE Vlasnik v SET v.lozinka = :p WHERE v.id = :id")
                 .setParameter("p", passwordHash)
                 .setParameter("id", id)
                 .executeUpdate();
    }
    
    
    
}
