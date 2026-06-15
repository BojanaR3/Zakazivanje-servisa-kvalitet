package com.mycompany.njt_mavenproject.servis;

import com.mycompany.njt_mavenproject.dto.impl.RacunDto;
import com.mycompany.njt_mavenproject.entity.impl.Racun;
import com.mycompany.njt_mavenproject.entity.impl.Rezervacija;
import com.mycompany.njt_mavenproject.mapper.impl.RacunMapper;
import com.mycompany.njt_mavenproject.repository.impl.RacunRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RacunServis {

    private final RacunRepository repo;
    private final RacunMapper mapper;

    @PersistenceContext
    private EntityManager em;

    public RacunServis(RacunRepository repo, RacunMapper mapper) {
        this.repo = repo;
        this.mapper = mapper;
    }

    public List<RacunDto> findAll() {
        return repo.findAll().stream().map(mapper::toDto).collect(Collectors.toList());
    }

    public RacunDto findById(Long id) throws Exception {
        return mapper.toDto(repo.findById(id));
    }

    public RacunDto findByRezervacija(Long rezervacijaId) {
        Racun r = repo.findByRezervacijaId(rezervacijaId);
        return r != null ? mapper.toDto(r) : null;
    }

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

    @Transactional
    public RacunDto updateStatus(Long id, String status) throws Exception {
        Racun r = repo.findById(id);
        r.setStatusPlacanja(status);
        repo.save(r);
        return mapper.toDto(r);
    }

    @Transactional
    public void deleteById(Long id) {
        repo.deleteById(id);
    }
}