package com.mycompany.njt_mavenproject.repository.impl;

import com.mycompany.njt_mavenproject.entity.impl.Mehanicar;
import com.mycompany.njt_mavenproject.exception.EntityNotFoundException;
import com.mycompany.njt_mavenproject.repository.MyAppRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * Repozitorijum za upravljanje entitetima mehaničara u bazi podataka.
 * Implementira osnovne CRUD operacije i pruža dodatne metode za
 * pretragu mehaničara po servisu.
 *
 * @author Bojana
 */
@Repository
public class MehanicarRepository implements MyAppRepository<Mehanicar, Long> {

    @PersistenceContext
    private EntityManager em;

    /**
     * Vraća listu svih mehaničara iz baze podataka.
     *
     * @return lista svih mehaničara
     */
    @Override
    public List<Mehanicar> findAll() {
        return em.createQuery("SELECT m FROM Mehanicar m", Mehanicar.class).getResultList();
    }

    /**
     * Pronalazi mehaničara na osnovu prosleđenog identifikatora.
     *
     * @param id identifikator mehaničara koji se traži
     * @return pronađeni mehaničar
     * @throws EntityNotFoundException ako mehaničar sa datim ID-jem ne postoji
     */
    @Override
    public Mehanicar findById(Long id) throws EntityNotFoundException {
        Mehanicar m = em.find(Mehanicar.class, id);
        if (m == null) throw new EntityNotFoundException("Mehanicar #" + id + " ne postoji.");
        return m;
    }

    /**
     * Čuva novog mehaničara ili ažurira postojećeg u bazi podataka.
     * Ako entitet nema postavljen ID, kreira se novi zapis,
     * u suprotnom se ažurira postojeći.
     *
     * @param entity mehaničar koji se čuva
     */
    @Override
    public void save(Mehanicar entity) {
        if (entity.getId() == null) em.persist(entity);
        else em.merge(entity);
    }

    /**
     * Briše mehaničara sa datim identifikatorom iz baze podataka.
     * Ako mehaničar ne postoji, metoda se završava bez greške.
     *
     * @param id identifikator mehaničara koji se briše
     */
    @Override
    public void deleteById(Long id) {
        Mehanicar m = em.find(Mehanicar.class, id);
        if (m != null) em.remove(m);
    }

    /**
     * Vraća listu svih mehaničara koji rade u datom servisu.
     *
     * @param servisId identifikator servisa
     * @return lista mehaničara koji pripadaju traženom servisu
     */
    public List<Mehanicar> findByServisId(Long servisId) {
        return em.createQuery("SELECT m FROM Mehanicar m WHERE m.servis.id = :sid", Mehanicar.class)
                .setParameter("sid", servisId)
                .getResultList();
    }
}