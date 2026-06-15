/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.njt_mavenproject.controller;

/**
 *
 * @author Korisnik
 */


import com.mycompany.njt_mavenproject.dto.impl.VoziloDto;
import com.mycompany.njt_mavenproject.entity.impl.Vlasnik;
import com.mycompany.njt_mavenproject.entity.impl.Vozilo;
import com.mycompany.njt_mavenproject.repository.impl.VlasnikRepository;
import com.mycompany.njt_mavenproject.repository.impl.VoziloRepository;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/vozilo")
public class VoziloKontroler {

    private final VoziloRepository vozila;
    private final VlasnikRepository vlasnici;

    public VoziloKontroler(VoziloRepository vozila, VlasnikRepository vlasnici) {
        this.vozila = vozila;
        this.vlasnici = vlasnici;
    }

    // Accept oba naziva godine (front šalje godProizvodnje, tvoj DTO ima godinaProizvodnje)
    public record CreateVoziloReq(
            @NotBlank String marka,
            @NotBlank String model,
            Integer godProizvodnje,
            Integer godinaProizvodnje,
            @NotBlank String registracija
    ) {}

    // ===== GET /api/vozilo/mine =====
    @GetMapping("/mine")
    @Operation(summary = "Vrati vozila ulogovanog vlasnika")
    public ResponseEntity<List<VoziloDto>> getMine(Authentication auth) {
        if (auth == null || auth.getName() == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        String username = auth.getName();

        List<VoziloDto> list = vozila.findByVlasnikUsername(username).stream()
                .map(VoziloKontroler::toDto)
                .toList();

        return ResponseEntity.ok(list);
    }

    // ===== POST /api/vozilo =====
    @PostMapping
    @Transactional
    @Operation(summary = "Kreiraj novo vozilo za ulogovanog vlasnika")
    public ResponseEntity<VoziloDto> create(@RequestBody @NotNull CreateVoziloReq req,
                                            Authentication auth) {
        if (auth == null || auth.getName() == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        String username = auth.getName();
        Vlasnik vlasnik = vlasnici.findByUsername(username);
        if (vlasnik == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Vozilo v = new Vozilo();
        v.setMarka(req.marka().trim());
        v.setModel(req.model().trim());
        // prioritet dajemo godProizvodnje (front), fallback na godinaProizvodnje (DTO)
        Integer god = req.godProizvodnje() != null ? req.godProizvodnje() : req.godinaProizvodnje();
        v.setGodProizvodnje(god);
        v.setRegistracija(req.registracija().trim());
        v.setVlasnik(vlasnik);

        vozila.save(v); // persist/merge

        return new ResponseEntity<>(toDto(v), HttpStatus.CREATED);
    }

    // ===== Mapper: Entity -> DTO (samo bitna polja za checkout) =====
    private static VoziloDto toDto(Vozilo v) {
        VoziloDto dto = new VoziloDto();
        dto.setId(v.getId());
        dto.setMarka(n(v.getMarka()));
        dto.setModel(n(v.getModel()));
        dto.setRegistracija(n(v.getRegistracija()));
        dto.setGodinaProizvodnje(v.getGodProizvodnje()); // DTO koristi "godinaProizvodnje"
        // opcionalna polja ostaju null (kilometraza, tipGoriva, ...)
        return dto;
    }

    private static String n(String s) { return s == null ? null : s; }
}

