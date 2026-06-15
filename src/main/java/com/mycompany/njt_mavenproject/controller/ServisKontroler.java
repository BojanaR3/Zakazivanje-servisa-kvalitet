/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.njt_mavenproject.controller;


/**
 *
 * @author Korisnik
 */

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

import java.util.ArrayList;
import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/servis")
public class ServisKontroler {

    private final ServisServis servisServis;
    private final ServisUslugaRepository cenovnikRepo;
    private final UslugaRepository uslugaRepo;

    public ServisKontroler(ServisServis servisServis,
                           ServisUslugaRepository cenovnikRepo,
                           UslugaRepository uslugaRepo) {
        this.servisServis = servisServis;
        this.cenovnikRepo = cenovnikRepo;
        this.uslugaRepo = uslugaRepo;
    }

    // ===== postojeće rute =====

    @GetMapping
    @Operation(summary = "Retrieve all Servis entities.")
    @ApiResponse(responseCode = "200", content = {
        @Content(schema = @Schema(implementation = ServisDto.class), mediaType = "application/json")
    })
    public ResponseEntity<List<ServisDto>> getAll() {
        return new ResponseEntity<>(servisServis.findAll(), HttpStatus.OK);
    }

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

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable(value = "id") Long id) {
        try {
            servisServis.deleteById(id);
            return new ResponseEntity<>("Servis successfully deleted.", HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>("Servis does not exist: " + id, HttpStatus.NOT_FOUND);
        }
    }

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

    @GetMapping("/{id}/usluge")
    public ResponseEntity<List<UslugaDto>> getUslugeByServis(@PathVariable Long id) {
        try {
            ServisDto servis = servisServis.findById(id);
            return new ResponseEntity<>(servis.getUsluge(), HttpStatus.OK);
        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Servis nije pronađen: " + ex.getMessage());
        }
    }

    // ===== CENOVNIK po servisu =====
    // DTO za čitanje/prikaz na frontu
    // DTO-i
public record CenovnikItemDto(Long uslugaId, String naziv, Double cena) {}
public record CenovnikUpsertReq(@NotNull Long uslugaId, @NotNull Double cena) {}

@GetMapping("/{id}/cenovnik")
public ResponseEntity<List<CenovnikItemDto>> getCenovnik(@PathVariable("id") Long servisId) {
    var rows = cenovnikRepo.findByServisId(servisId); // samo postojeće
    var out = rows.stream()
            .map(su -> new CenovnikItemDto(su.getUsluga().getId(),
                                           su.getUsluga().getNaziv(),
                                           su.getCena()))
            .toList();
    return ResponseEntity.ok(out);
}

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
