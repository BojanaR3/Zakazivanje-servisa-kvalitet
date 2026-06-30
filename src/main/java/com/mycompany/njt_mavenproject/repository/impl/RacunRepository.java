package com.mycompany.njt_mavenproject.repository.impl;

import com.mycompany.njt_mavenproject.entity.impl.Racun;
import com.mycompany.njt_mavenproject.exception.EntityNotFoundException;
import com.mycompany.njt_mavenproject.repository.MyAppRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * Repozitorijum za upravljanje entitetima računa u bazi podataka.
 * Implementira osnovne CRUD operacije i pruža dodatnu metodu
 * za pretragu računa po rezervaciji.
 *
 * @author Bojana
 */
@Repository
public class RacunRepository implements MyAppRepository<Racun, Long> {

    @PersistenceContext
    private EntityManager em;

    /**
     * Vraća listu svih računa iz baze podataka.
     *
     * @return lista svih računa
     */
    @Override
    public List<Racun> findAll() {
        return em.createQuery("SELECT r FROM Racun r", Racun.class).getResultList();
    }

    /**
     * Pronalazi račun na osnovu prosleđenog identifikatora.
     *
     * @param id identifikator računa koji se traži
     * @return pronađeni račun
     * @throws EntityNotFoundException ako račun sa datim ID-jem ne postoji
     */
    @Override
    public Racun findById(Long id) throws EntityNotFoundException {
        Racun r = em.find(Racun.class, id);
        if (r == null) throw new EntityNotFoundException("Racun #" + id + " ne postoji.");
        return r;
    }

    /**
     * Čuva novi račun ili ažurira postojeći u bazi podataka.
     * Ako entitet nema postavljen ID, kreira se novi zapis,
     * u suprotnom se ažurira postojeći.
     *
     * @param entity račun koji se čuva
     */
    @Override
    public void save(Racun entity) {
        if (entity.getId() == null) em.persist(entity);
        else em.merge(entity);
    }

    /**
     * Briše račun sa datim identifikatorom iz baze podataka.
     * Ako račun ne postoji, metoda se završava bez greške.
     *
     * @param id identifikator računa koji se briše
     */
    @Override
    public void deleteById(Long id) {
        Racun r = em.find(Racun.class, id);
        if (r != null) em.remove(r);
    }

    /**
     * Pronalazi račun koji je vezan za datu rezervaciju.
     *
     * @param rezervacijaId identifikator rezervacije
     * @return račun vezan za traženu rezervaciju, ili {@code null} ako ne postoji
     */
    public Racun findByRezervacijaId(Long rezervacijaId) {
        try {
            return em.createQuery("SELECT r FROM Racun r WHERE r.rezervacija.id = :rid", Racun.class)
                    .setParameter("rid", rezervacijaId)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
}