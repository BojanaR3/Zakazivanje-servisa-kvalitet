/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.njt_mavenproject.mapper.impl;

/**
 * Mapper klasa za konverziju između {@link StavkaRezervacije} entiteta
 * i {@link StavkaRezervacijeDto} objekata.
 * Cena po jedinici (unitPrice) se ne preuzima iz DTO-a već se
 * računa i postavlja u servisnom sloju.
 *
 * @author Bojana
 */
import com.mycompany.njt_mavenproject.dto.impl.StavkaRezervacijeDto;
import com.mycompany.njt_mavenproject.entity.impl.StavkaRezervacije;
import com.mycompany.njt_mavenproject.entity.impl.Usluga;
import com.mycompany.njt_mavenproject.mapper.DtoEntityMapper;
import org.springframework.stereotype.Component;

@Component
public class StavkaRezervacijeMapper implements DtoEntityMapper<StavkaRezervacijeDto, StavkaRezervacije> {

    /**
     * Konvertuje entitet stavke rezervacije u DTO objekat.
     *
     * @param e entitet stavke rezervacije koji se konvertuje
     * @return DTO objekat sa podacima stavke rezervacije
     */
    @Override
    public StavkaRezervacijeDto toDto(StavkaRezervacije e) {
        return new StavkaRezervacijeDto(
                e.getId(),
                e.getUsluga() != null ? e.getUsluga().getId() : null,
                e.getKolicina(),
                e.getUnitPrice()
        );
    }

    /**
     * Konvertuje DTO objekat u entitet stavke rezervacije.
     * Vrednost unitPrice se ignoriše iz DTO-a i postavlja se
     * naknadno u servisnom sloju na osnovu cenovnika.
     *
     * @param t DTO objekat stavke rezervacije koji se konvertuje
     * @return entitet stavke rezervacije spreman za dalju obradu
     */
    @Override
    public StavkaRezervacije toEntity(StavkaRezervacijeDto t) {
        StavkaRezervacije s = new StavkaRezervacije();
        s.setId(t.getId());
        s.setUsluga(t.getUslugaId() != null ? new Usluga(t.getUslugaId()) : null);
        s.setKolicina(t.getKolicina());
        // unitPrice postavljamo u servisu (računa se), ali ako dođe iz DTO-a, možemo ga ignorisati
        return s;
    }
}