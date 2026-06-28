/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.njt_mavenproject.mapper.impl;

import com.mycompany.njt_mavenproject.dto.impl.VlasnikDto;
import com.mycompany.njt_mavenproject.entity.impl.Uloga;
import com.mycompany.njt_mavenproject.entity.impl.Vlasnik;
import com.mycompany.njt_mavenproject.mapper.DtoEntityMapper;
import org.springframework.stereotype.Component;

/**
 * Mapper klasa za konverziju između {@link Vlasnik} entiteta
 * i {@link VlasnikDto} objekata.
 * Lozinka se nikada ne prenosi iz entiteta u DTO radi bezbednosti,
 * a enkodovanje lozinke se obavlja u servisnom sloju.
 *
 * @author Bojana
 */
@Component
public class VlasnikMapper implements DtoEntityMapper<VlasnikDto, Vlasnik> {

    /**
     * Konvertuje entitet vlasnika u DTO objekat.
     * Lozinka se nikada ne uključuje u DTO iz bezbednosnih razloga.
     *
     * @param e entitet vlasnika koji se konvertuje, može biti {@code null}
     * @return DTO objekat sa podacima vlasnika, ili {@code null} ako je entitet {@code null}
     */
    @Override
    public VlasnikDto toDto(Vlasnik e) {
        if (e == null) return null;
        return new VlasnikDto(e.getId(), e.getIme(), e.getPrezime(), e.getUsername(), e.getEmail(), e.getUloga());
    }

    /**
     * Konvertuje DTO objekat u entitet vlasnika.
     * Enkodovana lozinka se postavlja naknadno u servisnom sloju,
     * nakon prolaska kroz password encoder.
     *
     * @param t DTO objekat vlasnika koji se konvertuje, može biti {@code null}
     * @return entitet vlasnika spreman za dalju obradu, ili {@code null} ako je DTO {@code null}
     */
    @Override
    public Vlasnik toEntity(VlasnikDto t) {
        if (t == null) return null;
        Vlasnik u = new Vlasnik();
        u.setId(t.getId());
        u.setIme(t.getIme());
        u.setPrezime(t.getPrezime());
        u.setUsername(t.getUsername());
        u.setEmail(t.getEmail());
        // lozinka se postavlja u servisu nakon enkodovanja, ne ovde
        u.setUloga(t.getUloga());
        return u;
    }
}