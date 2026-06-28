package com.mycompany.njt_mavenproject.servis;

import com.mycompany.njt_mavenproject.dto.impl.UslugaDto;
import com.mycompany.njt_mavenproject.entity.impl.Usluga;
import com.mycompany.njt_mavenproject.mapper.impl.UslugaMapper;
import com.mycompany.njt_mavenproject.repository.impl.ServisUslugaRepository;
import com.mycompany.njt_mavenproject.repository.impl.UslugaRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Servis koji upravlja poslovnom logikom vezanom za usluge.
 * Pruža operacije kreiranja, pretraživanja, ažuriranja i brisanja usluga.
 * Prilikom brisanja usluge automatski se brišu i sve stavke cenovnika
 * vezane za tu uslugu kako bi se očuvao integritet podataka.
 *
 * @author Bojana
 */
@Service
public class UslugaServis {

    /** Repozitorijum za pristup podacima o uslugama u bazi podataka. */
    private final UslugaRepository usluge;

    /** Repozitorijum za upravljanje cenovnikom, koristi se pri brisanju usluge. */
    private final ServisUslugaRepository cenovnik;

    /** Mapper za konverziju između entiteta usluge i DTO objekata. */
    private final UslugaMapper mapper;

    /**
     * Konstruktor koji injektuje repozitorijume i mapper za usluge.
     *
     * @param usluge   repozitorijum za pristup podacima o uslugama
     * @param cenovnik repozitorijum za upravljanje cenovnikom usluga
     * @param mapper   mapper za konverziju između entiteta i DTO objekata
     */
    public UslugaServis(UslugaRepository usluge,
                        ServisUslugaRepository cenovnik,
                        UslugaMapper mapper) {
        this.usluge = usluge;
        this.cenovnik = cenovnik;
        this.mapper = mapper;
    }

    /**
     * Vraća listu svih usluga u sistemu.
     *
     * @return lista DTO objekata svih usluga
     */
    public List<UslugaDto> findAll() {
        return usluge.findAll().stream().map(mapper::toDto).collect(Collectors.toList());
    }

    /**
     * Pronalazi uslugu na osnovu prosleđenog identifikatora.
     *
     * @param id identifikator usluge koja se traži
     * @return DTO objekat pronađene usluge
     * @throws Exception ako usluga sa datim ID-jem ne postoji
     */
    public UslugaDto findById(Long id) throws Exception {
        return mapper.toDto(usluge.findById(id));
    }

    /**
     * Kreira novu uslugu u sistemu.
     *
     * @param dto DTO objekat sa podacima nove usluge
     * @return DTO objekat novokreirane usluge sa dodeljenim ID-jem
     */
    @Transactional
    public UslugaDto create(UslugaDto dto) {
        Usluga u = mapper.toEntity(dto);
        usluge.save(u);
        return mapper.toDto(u);
    }

    /**
     * Ažurira podatke postojeće usluge.
     * Menjaju se naziv, trajanje i jedinica mere usluge.
     *
     * @param dto DTO objekat sa ažuriranim podacima usluge
     * @return DTO objekat ažurirane usluge
     * @throws RuntimeException ako usluga sa datim ID-jem ne postoji
     */
    @Transactional
    public UslugaDto update(UslugaDto dto) {
        Usluga existing;
        try { existing = usluge.findById(dto.getId()); }
        catch (Exception e) { throw new RuntimeException("Usluga ne postoji: " + dto.getId()); }
        existing.setNaziv(dto.getNaziv());
        existing.setTrajanje(dto.getTrajanje());
        existing.setJedinicaMere(dto.getJedinicaMere());
        usluge.save(existing);
        return mapper.toDto(existing);
    }

    /**
     * Briše uslugu sa datim identifikatorom iz sistema.
     * Pre brisanja usluge brišu se sve stavke cenovnika vezane za tu uslugu.
     *
     * @param id identifikator usluge koja se briše
     */
    @Transactional
    public void deleteById(Long id) {
        cenovnik.deleteByUsluga(id);
        usluge.deleteById(id);
    }
}