/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.njt_mavenproject.servis;

/**
 *
 * @author Korisnik
 */


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
import java.util.stream.Collectors;

@Service
public class RezervacijaService {

    private final RezervacijaRepository repo;
    private final VlasnikRepository vlasnici;
    private final RezervacijaMapper mapper;
    private final ServisUslugaRepository cenovnik;

    @PersistenceContext
    private EntityManager em;

    public RezervacijaService(RezervacijaRepository repo,
                              VlasnikRepository vlasnici,
                              RezervacijaMapper mapper,
                              ServisUslugaRepository cenovnik) {
        this.repo = repo;
        this.vlasnici = vlasnici;
        this.mapper = mapper;
        this.cenovnik = cenovnik;
    }

    public List<RezervacijaDto> findAll() {
        return repo.findAll().stream().map(mapper::toDto).collect(Collectors.toList());
    }
    public List<RezervacijaDto> findMine(Long vlasnikId) {
        return repo.findByVlasnikId(vlasnikId).stream().map(mapper::toDto).collect(Collectors.toList());
    }
    public List<RezervacijaDto> findMineByUsername(String username) {
        Vlasnik v = vlasnici.findByUsername(username);
        if (v == null) return List.of();
        return findMine(v.getId());
    }
    public RezervacijaDto findById(Long id) throws Exception {
        return mapper.toDto(repo.findById(id));
    }

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

        // 1) Za svaku stavku nađi cenu iz servis_usluga i upiši je u unitPrice
        for (int i = 0; i < dto.getStavke().size(); i++) {
            StavkaRezervacijeDto sdto = dto.getStavke().get(i);
            StavkaRezervacije s = r.getStavke().get(i);

            if (s.getUsluga() == null && sdto.getUslugaId() != null) {
                Usluga u = em.find(Usluga.class, sdto.getUslugaId());
                if (u == null) throw new IllegalArgumentException("Nepostojeća usluga: #" + sdto.getUslugaId());
                s.setUsluga(u);
            }
            if (s.getKolicina() == null || s.getKolicina() <= 0) s.setKolicina(1);
            //s.setPopustProcenat(0.0);

            Double cena = cenovnik.findCena(servisId, s.getUsluga().getId());
            if (cena == null)
                throw new IllegalStateException("Nema definisane cene za servis #" + servisId +
                        " i uslugu #" + s.getUsluga().getId());
            s.setUnitPrice(cena);
        }

        // 2) Trajanje
        int trajanjeMin = computeTotalDurationMinutes(r);
        r.setTrajanjeMin(trajanjeMin <= 0 ? 1 : trajanjeMin);

        // 3) Overlap
        LocalDateTime start = r.getDatum();
        LocalDateTime end = start.plusMinutes(r.getTrajanjeMin());
        boolean clash = repo.existsOverlap(servisId, start, end);
        if (clash) throw new IllegalStateException("Termin je zauzet za izabrani servis u tom periodu.");

        // 4) Ukupan iznos = zbir (unitPrice * kolicina); sreda -10%
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

    @Transactional
    public RezervacijaDto updateStatus(Long id, StatusRezervacije status) throws Exception {
        Rezervacija r = repo.findById(id);
        r.setStatus(status);
        repo.save(r);
        return mapper.toDto(r);
    }
    @Transactional
    public void deleteById(Long id) { repo.deleteById(id); }

    // Helpers
    private boolean isWednesday(LocalDateTime dt) {
        return dt != null && dt.getDayOfWeek() == DayOfWeek.WEDNESDAY;
    }
    private double round2(double x) { return Math.round(x * 100.0) / 100.0; }

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
                default:  perUnitMin = t;
            }
            int k = (s.getKolicina() == null || s.getKolicina() <= 0) ? 1 : s.getKolicina();
            total += perUnitMin * k;
        }
        return total;
    }
    
    // VLASNIK: otkaži svoju rezervaciju (samo CREATED)
@Transactional
public void cancelMy(Long id, String username) throws Exception {
    // ko sam ja
    Vlasnik vlasnik = vlasnici.findByUsername(username);
    if (vlasnik == null) throw new Exception("Nepoznat korisnik: " + username);

    Rezervacija r = repo.findById(id);
    if (!r.getVlasnik().getId().equals(vlasnik.getId()))
        throw new IllegalStateException("Nije tvoja rezervacija.");
    if (r.getStatus() != StatusRezervacije.CREATED)
        throw new IllegalStateException("Samo rezervacije u statusu CREATED mogu da se otkažu.");

    repo.deleteById(id);
}

// VLASNIK: promeni termin svoje rezervacije (samo CREATED)
@Transactional
public RezervacijaDto rescheduleMy(Long id, LocalDateTime novi, String username) throws Exception {
    if (novi == null) throw new IllegalArgumentException("Novi datum je obavezan.");

    Vlasnik vlasnik = vlasnici.findByUsername(username);
    if (vlasnik == null) throw new Exception("Nepoznat korisnik: " + username);

    Rezervacija r = repo.findById(id);

    if (!r.getVlasnik().getId().equals(vlasnik.getId()))
        throw new IllegalStateException("Nije tvoja rezervacija.");
    if (r.getStatus() != StatusRezervacije.CREATED)
        throw new IllegalStateException("Samo rezervacije u statusu CREATED mogu da se menjaju.");

    // trajanje za overlap (postoji u entitetu)
    int traj = (r.getTrajanjeMin() == null || r.getTrajanjeMin() <= 0)
            ? computeTotalDurationMinutes(r)
            : r.getTrajanjeMin();

    // provera zauzeća
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









