/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.njt_mavenproject.repository.impl;

import com.mycompany.njt_mavenproject.entity.impl.Rezervacija;
import com.mycompany.njt_mavenproject.repository.MyAppRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class RezervacijaRepository implements MyAppRepository<Rezervacija, Long> {

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<Rezervacija> findAll() {
        return em.createQuery(
                "SELECT r FROM Rezervacija r ORDER BY r.datum DESC",
                Rezervacija.class
        ).getResultList();
    }

    public List<Rezervacija> findByVlasnikId(Long vlasnikId) {
        return em.createQuery(
                "SELECT r FROM Rezervacija r WHERE r.vlasnik.id = :vid ORDER BY r.datum DESC",
                Rezervacija.class
        )
        .setParameter("vid", vlasnikId)
        .getResultList();
    }

    @Override
    public Rezervacija findById(Long id) throws Exception {
        Rezervacija r = em.find(Rezervacija.class, id);
        if (r == null) throw new Exception("Rezervacija nije pronađena: " + id);
        return r;
    }

    @Override @Transactional
    public void save(Rezervacija entity) {
        if (entity.getId() == null) em.persist(entity);
        else em.merge(entity);
    }

    @Override @Transactional
    public void deleteById(Long id) {
        Rezervacija r = em.find(Rezervacija.class, id);
        if (r != null) em.remove(r);
    }

    /** Tvrda (SQL) provera preklapanja termina: (start,end) ∩ (datum, datum+trajanje) ≠ ∅ */
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

