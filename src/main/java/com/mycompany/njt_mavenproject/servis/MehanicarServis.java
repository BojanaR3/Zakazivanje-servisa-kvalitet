package com.mycompany.njt_mavenproject.servis;

import com.mycompany.njt_mavenproject.dto.impl.MehanicarDto;
import com.mycompany.njt_mavenproject.entity.impl.Mehanicar;
import com.mycompany.njt_mavenproject.entity.impl.Servis;
import com.mycompany.njt_mavenproject.exception.EntityNotFoundException;
import com.mycompany.njt_mavenproject.mapper.impl.MehanicarMapper;
import com.mycompany.njt_mavenproject.repository.impl.MehanicarRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * Servis koji upravlja poslovnom logikom vezanom za mehaničare.
 * Pruža operacije kreiranja, pretraživanja, ažuriranja i brisanja mehaničara,
 * kao i pretragu mehaničara po servisu u kojem rade.
 *
 * @author Bojana
 */
@Service
public class MehanicarServis {

    /** Repozitorijum za pristup podacima o mehaničarima u bazi podataka. */
    private final MehanicarRepository repo;

    /** Mapper za konverziju između entiteta mehaničara i DTO objekata. */
    private final MehanicarMapper mapper;

    /** Entity manager za direktan pristup JPA kontekstu, koristi se za dohvatanje referenci. */
    @PersistenceContext
    private EntityManager em;

    /**
     * Konstruktor koji injektuje repozitorijum i mapper za mehaničare.
     *
     * @param repo   repozitorijum za pristup podacima o mehaničarima
     * @param mapper mapper za konverziju između entiteta i DTO objekata
     */
    public MehanicarServis(MehanicarRepository repo, MehanicarMapper mapper) {
        this.repo = repo;
        this.mapper = mapper;
    }

    /**
     * Vraća listu svih mehaničara u sistemu.
     *
     * @return lista DTO objekata svih mehaničara
     */
    public List<MehanicarDto> findAll() {
        return repo.findAll().stream().map(mapper::toDto).toList();
    }

    /**
     * Vraća listu svih mehaničara koji rade u datom servisu.
     *
     * @param servisId identifikator servisa čiji se mehaničari traže
     * @return lista DTO objekata mehaničara iz traženog servisa
     */
    public List<MehanicarDto> findByServis(Long servisId) {
        return repo.findByServisId(servisId).stream().map(mapper::toDto).toList();
    }

    /**
     * Pronalazi mehaničara na osnovu prosleđenog identifikatora.
     *
     * @param id identifikator mehaničara koji se traži
     * @return DTO objekat pronađenog mehaničara
     * @throws EntityNotFoundException ako mehaničar sa datim ID-jem ne postoji
     */
    public MehanicarDto findById(Long id) throws EntityNotFoundException {
        return mapper.toDto(repo.findById(id));
    }

    /**
     * Kreira novog mehaničara u sistemu i dodeljuje ga servisu.
     *
     * @param dto DTO objekat sa podacima novog mehaničara
     * @return DTO objekat novokreiranog mehaničara sa dodeljenim ID-jem
     */
    @Transactional
    public MehanicarDto create(MehanicarDto dto) {
        Mehanicar m = mapper.toEntity(dto);
        if (dto.getServisId() != null)
            m.setServis(em.getReference(Servis.class, dto.getServisId()));
        repo.save(m);
        return mapper.toDto(m);
    }

    /**
     * Ažurira podatke postojećeg mehaničara.
     * Menjaju se ime, prezime, specijalnost, telefon i servis u kome radi.
     *
     * @param dto DTO objekat sa ažuriranim podacima mehaničara
     * @return DTO objekat ažuriranog mehaničara
     * @throws EntityNotFoundException ako mehaničar sa datim ID-jem ne postoji
     */
    @Transactional
    public MehanicarDto update(MehanicarDto dto) throws EntityNotFoundException {
        Mehanicar m = repo.findById(dto.getId());
        m.setIme(dto.getIme());
        m.setPrezime(dto.getPrezime());
        m.setSpecijalnost(dto.getSpecijalnost());
        m.setTelefon(dto.getTelefon());
        if (dto.getServisId() != null)
            m.setServis(em.getReference(Servis.class, dto.getServisId()));
        repo.save(m);
        return mapper.toDto(m);
    }

    /**
     * Briše mehaničara sa datim identifikatorom iz sistema.
     *
     * @param id identifikator mehaničara koji se briše
     */
    @Transactional
    public void deleteById(Long id) {
        repo.deleteById(id);
    }
}