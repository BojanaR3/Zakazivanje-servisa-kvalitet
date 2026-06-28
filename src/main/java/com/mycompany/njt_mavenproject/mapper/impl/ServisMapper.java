package com.mycompany.njt_mavenproject.mapper.impl;

import com.mycompany.njt_mavenproject.dto.impl.ServisDto;
import com.mycompany.njt_mavenproject.entity.impl.Servis;
import com.mycompany.njt_mavenproject.mapper.DtoEntityMapper;
import org.springframework.stereotype.Component;

/**
 * Mapper klasa za konverziju između {@link Servis} entiteta
 * i {@link ServisDto} objekata.
 * Cenovnik usluga po servisu se ne mapira ovde, već se dohvata
 * posebno putem /api/servis/{id}/cenovnik endpointa.
 *
 * @author Bojana
 */
@Component
public class ServisMapper implements DtoEntityMapper<ServisDto, Servis> {

    /**
     * Konvertuje entitet servisa u DTO objekat.
     * Cenovnik usluga se ne uključuje u konverziju.
     *
     * @param e entitet servisa koji se konvertuje
     * @return DTO objekat sa osnovnim podacima servisa
     */
    @Override
    public ServisDto toDto(Servis e) {
        return new ServisDto(
                e.getId(),
                e.getNaziv(),
                e.getAdresa(),
                e.getTelefon()
        );
    }

    /**
     * Konvertuje DTO objekat u entitet servisa.
     * Veze sa uslugama se ignorišu i vode se u tabeli servis_usluga.
     *
     * @param t DTO objekat servisa koji se konvertuje
     * @return entitet servisa spreman za perzistenciju
     */
    @Override
    public Servis toEntity(ServisDto t) {
        return new Servis(
                t.getId(),
                t.getNaziv(),
                t.getAdresa(),
                t.getTelefon()
        );
    }
}