/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.njt_mavenproject.repository.impl;

/**
 *
 * @author Korisnik
 */


import com.mycompany.njt_mavenproject.entity.impl.Servis;
import com.mycompany.njt_mavenproject.entity.impl.ServisUsluga;
import com.mycompany.njt_mavenproject.entity.impl.Usluga;
import com.mycompany.njt_mavenproject.repository.MyAppRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ServisUslugaRepository implements MyAppRepository<ServisUsluga, Long> {

    @PersistenceContext private EntityManager em;

    @Override
    public List<ServisUsluga> findAll() {
        return em.createQuery("select su from ServisUsluga su", ServisUsluga.class).getResultList();
    }

    @Override
    public ServisUsluga findById(Long id) throws Exception {
        ServisUsluga su = em.find(ServisUsluga.class, id);
        if (su == null) throw new Exception("ServisUsluga nije pronađena: " + id);
        return su;
    }

    @Override @Transactional
    public void save(ServisUsluga entity) {
        if (entity.getId() == null) em.persist(entity);
        else em.merge(entity);
    }

    @Override @Transactional
    public void deleteById(Long id) {
        ServisUsluga su = em.find(ServisUsluga.class, id);
        if (su != null) em.remove(su);
    }

    // --- dodatno ---

    public List<ServisUsluga> findByServisId(Long servisId) {
        return em.createQuery(
            "select su from ServisUsluga su join fetch su.usluga " +
            "where su.servis.id=:sid order by su.usluga.naziv", ServisUsluga.class)
            .setParameter("sid", servisId)
            .getResultList();
    }

    public Double findCena(Long servisId, Long uslugaId) {
        List<Double> l = em.createQuery(
            "select su.cena from ServisUsluga su " +
            "where su.servis.id=:sid and su.usluga.id=:uid", Double.class)
            .setParameter("sid", servisId)
            .setParameter("uid", uslugaId)
            .setMaxResults(1)
            .getResultList();
        return l.isEmpty() ? null : l.get(0);
    }

    @Transactional
    public void upsert(Long servisId, Long uslugaId, Double cena) {
        int updated = em.createQuery(
            "update ServisUsluga su set su.cena=:c " +
            "where su.servis.id=:sid and su.usluga.id=:uid")
            .setParameter("c", cena)
            .setParameter("sid", servisId)
            .setParameter("uid", uslugaId)
            .executeUpdate();

        if (updated == 0) {
            em.persist(new ServisUsluga(
                em.getReference(Servis.class, servisId),
                em.getReference(Usluga.class, uslugaId),
                cena
            ));
        }
    }

    @Transactional
    public void replaceAllForServis(Long servisId, List<ServisUsluga> novi) {
        em.createQuery("delete from ServisUsluga su where su.servis.id=:sid")
          .setParameter("sid", servisId).executeUpdate();
        for (ServisUsluga su : novi) em.persist(su);
    }

    @Transactional
    public void deleteByServis(Long servisId) {
        em.createQuery("delete from ServisUsluga su where su.servis.id=:sid")
          .setParameter("sid", servisId).executeUpdate();
    }

    @Transactional
    public void deleteByUsluga(Long uslugaId) {
        em.createQuery("delete from ServisUsluga su where su.usluga.id=:uid")
          .setParameter("uid", uslugaId).executeUpdate();
    }
}



