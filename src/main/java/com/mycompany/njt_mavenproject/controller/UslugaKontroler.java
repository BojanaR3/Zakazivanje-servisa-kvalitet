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
 * REST kontroler za upravljanje uslugama.
 * Omogućava pregled, kreiranje, ažuriranje i brisanje usluga.
 *
 * @author Bojana
 */
@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/usluga")
public class UslugaKontroler {

    private final UslugaServis uslugaServis;

    /**
     * Konstruktor koji injektuje servis za usluge.
     *
     * @param uslugaServis servis za upravljanje uslugama
     */
    public UslugaKontroler(UslugaServis uslugaServis) {
        this.uslugaServis = uslugaServis;
    }

    /**
     * Vraća listu svih usluga.
     *
     * @return lista svih usluga
     */
    @GetMapping
    @Operation(summary = "Retrieve all Usluga entities.")
    @ApiResponse(responseCode = "200", content = {
        @Content(schema = @Schema(implementation = UslugaDto.class), mediaType = "application/json")
    })
    public ResponseEntity<List<UslugaDto>> getAll() {
        return new ResponseEntity<>(uslugaServis.findAll(), HttpStatus.OK);
    }

    /**
     * Vraća uslugu sa zadatim ID-em.
     *
     * @param id jedinstveni identifikator usluge
     * @return usluga sa zadatim ID-em
     */
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

    /**
     * Kreira novu uslugu.
     *
     * @param uslugaDto podaci nove usluge
     * @return kreirana usluga sa statusom 201
     */
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

    /**
     * Briše uslugu sa zadatim ID-em.
     * Vraća odgovarajuću poruku u slučaju da usluga ne postoji,
     * ili da se koristi u postojećim rezervacijama.
     *
     * @param id jedinstveni identifikator usluge koja se briše
     * @return poruka o rezultatu brisanja
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        try {
            uslugaServis.deleteById(id);
            return ResponseEntity.ok("Usluga uspešno obrisana.");
        } catch (org.springframework.dao.EmptyResultDataAccessException notFound) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usluga ne postoji: " + id);
        } catch (org.springframework.dao.DataIntegrityViolationException fk) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Brisanje nije moguće: usluga se koristi u postojećim rezervacijama/stavkama.");
        } catch (jakarta.persistence.PersistenceException pe) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Brisanje nije moguće zbog veza (FK). Uklonite zavisnosti pa pokušajte ponovo.");
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Greška pri brisanju: " + ex.getMessage());
        }
    }

    /**
     * Ažurira postojeću uslugu sa zadatim ID-em.
     *
     * @param id        jedinstveni identifikator usluge koja se ažurira
     * @param uslugaDto novi podaci usluge
     * @return ažurirana usluga
     */
    @PutMapping("/{id}")
    @Operation(summary = "Update an existing usluga entity.")
    @ApiResponse(responseCode = "200", content = {
        @Content(schema = @Schema(implementation = UslugaDto.class), mediaType = "application/json")
    })
    public ResponseEntity<UslugaDto> updateUsluga(
            @PathVariable Long id,
            @Valid @RequestBody UslugaDto uslugaDto) {
        try {
            uslugaDto.setId(id);
            UslugaDto updated = uslugaServis.update(uslugaDto);
            return new ResponseEntity<>(updated, HttpStatus.OK);
        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error while updating usluga: " + ex.getMessage());
        }
    }
}