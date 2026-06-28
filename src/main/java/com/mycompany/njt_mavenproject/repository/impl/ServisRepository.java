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
 * Repozitorijum za upravljanje entitetima servisa u bazi podataka.
 * Implementira osnovne CRUD operacije i pruža dodatnu metodu
 * za pretragu više servisa po listi identifikatora.
 *
 * @author Bojana
 */
@Repository
public class ServisRepository implements MyAppRepository<Servis, Long> {

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Vraća listu svih servisa iz baze podataka.
     *
     * @return lista svih servisa
     */
    @Override
    public List<Servis> findAll() {
        return entityManager.createQuery("SELECT s FROM Servis s", Servis.class).getResultList();
    }

    /**
     * Pronalazi servis na osnovu prosleđenog identifikatora.
     *
     * @param id identifikator servisa koji se traži
     * @return pronađeni servis
     * @throws Exception ako servis sa datim ID-jem ne postoji
     */
    @Override
    public Servis findById(Long id) throws Exception {
        Servis servis = entityManager.find(Servis.class, id);
        if (servis == null) {
            throw new Exception("Servis nije pronađen!");
        }
        return servis;
    }

    /**
     * Vraća listu servisa čiji se identifikatori nalaze u prosleđenoj listi.
     * Ako je lista prazna ili null, vraća se prazna lista.
     *
     * @param ids lista identifikatora servisa koji se traže
     * @return lista pronađenih servisa
     */
    public List<Servis> findAllByIds(List<Long> ids) {
        if (ids == null || ids.isEmpty()) return List.of();
        return entityManager.createQuery(
                "SELECT s FROM Servis s WHERE s.id IN :ids", Servis.class)
            .setParameter("ids", ids)
            .getResultList();
    }

    /**
     * Čuva novi servis ili ažurira postojeći u bazi podataka.
     * Ako entitet nema postavljen ID, kreira se novi zapis,
     * u suprotnom se ažurira postojeći.
     *
     * @param entity servis koji se čuva
     */
    @Override
    @Transactional
    public void save(Servis entity) {
        if (entity.getId() == null) {
            entityManager.persist(entity);
        } else {
            entityManager.merge(entity);
        }
    }

    /**
     * Briše servis sa datim identifikatorom iz baze podataka.
     * Ako servis ne postoji, metoda se završava bez greške.
     *
     * @param id identifikator servisa koji se briše
     */
    @Override
    @Transactional
    public void deleteById(Long id) {
        Servis servis = entityManager.find(Servis.class, id);
        if (servis != null) {
            entityManager.remove(servis);
        }
    }
}