package com.mycompany.njt_mavenproject.controller;

import com.mycompany.njt_mavenproject.dto.impl.RezervacijaDto;
import com.mycompany.njt_mavenproject.entity.impl.StatusRezervacije;
import com.mycompany.njt_mavenproject.servis.RezervacijaService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * REST kontroler za upravljanje rezervacijama.
 * ADMIN može da pregleda sve rezervacije, menja status i briše.
 * VLASNIK može da kreira, pregleda svoje, menja termin i otkazuje svoje rezervacije.
 *
 * @author Bojana
 */
@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/rezervacija")
public class RezervacijaKontroler {

    private static final String GRESKA_PREFIX = "Greška: ";

    private final RezervacijaService service;

    /**
     * Konstruktor koji injektuje servis za rezervacije.
     *
     * @param service servis za upravljanje rezervacijama
     */
    public RezervacijaKontroler(RezervacijaService service) {
        this.service = service;
    }

    /**
     * Vraća listu svih rezervacija. Dostupno samo ADMINu.
     *
     * @return lista svih rezervacija
     */
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<RezervacijaDto>> all() {
        return ResponseEntity.ok(service.findAll());
    }

    /**
     * Vraća listu rezervacija trenutno prijavljenog vlasnika.
     *
     * @param auth objekat autentifikacije iz Spring Security konteksta
     * @return lista rezervacija prijavljenog vlasnika
     */
    @GetMapping("/moje")
    public ResponseEntity<List<RezervacijaDto>> my(Authentication auth) {
        return ResponseEntity.ok(service.findMineByUsername(auth.getName()));
    }

    /**
     * Vraća rezervaciju sa zadatim ID-em. Dostupno samo ADMINu.
     *
     * @param id jedinstveni identifikator rezervacije
     * @return rezervacija sa zadatim ID-em
     * @throws Exception u slučaju da rezervacija nije pronađena
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<RezervacijaDto> byId(@PathVariable Long id) throws Exception {
        return ResponseEntity.ok(service.findById(id));
    }

    /**
     * Kreira novu rezervaciju sa stavkama za prijavljenog vlasnika.
     *
     * @param dto  podaci nove rezervacije
     * @param auth objekat autentifikacije iz Spring Security konteksta
     * @return kreirana rezervacija sa statusom 201, ili poruka o grešci
     */
    @PostMapping
    @Operation(summary = "Kreiraj rezervaciju sa stavkama (VLASNIK)")
    public ResponseEntity<?> create(@Valid @RequestBody @NotNull RezervacijaDto dto,
                                    Authentication auth) {
        try {
            RezervacijaDto saved = service.create(dto, auth.getName());
            return new ResponseEntity<>(saved, HttpStatus.CREATED);
        } catch (IllegalStateException dup) {
            return new ResponseEntity<>(dup.getMessage(), HttpStatus.CONFLICT);
        } catch (Exception ex) {
            return new ResponseEntity<>(GRESKA_PREFIX + ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Menja status rezervacije sa zadatim ID-em. Dostupno samo ADMINu.
     *
     * @param id     jedinstveni identifikator rezervacije
     * @param status novi status rezervacije
     * @return ažurirana rezervacija
     * @throws Exception u slučaju da rezervacija nije pronađena
     */
    @PatchMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<RezervacijaDto> updateStatus(@PathVariable Long id,
                                                       @RequestParam StatusRezervacije status) throws Exception {
        return ResponseEntity.ok(service.updateStatus(id, status));
    }

    /**
     * Briše rezervaciju sa zadatim ID-em. Dostupno samo ADMINu.
     *
     * @param id jedinstveni identifikator rezervacije koja se briše
     * @return poruka o uspešnom brisanju
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.ok("Rezervacija obrisana");
    }

    /**
     * Otkazuje rezervaciju prijavljenog vlasnika. Moguće samo ako je rezervacija u statusu CREATED.
     *
     * @param id   jedinstveni identifikator rezervacije
     * @param auth objekat autentifikacije iz Spring Security konteksta
     * @return prazan odgovor sa statusom 204, ili poruka o grešci
     */
    @DeleteMapping("/{id}/moja")
    public ResponseEntity<?> cancelMy(@PathVariable Long id, Authentication auth) {
        try {
            service.cancelMy(id, auth.getName());
            return ResponseEntity.noContent().build();
        } catch (IllegalStateException ise) {
            return new ResponseEntity<>(ise.getMessage(), HttpStatus.CONFLICT);
        } catch (Exception ex) {
            return new ResponseEntity<>(GRESKA_PREFIX + ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Menja termin rezervacije prijavljenog vlasnika. Moguće samo ako je rezervacija u statusu CREATED.
     *
     * @param id   jedinstveni identifikator rezervacije
     * @param novi novi datum i vreme rezervacije
     * @param auth objekat autentifikacije iz Spring Security konteksta
     * @return ažurirana rezervacija, ili poruka o grešci
     */
    @PatchMapping("/{id}/termin")
    public ResponseEntity<?> rescheduleMy(@PathVariable Long id,
                                          @RequestParam("datum")
                                          @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                                          LocalDateTime novi,
                                          Authentication auth) {
        try {
            RezervacijaDto dto = service.rescheduleMy(id, novi, auth.getName());
            return ResponseEntity.ok(dto);
        } catch (IllegalStateException clash) {
            return new ResponseEntity<>(clash.getMessage(), HttpStatus.CONFLICT);
        } catch (Exception ex) {
            return new ResponseEntity<>(GRESKA_PREFIX + ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}