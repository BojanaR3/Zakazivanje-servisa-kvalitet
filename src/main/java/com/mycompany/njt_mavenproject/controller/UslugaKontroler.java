/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.njt_mavenproject.controller;

import com.mycompany.njt_mavenproject.dto.impl.UslugaDto;
import com.mycompany.njt_mavenproject.servis.UslugaServis;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

/**
 *
 * @author Korisnik
 */
@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/usluga")

public class UslugaKontroler {
    
    private final UslugaServis uslugaServis;

    public UslugaKontroler(UslugaServis uslugaServis) {
        this.uslugaServis = uslugaServis;
    }
    
    @GetMapping
    @Operation(summary = "Retrieve all Usluga entities.")
    @ApiResponse(responseCode = "200", content = {
        @Content(schema = @Schema(implementation = UslugaDto.class), mediaType = "application/json")
    })
    public ResponseEntity<List<UslugaDto>> getAll() {
        return new ResponseEntity<>(uslugaServis.findAll(), HttpStatus.OK);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<UslugaDto> getById(
            @NotNull(message = "Should not be null or empty.")
            @PathVariable(value = "id") Long id) {
        try {
            return new ResponseEntity<>(uslugaServis.findById(id), HttpStatus.OK);
        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "UslugaKontroler exception: " + ex.getMessage());
        }
    }
    
    @PostMapping
    @Operation(summary = "Create a new usluga entity.")
    @ApiResponse(responseCode = "201", content = {
        @Content(schema = @Schema(implementation = UslugaDto.class), mediaType = "application/json")
    })
    public ResponseEntity<UslugaDto> addUsluga(@Valid @RequestBody @NotNull UslugaDto uslugaDto) {
        try {
         
            UslugaDto saved = uslugaServis.create(uslugaDto);
            return new ResponseEntity<>(saved, HttpStatus.CREATED);
        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error while saving usluga: " + ex.getMessage());
        }
    }
    
    @DeleteMapping("/{id}")
public ResponseEntity<?> delete(@PathVariable Long id) {
    try {
        uslugaServis.deleteById(id);
        return ResponseEntity.ok("Usluga uspešno obrisana.");
    } catch (org.springframework.dao.EmptyResultDataAccessException notFound) {
        // stvarno ne postoji
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usluga ne postoji: " + id);
    } catch (org.springframework.dao.DataIntegrityViolationException fk) {
        // FK constraint – npr. postoje stavke rezervacije koje je koriste
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body("Brisanje nije moguće: usluga se koristi u postojećim rezervacijama/stavkama.");
    } catch (jakarta.persistence.PersistenceException pe) {
        // dodatna zaštita (nekad dođe kao PersistenceException)
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body("Brisanje nije moguće zbog veza (FK). Uklonite zavisnosti pa pokušajte ponovo.");
    } catch (Exception ex) {
        // sve ostalo – 500 sa porukom
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Greška pri brisanju: " + ex.getMessage());
    }
}

    
    @PutMapping("/{id}")
    @Operation(summary = "Update an existing usluga entity.")
    @ApiResponse(responseCode = "200", content = {
        @Content(schema = @Schema(implementation = UslugaDto.class), mediaType = "application/json")
    })
    public ResponseEntity<UslugaDto> updateUsluga(
            @PathVariable Long id,
            @Valid @RequestBody UslugaDto uslugaDto) {
        try {
            uslugaDto.setId(id); // Osiguravamo da se ažurira pravi entitet
            UslugaDto updated = uslugaServis.update(uslugaDto);
            return new ResponseEntity<>(updated, HttpStatus.OK);
        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error while updating usluga: " + ex.getMessage());
        }
    }
    
}
