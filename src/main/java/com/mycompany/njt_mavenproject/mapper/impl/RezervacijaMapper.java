/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.njt_mavenproject.mapper.impl;

/**
 *
 * @author Korisnik
 */


import com.mycompany.njt_mavenproject.dto.impl.RezervacijaDto;
import com.mycompany.njt_mavenproject.dto.impl.StavkaRezervacijeDto;
import com.mycompany.njt_mavenproject.entity.impl.Rezervacija;
import com.mycompany.njt_mavenproject.entity.impl.Servis;
import com.mycompany.njt_mavenproject.entity.impl.Vlasnik;
import com.mycompany.njt_mavenproject.entity.impl.Vozilo;
import com.mycompany.njt_mavenproject.mapper.DtoEntityMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class RezervacijaMapper implements DtoEntityMapper<RezervacijaDto, Rezervacija> {

    private final StavkaRezervacijeMapper itemMapper;

    public RezervacijaMapper(StavkaRezervacijeMapper itemMapper) {
        this.itemMapper = itemMapper;
    }

    @Override
    public RezervacijaDto toDto(Rezervacija e) {
        RezervacijaDto d = new RezervacijaDto();
        d.setId(e.getId());
        d.setStatus(e.getStatus());
        d.setDatum(e.getDatum());
        d.setUkupanIznos(e.getUkupanIznos());
        d.setServisId(e.getServis() != null ? e.getServis().getId() : null);
        d.setVoziloId(e.getVozilo() != null ? e.getVozilo().getId() : null);
        d.setVlasnikId(e.getVlasnik() != null ? e.getVlasnik().getId() : null);

        List<StavkaRezervacijeDto> items = e.getStavke()
                .stream()
                .map(itemMapper::toDto)
                .collect(Collectors.toList());
        d.setStavke(items);

        return d;
    }

    @Override
    public Rezervacija toEntity(RezervacijaDto t) {
        Rezervacija r = new Rezervacija();
        r.setId(t.getId());
        if (t.getStatus() != null) r.setStatus(t.getStatus());
        
        if (t.getServisId() != null) r.setServis(new Servis(t.getServisId()));
        if (t.getVoziloId() != null) r.setVozilo(new Vozilo(t.getVoziloId()));
        if (t.getVlasnikId() != null) r.setVlasnik(new Vlasnik(t.getVlasnikId())); // stvarni vlasnik se uzima iz tokena u servisu

        if (t.getStavke() != null) {
            t.getStavke().forEach(d -> r.addItem(itemMapper.toEntity(d)));
        }
        return r;
    }
}

