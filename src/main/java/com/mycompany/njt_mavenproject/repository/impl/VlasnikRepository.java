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
 * Repozitorijum za upravljanje entitetima vlasnika u bazi podataka.
 * Implementira osnovne CRUD operacije i pruža dodatne metode za
 * pretragu po email adresi i korisničkom imenu, proveru jedinstvenosti
 * i ažuriranje statusa naloga i lozinke.
 *
 * @author Bojana
 */
@Repository
public class VlasnikRepository implements MyAppRepository<Vlasnik, Long> {

    @PersistenceContext
    private EntityManager em;

    /**
     * Vraća listu svih vlasnika iz baze podataka.
     *
     * @return lista svih vlasnika
     */
    @Override
    public List<Vlasnik> findAll() {
        return em.createQuery("SELECT v FROM Vlasnik v", Vlasnik.class).getResultList();
    }

    /**
     * Pronalazi vlasnika na osnovu prosleđenog identifikatora.
     *
     * @param id identifikator vlasnika koji se traži
     * @return pronađeni vlasnik
     * @throws Exception ako vlasnik sa datim ID-jem ne postoji
     */
    @Override
    public Vlasnik findById(Long id) throws Exception {
        Vlasnik v = em.find(Vlasnik.class, id);
        if (v == null) throw new Exception("Vlasnik nije pronađen!");
        return v;
    }

    /**
     * Čuva novog vlasnika ili ažurira postojećeg u bazi podataka.
     * Ako entitet nema postavljen ID, kreira se novi zapis,
     * u suprotnom se ažurira postojeći.
     *
     * @param entity vlasnik koji se čuva
     */
    @Transactional
    @Override
    public void save(Vlasnik entity) {
        if (entity.getId() == null) em.persist(entity);
        else em.merge(entity);
    }

    /**
     * Briše vlasnika sa datim identifikatorom iz baze podataka.
     * Ako vlasnik ne postoji, metoda se završava bez greške.
     *
     * @param id identifikator vlasnika koji se briše
     */
    @Transactional
    @Override
    public void deleteById(Long id) {
        Vlasnik v = em.find(Vlasnik.class, id);
        if (v != null) em.remove(v);
    }

    /**
     * Pronalazi vlasnika na osnovu email adrese.
     *
     * @param email email adresa vlasnika koji se traži
     * @return pronađeni vlasnik, ili {@code null} ako ne postoji
     */
    public Vlasnik findByEmail(String email) {
        List<Vlasnik> r = em.createQuery(
                "SELECT v FROM Vlasnik v WHERE v.email = :e", Vlasnik.class)
                .setParameter("e", email)
                .setMaxResults(1)
                .getResultList();
        return r.isEmpty() ? null : r.get(0);
    }

    /**
     * Pronalazi vlasnika na osnovu korisničkog imena.
     *
     * @param username korisničko ime vlasnika koji se traži
     * @return pronađeni vlasnik, ili {@code null} ako ne postoji
     */
    public Vlasnik findByUsername(String username) {
        List<Vlasnik> r = em.createQuery(
                "SELECT v FROM Vlasnik v WHERE v.username = :u", Vlasnik.class)
                .setParameter("u", username)
                .setMaxResults(1)
                .getResultList();
        return r.isEmpty() ? null : r.get(0);
    }

    /**
     * Proverava da li vlasnik sa datom email adresom već postoji u bazi podataka.
     *
     * @param email email adresa čija se jedinstvenost proverava
     * @return {@code true} ako email već postoji, {@code false} ako je slobodan
     */
    public boolean existsByEmail(String email) {
        Long cnt = em.createQuery(
                "SELECT COUNT(v) FROM Vlasnik v WHERE v.email = :e", Long.class)
                .setParameter("e", email)
                .getSingleResult();
        return cnt != null && cnt > 0;
    }

    /**
     * Proverava da li vlasnik sa datim korisničkim imenom već postoji u bazi podataka.
     *
     * @param username korisničko ime čija se jedinstvenost proverava
     * @return {@code true} ako korisničko ime već postoji, {@code false} ako je slobodno
     */
    public boolean existsByUsername(String username) {
        Long cnt = em.createQuery(
                "SELECT COUNT(v) FROM Vlasnik v WHERE v.username = :u", Long.class)
                .setParameter("u", username)
                .getSingleResult();
        return cnt != null && cnt > 0;
    }

    /**
     * Ažurira status aktivacije naloga vlasnika.
     *
     * @param id      identifikator vlasnika čiji se status menja
     * @param enabled {@code true} da se nalog aktivira, {@code false} da se deaktivira
     * @return broj ažuriranih zapisa
     */
    @Transactional
    public int updateEnabled(Long id, boolean enabled) {
        return em.createQuery("UPDATE Vlasnik v SET v.enabled = :en WHERE v.id = :id")
                 .setParameter("en", enabled)
                 .setParameter("id", id)
                 .executeUpdate();
    }

    /**
     * Ažurira enkodovanu lozinku vlasnika u bazi podataka.
     *
     * @param id           identifikator vlasnika čija se lozinka menja
     * @param passwordHash nova enkodovana lozinka
     * @return broj ažuriranih zapisa
     */
    @Transactional
    public int updatePassword(Long id, String passwordHash) {
        return em.createQuery("UPDATE Vlasnik v SET v.lozinka = :p WHERE v.id = :id")
                 .setParameter("p", passwordHash)
                 .setParameter("id", id)
                 .executeUpdate();
    }
}