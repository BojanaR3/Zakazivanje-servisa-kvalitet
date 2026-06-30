package com.mycompany.njt_mavenproject.mapper.impl;

import com.mycompany.njt_mavenproject.dto.impl.UslugaDto;
import com.mycompany.njt_mavenproject.entity.impl.Usluga;
import com.mycompany.njt_mavenproject.mapper.DtoEntityMapper;
import org.springframework.stereotype.Component;

/**
 * Mapper klasa za konverziju između {@link Usluga} entiteta
 * i {@link UslugaDto} objekata.
 * Veze između usluga i servisa se ne mapiraju ovde,
 * već se vode kroz cenovnik putem posebnog endpointa.
 *
 * @author Bojana
 */
@Component
public class UslugaMapper implements DtoEntityMapper<UslugaDto, Usluga> {

    /**
     * Konvertuje entitet usluge u DTO objekat.
     * Veze sa servisima se ne uključuju u konverziju,
     * već se dohvataju posebno kroz cenovnik.
     *
     * @param e entitet usluge koji se konvertuje
     * @return DTO objekat sa podacima usluge
     */
	@Override
	public UslugaDto toDto(Usluga e) {
	    // veze sa servisima idu preko /cenovnik endpointa
	    return new UslugaDto(
	            e.getId(),
	            e.getNaziv(),
	            e.getTrajanje(),
	            e.getJedinicaMere()
	    );
	}

    /**
     * Konvertuje DTO objekat u entitet usluge.
     * Globalna referentna cena se opciono može sačuvati,
     * ali se ne koristi za obračun rezervacija.
     *
     * @param t DTO objekat usluge koji se konvertuje
     * @return entitet usluge spreman za perzistenciju
     */
    @Override
    public Usluga toEntity(UslugaDto t) {
        Usluga u = new Usluga();
        u.setId(t.getId());
        u.setNaziv(t.getNaziv());
        u.setTrajanje(t.getTrajanje());
        u.setJedinicaMere(t.getJedinicaMere());
        return u;
    }
}