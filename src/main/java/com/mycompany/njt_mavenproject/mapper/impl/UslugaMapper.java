package com.mycompany.njt_mavenproject.mapper.impl;

import com.mycompany.njt_mavenproject.dto.impl.UslugaDto;
import com.mycompany.njt_mavenproject.entity.impl.Usluga;
import com.mycompany.njt_mavenproject.mapper.DtoEntityMapper;
import org.springframework.stereotype.Component;

@Component
public class UslugaMapper implements DtoEntityMapper<UslugaDto, Usluga> {

    @Override
    public UslugaDto toDto(Usluga e) {
        UslugaDto d = new UslugaDto(
                e.getId(),
                e.getNaziv(),
                e.getTrajanje(),
                e.getJedinicaMere()
        );
        // Ovo polje može ostati radi prikaza, ali ne služi za obračun
      
        // d.setServisi(...) i d.setServisIds(...) više NE punimo — veze idu preko /cenovnik
        return d;
    }

    @Override
    public Usluga toEntity(UslugaDto t) {
        Usluga u = new Usluga();
        u.setId(t.getId());
        u.setNaziv(t.getNaziv());
        u.setTrajanje(t.getTrajanje());
        u.setJedinicaMere(t.getJedinicaMere());
        // opcionalno, čuvamo ako želiš “globalnu” referentnu cenu
 
        return u;
    }
}
