package com.mycompany.njt_mavenproject.controller;

import com.mycompany.njt_mavenproject.dto.impl.MehanicarDto;
import com.mycompany.njt_mavenproject.servis.MehanicarServis;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * REST kontroler za upravljanje mehaničarima.
 * Omogućava pregled, kreiranje, ažuriranje i brisanje mehaničara.
 *
 * @author Bojana
 */
@RestController
@RequestMapping("/api/mehanicari")
public class MehanicarKontroler {

    private final MehanicarServis servis;

    /**
     * Konstruktor koji injektuje servis za mehaničare.
     *
     * @param servis servis za upravljanje mehaničarima
     */
    public MehanicarKontroler(MehanicarServis servis) {
        this.servis = servis;
    }

    /**
     * Vraća listu svih mehaničara.
     *
     * @return lista svih mehaničara
     */
    @GetMapping
    public List<MehanicarDto> findAll() { return servis.findAll(); }

    /**
     * Vraća mehaničara sa zadatim ID-em.
     *
     * @param id jedinstveni identifikator mehaničara
     * @return mehaničar sa zadatim ID-em
     * @throws Exception u slučaju da mehaničar nije pronađen
     */
    @GetMapping("/{id}")
    public ResponseEntity<MehanicarDto> findById(@PathVariable Long id) throws Exception {
        return ResponseEntity.ok(servis.findById(id));
    }

    /**
     * Vraća listu mehaničara koji rade u zadatom servisu.
     *
     * @param servisId jedinstveni identifikator servisa
     * @return lista mehaničara koji rade u zadatom servisu
     */
    @GetMapping("/servis/{servisId}")
    public List<MehanicarDto> findByServis(@PathVariable Long servisId) {
        return servis.findByServis(servisId);
    }

    /**
     * Kreira novog mehaničara.
     *
     * @param dto podaci novog mehaničara
     * @return kreirani mehaničar
     */
    @PostMapping
    public ResponseEntity<MehanicarDto> create(@RequestBody MehanicarDto dto) {
        return ResponseEntity.ok(servis.create(dto));
    }

    /**
     * Ažurira postojećeg mehaničara sa zadatim ID-em.
     *
     * @param id  jedinstveni identifikator mehaničara koji se ažurira
     * @param dto novi podaci mehaničara
     * @return ažurirani mehaničar
     * @throws Exception u slučaju da mehaničar nije pronađen
     */
    @PutMapping("/{id}")
    public ResponseEntity<MehanicarDto> update(@PathVariable Long id, @RequestBody MehanicarDto dto) throws Exception {
        dto.setId(id);
        return ResponseEntity.ok(servis.update(dto));
    }

    /**
     * Briše mehaničara sa zadatim ID-em.
     *
     * @param id jedinstveni identifikator mehaničara koji se briše
     * @return prazan odgovor sa statusom 204
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        servis.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}