package com.mycompany.njt_mavenproject.servis;

import com.mycompany.njt_mavenproject.dto.impl.UslugaDto;
import com.mycompany.njt_mavenproject.entity.impl.Usluga;
import com.mycompany.njt_mavenproject.mapper.impl.UslugaMapper;
import com.mycompany.njt_mavenproject.repository.impl.ServisUslugaRepository;
import com.mycompany.njt_mavenproject.repository.impl.UslugaRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UslugaServis {

    private final UslugaRepository usluge;
    private final ServisUslugaRepository cenovnik;
    private final UslugaMapper mapper;

    public UslugaServis(UslugaRepository usluge,
                        ServisUslugaRepository cenovnik,
                        UslugaMapper mapper) {
        this.usluge = usluge;
        this.cenovnik = cenovnik;
        this.mapper = mapper;
    }

    public List<UslugaDto> findAll() {
        return usluge.findAll().stream().map(mapper::toDto).collect(Collectors.toList());
    }

    public UslugaDto findById(Long id) throws Exception {
        return mapper.toDto(usluge.findById(id));
    }

    @Transactional
    public UslugaDto create(UslugaDto dto) {
        Usluga u = mapper.toEntity(dto);
        usluge.save(u);
        return mapper.toDto(u);
    }

    @Transactional
    public UslugaDto update(UslugaDto dto) {
        Usluga existing;
        try { existing = usluge.findById(dto.getId()); }
        catch (Exception e) { throw new RuntimeException("Usluga ne postoji: " + dto.getId()); }

        existing.setNaziv(dto.getNaziv());
        existing.setTrajanje(dto.getTrajanje());
        existing.setJedinicaMere(dto.getJedinicaMere());
       
        usluge.save(existing);
        return mapper.toDto(existing);
    }

    @Transactional
    public void deleteById(Long id) {
        // pre brisanja – ukloni cenovnik unosa za ovu uslugu
        cenovnik.deleteByUsluga(id);
        usluge.deleteById(id);
    }
}
