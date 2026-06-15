/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.njt_mavenproject.controller;

/**
 *
 * @author Korisnik
 */
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

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/rezervacija")
public class RezervacijaKontroler {

    private final RezervacijaService service;

    public RezervacijaKontroler(RezervacijaService service) {
        this.service = service;
    }

    // ===== ADMIN: sve rezervacije
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<RezervacijaDto>> all() {
        return ResponseEntity.ok(service.findAll());
    }

    // ===== VLASNIK: svoje rezervacije
    @GetMapping("/moje")
    public ResponseEntity<List<RezervacijaDto>> my(Authentication auth) {
        return ResponseEntity.ok(service.findMineByUsername(auth.getName())); 
    }

    // ===== ADMIN: po ID
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<RezervacijaDto> byId(@PathVariable Long id) throws Exception {
        return ResponseEntity.ok(service.findById(id));
    }

    // ===== VLASNIK: kreiraj rezervaciju sa stavkama
    @PostMapping
    @Operation(summary = "Kreiraj rezervaciju sa stavkama (VLASNIK)")
    public ResponseEntity<?> create(@Valid @RequestBody @NotNull RezervacijaDto dto,
                                    Authentication auth) {
        try {
            RezervacijaDto saved = service.create(dto, auth.getName()); // vlasnik iz JWT (username)
            return new ResponseEntity<>(saved, HttpStatus.CREATED);
        } catch (IllegalStateException dup) {
            // npr. kad je termin već zauzet (unique constraint na (servis_id, datum))
            return new ResponseEntity<>(dup.getMessage(), HttpStatus.CONFLICT);
        } catch (Exception ex) {
            return new ResponseEntity<>("Greška: " + ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    // ===== ADMIN: promeni status
    @PatchMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<RezervacijaDto> updateStatus(@PathVariable Long id,
                                                       @RequestParam StatusRezervacije status) throws Exception {
        return ResponseEntity.ok(service.updateStatus(id, status));
    }

    // ===== ADMIN: obriši
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.ok("Rezervacija obrisana");
    }
    
    // ===== VLASNIK: otkaži (samo ako je CREATED i ako je moja)
    @DeleteMapping("/{id}/moja")
    public ResponseEntity<?> cancelMy(@PathVariable Long id, Authentication auth) {
        try {
            service.cancelMy(id, auth.getName());
            return ResponseEntity.noContent().build();
        } catch (IllegalStateException ise) {
            return new ResponseEntity<>(ise.getMessage(), HttpStatus.CONFLICT);
        } catch (Exception ex) {
            return new ResponseEntity<>("Greška: " + ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    // ===== VLASNIK: promeni termin (samo ako je CREATED i ako je moja)
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
            return new ResponseEntity<>("Greška: " + ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}

