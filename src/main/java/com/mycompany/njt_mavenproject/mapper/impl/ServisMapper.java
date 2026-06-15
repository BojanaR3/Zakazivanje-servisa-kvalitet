package com.mycompany.njt_mavenproject.mapper.impl;

import com.mycompany.njt_mavenproject.dto.impl.ServisDto;
import com.mycompany.njt_mavenproject.entity.impl.Servis;
import com.mycompany.njt_mavenproject.mapper.DtoEntityMapper;
import org.springframework.stereotype.Component;

@Component
public class ServisMapper implements DtoEntityMapper<ServisDto, Servis> {

    @Override
    public ServisDto toDto(Servis e) {
        // Nema više e.getUsluge(); cenovnik po servisu ide preko /api/servis/{id}/cenovnik
        return new ServisDto(
                e.getId(),
                e.getNaziv(),
                e.getAdresa(),
                e.getTelefon()
        );
    }

    @Override
    public Servis toEntity(ServisDto t) {
        // DTO.usluge ignorisemo – veze se vode u servis_usluga
        return new Servis(
                t.getId(),
                t.getNaziv(),
                t.getAdresa(),
                t.getTelefon()
        );
    }
}
