package com.mycompany.njt_mavenproject.mapper.impl;

import com.mycompany.njt_mavenproject.dto.impl.RacunDto;
import com.mycompany.njt_mavenproject.entity.impl.Racun;
import com.mycompany.njt_mavenproject.entity.impl.Rezervacija;
import com.mycompany.njt_mavenproject.mapper.DtoEntityMapper;
import org.springframework.stereotype.Component;

@Component
public class RacunMapper implements DtoEntityMapper<RacunDto, Racun> {

    @Override
    public RacunDto toDto(Racun e) {
        return new RacunDto(
                e.getId(),
                e.getBroj(),
                e.getDatumIzdavanja(),
                e.getUkupanIznos(),
                e.getStatusPlacanja(),
                e.getRezervacija() != null ? e.getRezervacija().getId() : null
        );
    }

    @Override
    public Racun toEntity(RacunDto t) {
        Racun r = new Racun();
        r.setId(t.getId());
        r.setBroj(t.getBroj());
        r.setDatumIzdavanja(t.getDatumIzdavanja());
        r.setUkupanIznos(t.getUkupanIznos());
        r.setStatusPlacanja(t.getStatusPlacanja());
        if (t.getRezervacijaId() != null)
            r.setRezervacija(new Rezervacija(t.getRezervacijaId()));
        return r;
    }
}