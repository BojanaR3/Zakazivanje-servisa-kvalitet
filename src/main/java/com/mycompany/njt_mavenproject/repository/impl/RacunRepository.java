package com.mycompany.njt_mavenproject.repository.impl;

import com.mycompany.njt_mavenproject.entity.impl.Racun;
import com.mycompany.njt_mavenproject.repository.MyAppRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public class RacunRepository implements MyAppRepository<Racun, Long> {

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<Racun> findAll() {
        return em.createQuery("SELECT r FROM Racun r", Racun.class).getResultList();
    }

    @Override
    public Racun findById(Long id) throws Exception {
        Racun r = em.find(Racun.class, id);
        if (r == null) throw new Exception("Racun #" + id + " ne postoji.");
        return r;
    }

    @Override
    public void save(Racun entity) {
        if (entity.getId() == null) em.persist(entity);
        else em.merge(entity);
    }

    @Override
    public void deleteById(Long id) {
        Racun r = em.find(Racun.class, id);
        if (r != null) em.remove(r);
    }

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