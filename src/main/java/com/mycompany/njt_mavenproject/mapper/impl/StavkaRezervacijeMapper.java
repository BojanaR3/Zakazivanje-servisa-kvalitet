/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.njt_mavenproject.mapper.impl;

/**
 *
 * @author Korisnik
 */
import com.mycompany.njt_mavenproject.dto.impl.StavkaRezervacijeDto;
import com.mycompany.njt_mavenproject.entity.impl.StavkaRezervacije;
import com.mycompany.njt_mavenproject.entity.impl.Usluga;
import com.mycompany.njt_mavenproject.mapper.DtoEntityMapper;
import org.springframework.stereotype.Component;

@Component
public class StavkaRezervacijeMapper implements DtoEntityMapper<StavkaRezervacijeDto, StavkaRezervacije> {

    @Override
    public StavkaRezervacijeDto toDto(StavkaRezervacije e) {
        return new StavkaRezervacijeDto(
                e.getId(),
                e.getUsluga() != null ? e.getUsluga().getId() : null,
                e.getKolicina(),
                e.getUnitPrice()
        );
        
    }

    @Override
    public StavkaRezervacije toEntity(StavkaRezervacijeDto t) {
        StavkaRezervacije s = new StavkaRezervacije();
        s.setId(t.getId());
        s.setUsluga(t.getUslugaId() != null ? new Usluga(t.getUslugaId()) : null); // "ref"
        s.setKolicina(t.getKolicina());
       
        // unitPrice postavljamo u servisu (računa se), ali ako dođe iz DTO-a, možemo ga ignorisati
        return s;
    }
}
