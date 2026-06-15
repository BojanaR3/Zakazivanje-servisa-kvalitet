package com.mycompany.njt_mavenproject.repository.impl;

import com.mycompany.njt_mavenproject.entity.impl.Mehanicar;
import com.mycompany.njt_mavenproject.repository.MyAppRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public class MehanicarRepository implements MyAppRepository<Mehanicar, Long> {

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<Mehanicar> findAll() {
        return em.createQuery("SELECT m FROM Mehanicar m", Mehanicar.class).getResultList();
    }

    @Override
    public Mehanicar findById(Long id) throws Exception {
        Mehanicar m = em.find(Mehanicar.class, id);
        if (m == null) throw new Exception("Mehanicar #" + id + " ne postoji.");
        return m;
    }

    @Override
    public void save(Mehanicar entity) {
        if (entity.getId() == null) em.persist(entity);
        else em.merge(entity);
    }

    @Override
    public void deleteById(Long id) {
        Mehanicar m = em.find(Mehanicar.class, id);
        if (m != null) em.remove(m);
    }

    public List<Mehanicar> findByServisId(Long servisId) {
        return em.createQuery("SELECT m FROM Mehanicar m WHERE m.servis.id = :sid", Mehanicar.class)
                .setParameter("sid", servisId)
                .getResultList();
    }
}