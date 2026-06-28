/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.njt_mavenproject.repository.impl;

/**
 * Repozitorijum za upravljanje vezama između servisa i usluga u bazi podataka.
 * Čuva cenovnik — tj. koje usluge nudi koji servis i po kojoj ceni.
 * Implementira osnovne CRUD operacije i pruža dodatne metode za
 * pretragu, ažuriranje i brisanje stavki cenovnika.
 *
 * @author Bojana
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

    @PersistenceContext
    private EntityManager em;

    /**
     * Vraća listu svih veza između servisa i usluga iz baze podataka.
     *
     * @return lista svih stavki cenovnika
     */
    @Override
    public List<ServisUsluga> findAll() {
        return em.createQuery("select su from ServisUsluga su", ServisUsluga.class).getResultList();
    }

    /**
     * Pronalazi vezu između servisa i usluge na osnovu prosleđenog identifikatora.
     *
     * @param id identifikator stavke cenovnika koja se traži
     * @return pronađena stavka cenovnika
     * @throws Exception ako stavka sa datim ID-jem ne postoji
     */
    @Override
    public ServisUsluga findById(Long id) throws Exception {
        ServisUsluga su = em.find(ServisUsluga.class, id);
        if (su == null) throw new Exception("ServisUsluga nije pronađena: " + id);
        return su;
    }

    /**
     * Čuva novu stavku cenovnika ili ažurira postojeću u bazi podataka.
     * Ako entitet nema postavljen ID, kreira se novi zapis,
     * u suprotnom se ažurira postojeći.
     *
     * @param entity stavka cenovnika koja se čuva
     */
    @Override
    @Transactional
    public void save(ServisUsluga entity) {
        if (entity.getId() == null) em.persist(entity);
        else em.merge(entity);
    }

    /**
     * Briše stavku cenovnika sa datim identifikatorom iz baze podataka.
     * Ako stavka ne postoji, metoda se završava bez greške.
     *
     * @param id identifikator stavke cenovnika koja se briše
     */
    @Override
    @Transactional
    public void deleteById(Long id) {
        ServisUsluga su = em.find(ServisUsluga.class, id);
        if (su != null) em.remove(su);
    }

    /**
     * Vraća listu svih usluga koje nudi dati servis, sortirano po nazivu usluge.
     *
     * @param servisId identifikator servisa čiji se cenovnik traži
     * @return lista stavki cenovnika za dati servis
     */
    public List<ServisUsluga> findByServisId(Long servisId) {
        return em.createQuery(
            "select su from ServisUsluga su join fetch su.usluga " +
            "where su.servis.id=:sid order by su.usluga.naziv", ServisUsluga.class)
            .setParameter("sid", servisId)
            .getResultList();
    }

    /**
     * Vraća cenu određene usluge u datom servisu.
     *
     * @param servisId identifikator servisa
     * @param uslugaId identifikator usluge
     * @return cena usluge u datom servisu, ili {@code null} ako veza ne postoji
     */
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

    /**
     * Ažurira cenu usluge u datom servisu ako veza već postoji,
     * a ako ne postoji, kreira novu stavku cenovnika.
     *
     * @param servisId identifikator servisa
     * @param uslugaId identifikator usluge
     * @param cena     nova cena usluge
     */
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

    /**
     * Zamenjuje sve stavke cenovnika za dati servis novim stavkama.
     * Prvo se brišu sve postojeće veze, a zatim se upisuju nove.
     *
     * @param servisId identifikator servisa čiji se cenovnik zamenjuje
     * @param novi     lista novih stavki cenovnika
     */
    @Transactional
    public void replaceAllForServis(Long servisId, List<ServisUsluga> novi) {
        em.createQuery("delete from ServisUsluga su where su.servis.id=:sid")
          .setParameter("sid", servisId).executeUpdate();
        for (ServisUsluga su : novi) em.persist(su);
    }

    /**
     * Briše sve stavke cenovnika vezane za dati servis.
     *
     * @param servisId identifikator servisa čiji se cenovnik briše
     */
    @Transactional
    public void deleteByServis(Long servisId) {
        em.createQuery("delete from ServisUsluga su where su.servis.id=:sid")
          .setParameter("sid", servisId).executeUpdate();
    }

    /**
     * Briše sve stavke cenovnika vezane za datu uslugu.
     * Koristi se pre brisanja usluge kako bi se očuvao integritet podataka.
     *
     * @param uslugaId identifikator usluge čije se stavke cenovnika brišu
     */
    @Transactional
    public void deleteByUsluga(Long uslugaId) {
        em.createQuery("delete from ServisUsluga su where su.usluga.id=:uid")
          .setParameter("uid", uslugaId).executeUpdate();
    }
}