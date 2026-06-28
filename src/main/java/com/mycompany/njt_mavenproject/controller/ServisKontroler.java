package com.mycompany.njt_mavenproject.controller;

import com.mycompany.njt_mavenproject.dto.impl.ServisDto;
import com.mycompany.njt_mavenproject.dto.impl.UslugaDto;
import com.mycompany.njt_mavenproject.entity.impl.Usluga;
import com.mycompany.njt_mavenproject.repository.impl.ServisUslugaRepository;
import com.mycompany.njt_mavenproject.repository.impl.UslugaRepository;
import com.mycompany.njt_mavenproject.servis.ServisServis;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;

/**
 * REST kontroler za upravljanje servisima i cenovnikom.
 * Omogućava pregled, kreiranje, ažuriranje i brisanje servisa,
 * kao i upravljanje cenovnikom usluga po servisu.
 *
 * @author Bojana
 */
@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/servis")
public class ServisKontroler {

    private final ServisServis servisServis;
    private final ServisUslugaRepository cenovnikRepo;
    private final UslugaRepository uslugaRepo;

    /**
     * Konstruktor koji injektuje potrebne zavisnosti.
     *
     * @param servisServis servis za upravljanje servisima
     * @param cenovnikRepo repozitorijum za cenovnik
     * @param uslugaRepo   repozitorijum za usluge
     */
    public ServisKontroler(ServisServis servisServis,
                           ServisUslugaRepository cenovnikRepo,
                           UslugaRepository uslugaRepo) {
        this.servisServis = servisServis;
        this.cenovnikRepo = cenovnikRepo;
        this.uslugaRepo = uslugaRepo;
    }

    /**
     * Vraća listu svih servisa.
     *
     * @return lista svih servisa
     */
    @GetMapping
    @Operation(summary = "Retrieve all Servis entities.")
    @ApiResponse(responseCode = "200", content = {
        @Content(schema = @Schema(implementation = ServisDto.class), mediaType = "application/json")
    })
    public ResponseEntity<List<ServisDto>> getAll() {
        return new ResponseEntity<>(servisServis.findAll(), HttpStatus.OK);
    }

    /**
     * Vraća servis sa zadatim ID-em.
     *
     * @param id jedinstveni identifikator servisa
     * @return servis sa zadatim ID-em
     */
    @GetMapping("/{id}")
    public ResponseEntity<ServisDto> getById(
            @NotNull(message = "Should not be null or empty.")
            @PathVariable(value = "id") Long id) {
        try {
            return new ResponseEntity<>(servisServis.findById(id), HttpStatus.OK);
        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "ServisKontroler exception: " + ex.getMessage());
        }
    }

    /**
     * Kreira novi servis.
     *
     * @param servisDto podaci novog servisa
     * @return kreirani servis sa statusom 201
     */
    @PostMapping
    @Operation(summary = "Create a new servis entity.")
    @ApiResponse(responseCode = "201", content = {
        @Content(schema = @Schema(implementation = ServisDto.class), mediaType = "application/json")
    })
    public ResponseEntity<ServisDto> addServis(@Valid @RequestBody @NotNull ServisDto servisDto) {
        try {
            ServisDto saved = servisServis.create(servisDto);
            return new ResponseEntity<>(saved, HttpStatus.CREATED);
        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error while saving servis: " + ex.getMessage());
        }
    }

    /**
     * Briše servis sa zadatim ID-em.
     *
     * @param id jedinstveni identifikator servisa koji se briše
     * @return poruka o uspešnom brisanju, ili 404 ako servis ne postoji
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable(value = "id") Long id) {
        try {
            servisServis.deleteById(id);
            return new ResponseEntity<>("Servis successfully deleted.", HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>("Servis does not exist: " + id, HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Ažurira postojeći servis sa zadatim ID-em.
     *
     * @param id        jedinstveni identifikator servisa koji se ažurira
     * @param servisDto novi podaci servisa
     * @return ažurirani servis
     */
    @PutMapping("/{id}")
    @Operation(summary = "Update an existing servis entity.")
    @ApiResponse(responseCode = "200", content = {
        @Content(schema = @Schema(implementation = ServisDto.class), mediaType = "application/json")
    })
    public ResponseEntity<ServisDto> updateServis(
            @PathVariable Long id,
            @Valid @RequestBody ServisDto servisDto) {
        try {
            servisDto.setId(id);
            ServisDto updated = servisServis.update(servisDto);
            return new ResponseEntity<>(updated, HttpStatus.OK);
        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error while updating servis: " + ex.getMessage());
        }
    }

    /**
     * Vraća listu usluga koje nudi servis sa zadatim ID-em.
     *
     * @param id jedinstveni identifikator servisa
     * @return lista usluga servisa
     */
    @GetMapping("/{id}/usluge")
    public ResponseEntity<List<UslugaDto>> getUslugeByServis(@PathVariable Long id) {
        try {
            ServisDto servis = servisServis.findById(id);
            return new ResponseEntity<>(servis.getUsluge(), HttpStatus.OK);
        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Servis nije pronađen: " + ex.getMessage());
        }
    }

    /**
     * DTO za prikaz stavke cenovnika (usluga sa cenom).
     *
     * @param uslugaId jedinstveni identifikator usluge
     * @param naziv    naziv usluge
     * @param cena     cena usluge u servisu
     */
    public record CenovnikItemDto(Long uslugaId, String naziv, Double cena) {}

    /**
     * DTO za kreiranje ili ažuriranje stavke cenovnika.
     *
     * @param uslugaId jedinstveni identifikator usluge
     * @param cena     cena usluge u servisu
     */
    public record CenovnikUpsertReq(@NotNull Long uslugaId, @NotNull Double cena) {}

    /**
     * Vraća cenovnik servisa sa zadatim ID-em.
     *
     * @param servisId jedinstveni identifikator servisa
     * @return lista stavki cenovnika za zadati servis
     */
    @GetMapping("/{id}/cenovnik")
    public ResponseEntity<List<CenovnikItemDto>> getCenovnik(@PathVariable("id") Long servisId) {
        var rows = cenovnikRepo.findByServisId(servisId);
        var out = rows.stream()
                .map(su -> new CenovnikItemDto(su.getUsluga().getId(),
                                               su.getUsluga().getNaziv(),
                                               su.getCena()))
                .toList();
        return ResponseEntity.ok(out);
    }

    /**
     * Kreira ili ažurira cene usluga u cenovniku servisa.
     *
     * @param servisId jedinstveni identifikator servisa
     * @param body     lista stavki cenovnika koje se kreiraju ili ažuriraju
     * @return prazan odgovor sa statusom 204
     */
    @PutMapping("/{id}/cenovnik")
    public ResponseEntity<Void> upsertCenovnik(@PathVariable("id") Long servisId,
                                               @RequestBody List<@Valid CenovnikUpsertReq> body) {
        if (body == null) body = List.of();
        for (var item : body) {
            cenovnikRepo.upsert(servisId, item.uslugaId(), item.cena());
        }
        return ResponseEntity.noContent().build();
    }
}