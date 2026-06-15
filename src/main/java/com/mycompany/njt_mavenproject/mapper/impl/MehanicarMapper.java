package com.mycompany.njt_mavenproject.mapper.impl;

import com.mycompany.njt_mavenproject.dto.impl.MehanicarDto;
import com.mycompany.njt_mavenproject.entity.impl.Mehanicar;
import com.mycompany.njt_mavenproject.entity.impl.Servis;
import com.mycompany.njt_mavenproject.mapper.DtoEntityMapper;
import org.springframework.stereotype.Component;

@Component
public class MehanicarMapper implements DtoEntityMapper<MehanicarDto, Mehanicar> {

    @Override
    public MehanicarDto toDto(Mehanicar e) {
        return new MehanicarDto(
                e.getId(),
                e.getIme(),
                e.getPrezime(),
                e.getSpecijalnost(),
                e.getTelefon(),
                e.getServis() != null ? e.getServis().getId() : null
        );
    }

    @Override
    public Mehanicar toEntity(MehanicarDto t) {
        Mehanicar m = new Mehanicar();
        m.setId(t.getId());
        m.setIme(t.getIme());
        m.setPrezime(t.getPrezime());
        m.setSpecijalnost(t.getSpecijalnost());
        m.setTelefon(t.getTelefon());
        if (t.getServisId() != null) m.setServis(new Servis(t.getServisId()));
        return m;
    }
}