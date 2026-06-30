/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.njt_mavenproject.mapper.impl;

/**
 * Mapper klasa za konverziju između {@link com.mycompany.njt_mavenproject.entity.impl.Rezervacija} entiteta
 * i {@link com.mycompany.njt_mavenproject.dto.impl.RezervacijaDto} objekata.
 * Koristi {@link StavkaRezervacijeMapper} za konverziju stavki rezervacije.
 *
 * @author Bojana
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

@Component
public class RezervacijaMapper implements DtoEntityMapper<RezervacijaDto, Rezervacija> {

    private final StavkaRezervacijeMapper itemMapper;

    /**
     * Konstruktor koji injektuje mapper za stavke rezervacije.
     *
     * @param itemMapper mapper za konverziju stavki rezervacije
     */
    public RezervacijaMapper(StavkaRezervacijeMapper itemMapper) {
        this.itemMapper = itemMapper;
    }

    /**
     * Konvertuje entitet rezervacije u DTO objekat.
     * Mapira sve osnovne podatke kao i listu stavki rezervacije.
     *
     * @param e entitet rezervacije koji se konvertuje
     * @return DTO objekat sa podacima rezervacije
     */
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
                .toList();
        d.setStavke(items);
        return d;
    }

    /**
     * Konvertuje DTO objekat u entitet rezervacije.
     * Stvarni vlasnik se postavlja na osnovu JWT tokena u servisnom sloju,
     * a ne iz DTO-a.
     *
     * @param t DTO objekat rezervacije koji se konvertuje
     * @return entitet rezervacije spreman za perzistenciju
     */
    @Override
    public Rezervacija toEntity(RezervacijaDto t) {
        Rezervacija r = new Rezervacija();
        r.setId(t.getId());
        if (t.getStatus() != null) r.setStatus(t.getStatus());

        if (t.getServisId() != null) r.setServis(new Servis(t.getServisId()));
        if (t.getVoziloId() != null) r.setVozilo(new Vozilo(t.getVoziloId()));
        if (t.getVlasnikId() != null) r.setVlasnik(new Vlasnik(t.getVlasnikId()));
        if (t.getStavke() != null) {
            t.getStavke().forEach(d -> r.addItem(itemMapper.toEntity(d)));
        }
        return r;
    }
}