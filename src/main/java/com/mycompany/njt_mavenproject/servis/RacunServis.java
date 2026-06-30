package com.mycompany.njt_mavenproject.servis;

import com.mycompany.njt_mavenproject.dto.impl.RacunDto;
import com.mycompany.njt_mavenproject.entity.impl.Racun;
import com.mycompany.njt_mavenproject.entity.impl.Rezervacija;
import com.mycompany.njt_mavenproject.exception.EntityNotFoundException;
import com.mycompany.njt_mavenproject.mapper.impl.RacunMapper;
import com.mycompany.njt_mavenproject.repository.impl.RacunRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Servis koji upravlja poslovnom logikom vezanom za račune.
 * Pruža operacije kreiranja, pretraživanja, ažuriranja statusa plaćanja
 * i brisanja računa koji su vezani za rezervacije.
 *
 * @author Bojana
 */
@Service
public class RacunServis {

    /** Repozitorijum za pristup podacima o računima u bazi podataka. */
    private final RacunRepository repo;

    /** Mapper za konverziju između entiteta računa i DTO objekata. */
    private final RacunMapper mapper;

    /** Entity manager za direktan pristup JPA kontekstu, koristi se za dohvatanje referenci. */
    @PersistenceContext
    private EntityManager em;

    /**
     * Konstruktor koji injektuje repozitorijum i mapper za račune.
     *
     * @param repo   repozitorijum za pristup podacima o računima
     * @param mapper mapper za konverziju između entiteta i DTO objekata
     */
    public RacunServis(RacunRepository repo, RacunMapper mapper) {
        this.repo = repo;
        this.mapper = mapper;
    }

    /**
     * Vraća listu svih računa u sistemu.
     *
     * @return lista DTO objekata svih računa
     */
    public List<RacunDto> findAll() {
        return repo.findAll().stream().map(mapper::toDto).toList();
    }

    /**
     * Pronalazi račun na osnovu prosleđenog identifikatora.
     *
     * @param id identifikator računa koji se traži
     * @return DTO objekat pronađenog računa
     * @throws EntityNotFoundException ako račun sa datim ID-jem ne postoji
     */
    public RacunDto findById(Long id) throws EntityNotFoundException {
        return mapper.toDto(repo.findById(id));
    }

    /**
     * Pronalazi račun koji je vezan za datu rezervaciju.
     *
     * @param rezervacijaId identifikator rezervacije čiji se račun traži
     * @return DTO objekat pronađenog računa, ili {@code null} ako račun ne postoji
     */
    public RacunDto findByRezervacija(Long rezervacijaId) {
        Racun r = repo.findByRezervacijaId(rezervacijaId);
        return r != null ? mapper.toDto(r) : null;
    }

    /**
     * Kreira novi račun u sistemu i vezuje ga za odgovarajuću rezervaciju.
     * Ako datum izdavanja nije prosleđen, automatski se postavlja trenutno vreme.
     *
     * @param dto DTO objekat sa podacima novog računa
     * @return DTO objekat novokreiranog računa sa dodeljenim ID-jem
     */
    @Transactional
    public RacunDto create(RacunDto dto) {
        Racun r = mapper.toEntity(dto);
        if (dto.getRezervacijaId() != null)
            r.setRezervacija(em.getReference(Rezervacija.class, dto.getRezervacijaId()));
        if (r.getDatumIzdavanja() == null)
            r.setDatumIzdavanja(LocalDateTime.now());
        repo.save(r);
        return mapper.toDto(r);
    }

    /**
     * Ažurira status plaćanja postojećeg računa.
     *
     * @param id     identifikator računa čiji se status menja
     * @param status novi status plaćanja koji se postavlja
     * @return DTO objekat ažuriranog računa
     * @throws EntityNotFoundException ako račun sa datim ID-jem ne postoji
     */
    @Transactional
    public RacunDto updateStatus(Long id, String status) throws EntityNotFoundException {
        Racun r = repo.findById(id);
        r.setStatusPlacanja(status);
        repo.save(r);
        return mapper.toDto(r);
    }

    /**
     * Briše račun sa datim identifikatorom iz sistema.
     *
     * @param id identifikator računa koji se briše
     */
    @Transactional
    public void deleteById(Long id) {
        repo.deleteById(id);
    }
}