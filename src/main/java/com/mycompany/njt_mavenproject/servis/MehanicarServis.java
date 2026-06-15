package com.mycompany.njt_mavenproject.servis;

import com.mycompany.njt_mavenproject.dto.impl.MehanicarDto;
import com.mycompany.njt_mavenproject.entity.impl.Mehanicar;
import com.mycompany.njt_mavenproject.entity.impl.Servis;
import com.mycompany.njt_mavenproject.mapper.impl.MehanicarMapper;
import com.mycompany.njt_mavenproject.repository.impl.MehanicarRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MehanicarServis {

    private final MehanicarRepository repo;
    private final MehanicarMapper mapper;

    @PersistenceContext
    private EntityManager em;

    public MehanicarServis(MehanicarRepository repo, MehanicarMapper mapper) {
        this.repo = repo;
        this.mapper = mapper;
    }

    public List<MehanicarDto> findAll() {
        return repo.findAll().stream().map(mapper::toDto).collect(Collectors.toList());
    }

    public List<MehanicarDto> findByServis(Long servisId) {
        return repo.findByServisId(servisId).stream().map(mapper::toDto).collect(Collectors.toList());
    }

    public MehanicarDto findById(Long id) throws Exception {
        return mapper.toDto(repo.findById(id));
    }

    @Transactional
    public MehanicarDto create(MehanicarDto dto) {
        Mehanicar m = mapper.toEntity(dto);
        if (dto.getServisId() != null)
            m.setServis(em.getReference(Servis.class, dto.getServisId()));
        repo.save(m);
        return mapper.toDto(m);
    }

    @Transactional
    public MehanicarDto update(MehanicarDto dto) throws Exception {
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

    @Transactional
    public void deleteById(Long id) {
        repo.deleteById(id);
    }
}