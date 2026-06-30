/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.njt_mavenproject.servis;

import com.mycompany.njt_mavenproject.dto.impl.RezervacijaDto;
import com.mycompany.njt_mavenproject.dto.impl.StavkaRezervacijeDto;
import com.mycompany.njt_mavenproject.entity.impl.*;
import com.mycompany.njt_mavenproject.mapper.impl.RezervacijaMapper;
import com.mycompany.njt_mavenproject.repository.impl.RezervacijaRepository;
import com.mycompany.njt_mavenproject.repository.impl.VlasnikRepository;
import com.mycompany.njt_mavenproject.repository.impl.ServisUslugaRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Servis koji upravlja poslovnom logikom vezanom za rezervacije.
 * Pruža operacije kreiranja, pretraživanja i ažuriranja rezervacija,
 * kao i mogućnost otkazivanja i promene termina od strane vlasnika.
 * Prilikom kreiranja rezervacije automatski se računaju cene iz cenovnika,
 * ukupno trajanje i primenjuje popust od 10% za rezervacije u sredu.
 *
 * @author Bojana
 */
@Service
public class RezervacijaService {

    /** Repozitorijum za pristup podacima o rezervacijama u bazi podataka. */
    private final RezervacijaRepository repo;

    /** Repozitorijum za pristup podacima o vlasnicima, koristi se za identifikaciju korisnika. */
    private final VlasnikRepository vlasnici;

    /** Mapper za konverziju između entiteta rezervacije i DTO objekata. */
    private final RezervacijaMapper mapper;

    /** Repozitorijum za dohvatanje cena usluga po servisu iz cenovnika. */
    private final ServisUslugaRepository cenovnik;

    /** Entity manager za direktan pristup JPA kontekstu, koristi se za dohvatanje referenci. */
    @PersistenceContext
    private EntityManager em;

    /**
     * Konstruktor koji injektuje sve potrebne zavisnosti za upravljanje rezervacijama.
     *
     * @param repo     repozitorijum za pristup podacima o rezervacijama
     * @param vlasnici repozitorijum za pristup podacima o vlasnicima
     * @param mapper   mapper za konverziju između entiteta i DTO objekata
     * @param cenovnik repozitorijum za dohvatanje cena iz cenovnika
     */
    public RezervacijaService(RezervacijaRepository repo,
                              VlasnikRepository vlasnici,
                              RezervacijaMapper mapper,
                              ServisUslugaRepository cenovnik) {
        this.repo = repo;
        this.vlasnici = vlasnici;
        this.mapper = mapper;
        this.cenovnik = cenovnik;
    }

    /**
     * Vraća listu svih rezervacija u sistemu.
     *
     * @return lista DTO objekata svih rezervacija
     */
    public List<RezervacijaDto> findAll() {
        return repo.findAll().stream().map(mapper::toDto).toList();
    }

    /**
     * Vraća listu svih rezervacija datog vlasnika.
     *
     * @param vlasnikId identifikator vlasnika čije se rezervacije traže
     * @return lista DTO objekata rezervacija datog vlasnika
     */
    public List<RezervacijaDto> findMine(Long vlasnikId) {
        return repo.findByVlasnikId(vlasnikId).stream().map(mapper::toDto).toList();
    }

    /**
     * Vraća listu svih rezervacija korisnika identifikovanog korisničkim imenom.
     *
     * @param username korisničko ime vlasnika čije se rezervacije traže
     * @return lista DTO objekata rezervacija, ili prazna lista ako korisnik ne postoji
     */
    public List<RezervacijaDto> findMineByUsername(String username) {
        Vlasnik v = vlasnici.findByUsername(username);
        if (v == null) return List.of();
        return findMine(v.getId());
    }

    /**
     * Pronalazi rezervaciju na osnovu prosleđenog identifikatora.
     *
     * @param id identifikator rezervacije koja se traži
     * @return DTO objekat pronađene rezervacije
     * @throws Exception ako rezervacija sa datim ID-jem ne postoji
     */
    public RezervacijaDto findById(Long id) throws Exception {
        return mapper.toDto(repo.findById(id));
    }

    /**
     * Kreira novu rezervaciju u sistemu za prijavljenog korisnika.
     * Za svaku stavku dohvata cenu iz cenovnika servisa, računa ukupno trajanje
     * i proverava da li je termin slobodan. Ako je rezervacija u sredu,
     * primenjuje se popust od 10% na ukupan iznos.
     *
     * @param dto      DTO objekat sa podacima nove rezervacije i stavkama
     * @param username korisničko ime vlasnika koji pravi rezervaciju
     * @return DTO objekat novokreirane rezervacije
     * @throws IllegalArgumentException ako nedostaju obavezni podaci
     * @throws IllegalStateException    ako je termin zauzet ili cena nije definisana
     * @throws Exception                ako korisnik sa datim korisničkim imenom ne postoji
     */
    @Transactional
    public RezervacijaDto create(RezervacijaDto dto, String username) throws Exception {
        if (dto.getDatum() == null) throw new IllegalArgumentException("datum je obavezan.");

        Vlasnik vlasnik = vlasnici.findByUsername(username);
        if (vlasnik == null) throw new Exception("Nepoznat korisnik: " + username);

        Rezervacija r = mapper.toEntity(dto);

        if (r.getServis() == null && dto.getServisId() != null)
            r.setServis(em.getReference(Servis.class, dto.getServisId()));
        if (r.getVozilo() == null && dto.getVoziloId() != null)
            r.setVozilo(em.getReference(Vozilo.class, dto.getVoziloId()));

        r.setVlasnik(vlasnik);
        r.setDatum(dto.getDatum());

        if (r.getServis() == null) throw new IllegalArgumentException("servisId je obavezan.");
        if (r.getVozilo() == null) throw new IllegalArgumentException("voziloId je obavezan.");
        if (dto.getStavke() == null || dto.getStavke().isEmpty())
            throw new IllegalArgumentException("Bar jedna stavka je obavezna.");

        Long servisId = r.getServis().getId();

        for (int i = 0; i < dto.getStavke().size(); i++) {
            StavkaRezervacijeDto sdto = dto.getStavke().get(i);
            StavkaRezervacije s = r.getStavke().get(i);

            if (s.getUsluga() == null && sdto.getUslugaId() != null) {
                Usluga u = em.find(Usluga.class, sdto.getUslugaId());
                if (u == null) throw new IllegalArgumentException("Nepostojeća usluga: #" + sdto.getUslugaId());
                s.setUsluga(u);
            }
            if (s.getKolicina() == null || s.getKolicina() <= 0) s.setKolicina(1);

            Double cena = cenovnik.findCena(servisId, s.getUsluga().getId());
            if (cena == null)
                throw new IllegalStateException("Nema definisane cene za servis #" + servisId +
                        " i uslugu #" + s.getUsluga().getId());
            s.setUnitPrice(cena);
        }

        int trajanjeMin = computeTotalDurationMinutes(r);
        r.setTrajanjeMin(trajanjeMin <= 0 ? 1 : trajanjeMin);

        LocalDateTime start = r.getDatum();
        LocalDateTime end = start.plusMinutes(r.getTrajanjeMin());
        boolean clash = repo.existsOverlap(servisId, start, end);
        if (clash) throw new IllegalStateException("Termin je zauzet za izabrani servis u tom periodu.");

        double total = 0.0;
        for (StavkaRezervacije s : r.getStavke()) {
            int k = (s.getKolicina() == null || s.getKolicina() <= 0) ? 1 : s.getKolicina();
            total += s.getUnitPrice() * k;
        }
        if (isWednesday(r.getDatum())) total *= 0.90;
        r.setUkupanIznos(round2(total));

        r.setStatus(StatusRezervacije.CREATED);
        repo.save(r);
        return mapper.toDto(r);
    }

    /**
     * Ažurira status postojeće rezervacije.
     *
     * @param id     identifikator rezervacije čiji se status menja
     * @param status novi status koji se postavlja
     * @return DTO objekat ažurirane rezervacije
     * @throws Exception ako rezervacija sa datim ID-jem ne postoji
     */
    @Transactional
    public RezervacijaDto updateStatus(Long id, StatusRezervacije status) throws Exception {
        Rezervacija r = repo.findById(id);
        r.setStatus(status);
        repo.save(r);
        return mapper.toDto(r);
    }

    /**
     * Briše rezervaciju sa datim identifikatorom iz sistema.
     *
     * @param id identifikator rezervacije koja se briše
     */
    @Transactional
    public void deleteById(Long id) {
        repo.deleteById(id);
    }

    /**
     * Proverava da li je dati datum sreda.
     *
     * @param dt datum koji se proverava
     * @return {@code true} ako je sreda, {@code false} u suprotnom
     */
    private boolean isWednesday(LocalDateTime dt) {
        return dt != null && dt.getDayOfWeek() == DayOfWeek.WEDNESDAY;
    }

    /**
     * Zaokružuje broj na dve decimale.
     *
     * @param x broj koji se zaokružuje
     * @return broj zaokružen na dve decimale
     */
    private double round2(double x) {
        return Math.round(x * 100.0) / 100.0;
    }

    /**
     * Računa ukupno trajanje rezervacije u minutima na osnovu stavki.
     * Podržava jedinice mere: minuti (podrazumevano), sati (h) i dani (d, dan, dani, rad).
     *
     * @param r rezervacija čije se ukupno trajanje računa
     * @return ukupno trajanje u minutima
     */
    private int computeTotalDurationMinutes(Rezervacija r) {
        if (r.getStavke() == null) return 0;
        int total = 0;
        for (StavkaRezervacije s : r.getStavke()) {
            Usluga u = s.getUsluga();
            if (u == null) continue;
            Integer t = u.getTrajanje();
            String jm = (u.getJedinicaMere() == null) ? "min" : u.getJedinicaMere().trim().toLowerCase();
            if (t == null || t <= 0) continue;

            int perUnitMin;
            switch (jm) {
                case "h": perUnitMin = t * 60; break;
                case "d":
                case "dan":
                case "dani":
                case "rad": perUnitMin = t * 1440; break;
                default: perUnitMin = t;
            }
            int k = (s.getKolicina() == null || s.getKolicina() <= 0) ? 1 : s.getKolicina();
            total += perUnitMin * k;
        }
        return total;
    }

    /**
     * Otkazuje rezervaciju vlasnika ako je u statusu CREATED.
     * Vlasnik može otkazati samo svoju rezervaciju.
     *
     * @param id       identifikator rezervacije koja se otkazuje
     * @param username korisničko ime vlasnika koji otkazuje rezervaciju
     * @throws IllegalStateException ako rezervacija ne pripada korisniku ili nije u statusu CREATED
     * @throws Exception             ako korisnik sa datim korisničkim imenom ne postoji
     */
    @Transactional
    public void cancelMy(Long id, String username) throws Exception {
        Vlasnik vlasnik = vlasnici.findByUsername(username);
        if (vlasnik == null) throw new Exception("Nepoznat korisnik: " + username);

        Rezervacija r = repo.findById(id);
        if (!r.getVlasnik().getId().equals(vlasnik.getId()))
            throw new IllegalStateException("Nije tvoja rezervacija.");
        if (!StatusRezervacije.CREATED.equals(r.getStatus()))
            throw new IllegalStateException("Samo rezervacije u statusu CREATED mogu da se otkažu.");

        repo.deleteById(id);
    }

    /**
     * Menja termin postojeće rezervacije vlasnika ako je u statusu CREATED.
     * Proverava da li je novi termin slobodan pre izmene.
     * Vlasnik može menjati samo svoju rezervaciju.
     *
     * @param id       identifikator rezervacije čiji se termin menja
     * @param novi     novi datum i vreme rezervacije
     * @param username korisničko ime vlasnika koji menja termin
     * @return DTO objekat ažurirane rezervacije sa novim terminom
     * @throws IllegalArgumentException ako novi datum nije prosleđen
     * @throws IllegalStateException    ako rezervacija ne pripada korisniku, nije u statusu CREATED ili je novi termin zauzet
     * @throws Exception                ako korisnik sa datim korisničkim imenom ne postoji
     */
    @Transactional
    public RezervacijaDto rescheduleMy(Long id, LocalDateTime novi, String username) throws Exception {
        if (novi == null) throw new IllegalArgumentException("Novi datum je obavezan.");

        Vlasnik vlasnik = vlasnici.findByUsername(username);
        if (vlasnik == null) throw new Exception("Nepoznat korisnik: " + username);

        Rezervacija r = repo.findById(id);

        if (!r.getVlasnik().getId().equals(vlasnik.getId()))
            throw new IllegalStateException("Nije tvoja rezervacija.");
        if (!StatusRezervacije.CREATED.equals(r.getStatus()))
            throw new IllegalStateException("Samo rezervacije u statusu CREATED mogu da se menjaju.");

        int traj = (r.getTrajanjeMin() == null || r.getTrajanjeMin() <= 0)
                ? computeTotalDurationMinutes(r)
                : r.getTrajanjeMin();

        LocalDateTime start = novi;
        LocalDateTime end = start.plusMinutes(Math.max(traj, 1));
        boolean clash = repo.existsOverlap(r.getServis().getId(), start, end);
        if (clash) throw new IllegalStateException("Termin je zauzet za izabrani servis u tom periodu.");

        r.setDatum(novi);
        r.setTrajanjeMin(Math.max(traj, 1));
        repo.save(r);
        return mapper.toDto(r);
    }
}