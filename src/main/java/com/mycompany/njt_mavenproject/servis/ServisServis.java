package com.mycompany.njt_mavenproject.servis;

import com.mycompany.njt_mavenproject.dto.impl.ServisDto;
import com.mycompany.njt_mavenproject.entity.impl.Servis;
import com.mycompany.njt_mavenproject.mapper.impl.ServisMapper;
import com.mycompany.njt_mavenproject.repository.impl.ServisRepository;
import com.mycompany.njt_mavenproject.repository.impl.ServisUslugaRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ServisServis {

    private final ServisRepository repo;
    private final ServisMapper mapper;
    private final ServisUslugaRepository cenovnik;

    public ServisServis(ServisRepository repo, ServisMapper mapper, ServisUslugaRepository cenovnik) {
        this.repo = repo; this.mapper = mapper; this.cenovnik = cenovnik;
    }

    public List<ServisDto> findAll() {
        return repo.findAll().stream().map(mapper::toDto).collect(Collectors.toList());
    }

    public ServisDto findById(Long id) throws Exception {
        return mapper.toDto(repo.findById(id));
    }

    @Transactional
    public ServisDto create(ServisDto dto) {
        Servis s = mapper.toEntity(dto);
        repo.save(s);
        return mapper.toDto(s);
    }

    @Transactional
    public ServisDto update(ServisDto dto) {
        Servis s;
        try { s = repo.findById(dto.getId()); }
        catch (Exception e) { throw new RuntimeException("Servis ne postoji: " + dto.getId()); }

        s.setNaziv(dto.getNaziv());
        s.setAdresa(dto.getAdresa());
        s.setTelefon(dto.getTelefon());
        repo.save(s);
        return mapper.toDto(s);
    }

    @Transactional
    public void deleteById(Long id) {
        // očisti cenovnik za taj servis pre brisanja
        cenovnik.deleteByServis(id);
        repo.deleteById(id);
    }
}
