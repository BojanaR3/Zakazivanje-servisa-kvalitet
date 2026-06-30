package com.mycompany.njt_mavenproject.servis;

import com.mycompany.njt_mavenproject.dto.impl.ServisDto;
import com.mycompany.njt_mavenproject.entity.impl.Servis;
import com.mycompany.njt_mavenproject.mapper.impl.ServisMapper;
import com.mycompany.njt_mavenproject.repository.impl.ServisRepository;
import com.mycompany.njt_mavenproject.repository.impl.ServisUslugaRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * Servis koji upravlja poslovnom logikom vezanom za auto-servise.
 * Pruža operacije kreiranja, pretraživanja, ažuriranja i brisanja servisa.
 * Prilikom brisanja servisa automatski se brišu i sve stavke cenovnika
 * vezane za taj servis kako bi se očuvao integritet podataka.
 *
 * @author Bojana
 */
@Service
public class ServisServis {

    /** Repozitorijum za pristup podacima o servisima u bazi podataka. */
    private final ServisRepository repo;

    /** Mapper za konverziju između entiteta servisa i DTO objekata. */
    private final ServisMapper mapper;

    /** Repozitorijum za upravljanje cenovnikom, koristi se pri brisanju servisa. */
    private final ServisUslugaRepository cenovnik;

    /**
     * Konstruktor koji injektuje repozitorijume i mapper za servise.
     *
     * @param repo     repozitorijum za pristup podacima o servisima
     * @param mapper   mapper za konverziju između entiteta i DTO objekata
     * @param cenovnik repozitorijum za upravljanje cenovnikom servisa
     */
    public ServisServis(ServisRepository repo, ServisMapper mapper, ServisUslugaRepository cenovnik) {
        this.repo = repo;
        this.mapper = mapper;
        this.cenovnik = cenovnik;
    }

    /**
     * Vraća listu svih servisa u sistemu.
     *
     * @return lista DTO objekata svih servisa
     */
    public List<ServisDto> findAll() {
        return repo.findAll().stream().map(mapper::toDto).toList();
    }

    /**
     * Pronalazi servis na osnovu prosleđenog identifikatora.
     *
     * @param id identifikator servisa koji se traži
     * @return DTO objekat pronađenog servisa
     * @throws Exception ako servis sa datim ID-jem ne postoji
     */
    public ServisDto findById(Long id) throws Exception {
        return mapper.toDto(repo.findById(id));
    }

    /**
     * Kreira novi servis u sistemu.
     *
     * @param dto DTO objekat sa podacima novog servisa
     * @return DTO objekat novokreiranog servisa sa dodeljenim ID-jem
     */
    @Transactional
    public ServisDto create(ServisDto dto) {
        Servis s = mapper.toEntity(dto);
        repo.save(s);
        return mapper.toDto(s);
    }

    /**
     * Ažurira podatke postojećeg servisa.
     * Menjaju se naziv, adresa i telefon servisa.
     *
     * @param dto DTO objekat sa ažuriranim podacima servisa
     * @return DTO objekat ažuriranog servisa
     * @throws RuntimeException ako servis sa datim ID-jem ne postoji
     */
    @Transactional
    public ServisDto update(ServisDto dto) {
        Servis s;
        try { s = repo.findById(dto.getId()); }
        catch (Exception e) { throw new RuntimeException("Servis ne postoji: " + dto.getId()); }
        s.setNaziv(dto.getNaziv());
        s.setAdresa(dto.getAdresa());
        s.setTelefon(dto.getTelefon());
        repo.save(s);
        return mapper.toDto(s);
    }

    /**
     * Briše servis sa datim identifikatorom iz sistema.
     * Pre brisanja servisa brišu se sve stavke cenovnika vezane za taj servis.
     *
     * @param id identifikator servisa koji se briše
     */
    @Transactional
    public void deleteById(Long id) {
        cenovnik.deleteByServis(id);
        repo.deleteById(id);
    }
}