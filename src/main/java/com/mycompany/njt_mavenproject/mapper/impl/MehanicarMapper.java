
package com.mycompany.njt_mavenproject.mapper.impl;

import com.mycompany.njt_mavenproject.dto.impl.MehanicarDto;
import com.mycompany.njt_mavenproject.entity.impl.Mehanicar;
import com.mycompany.njt_mavenproject.entity.impl.Servis;
import com.mycompany.njt_mavenproject.mapper.DtoEntityMapper;
import org.springframework.stereotype.Component;

/**
 * Implementacija interfejsa za mapiranje između objekata tipa
 * {@link Mehanicar} i {@link MehanicarDto}.
 * Omogućava konverziju entiteta u DTO objekat i obrnuto.
 *
 * @author Bojana
 */
@Component
public class MehanicarMapper implements DtoEntityMapper<MehanicarDto, Mehanicar> {

    /**
     * Konvertuje objekat tipa Mehanicar u DTO objekat.
     *
     * @param e entitet mehaničara koji se konvertuje
     * @return DTO objekat mehaničara
     */
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

    /**
     * Konvertuje DTO objekat mehaničara u entitet.
     *
     * @param t DTO objekat mehaničara koji se konvertuje
     * @return entitet mehaničara
     */
    @Override
    public Mehanicar toEntity(MehanicarDto t) {
        Mehanicar m = new Mehanicar();

        m.setId(t.getId());
        m.setIme(t.getIme());
        m.setPrezime(t.getPrezime());
        m.setSpecijalnost(t.getSpecijalnost());
        m.setTelefon(t.getTelefon());

        if (t.getServisId() != null) {
            m.setServis(new Servis(t.getServisId()));
        }

        return m;
    }
}

