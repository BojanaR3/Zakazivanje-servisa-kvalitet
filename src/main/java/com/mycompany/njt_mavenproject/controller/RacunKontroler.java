package com.mycompany.njt_mavenproject.controller;

import com.mycompany.njt_mavenproject.dto.impl.RacunDto;
import com.mycompany.njt_mavenproject.servis.RacunServis;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * REST kontroler za upravljanje računima.
 * Omogućava pregled, kreiranje, ažuriranje statusa i brisanje računa.
 * Dostupno samo korisnicima sa ADMIN ulogom.
 *
 * @author Bojana
 */
@RestController
@RequestMapping("/api/racuni")
public class RacunKontroler {

    private final RacunServis servis;

    /**
     * Konstruktor koji injektuje servis za račune.
     *
     * @param servis servis za upravljanje računima
     */
    public RacunKontroler(RacunServis servis) {
        this.servis = servis;
    }

    /**
     * Vraća listu svih računa.
     *
     * @return lista svih računa
     */
    @GetMapping
    public List<RacunDto> findAll() { return servis.findAll(); }

    /**
     * Vraća račun sa zadatim ID-em.
     *
     * @param id jedinstveni identifikator računa
     * @return račun sa zadatim ID-em
     * @throws Exception u slučaju da račun nije pronađen
     */
    @GetMapping("/{id}")
    public ResponseEntity<RacunDto> findById(@PathVariable Long id) throws Exception {
        return ResponseEntity.ok(servis.findById(id));
    }

    /**
     * Vraća račun koji pripada zadatoj rezervaciji.
     *
     * @param rezervacijaId jedinstveni identifikator rezervacije
     * @return račun za zadatu rezervaciju, ili 404 ako ne postoji
     */
    @GetMapping("/rezervacija/{rezervacijaId}")
    public ResponseEntity<RacunDto> findByRezervacija(@PathVariable Long rezervacijaId) {
        RacunDto dto = servis.findByRezervacija(rezervacijaId);
        return dto != null ? ResponseEntity.ok(dto) : ResponseEntity.notFound().build();
    }

    /**
     * Kreira novi račun.
     *
     * @param dto podaci novog računa
     * @return kreirani račun
     */
    @PostMapping
    public ResponseEntity<RacunDto> create(@RequestBody RacunDto dto) {
        return ResponseEntity.ok(servis.create(dto));
    }

    /**
     * Ažurira status plaćanja računa sa zadatim ID-em.
     *
     * @param id     jedinstveni identifikator računa
     * @param status novi status plaćanja
     * @return ažurirani račun
     * @throws Exception u slučaju da račun nije pronađen
     */
    @PatchMapping("/{id}/status")
    public ResponseEntity<RacunDto> updateStatus(@PathVariable Long id,
                                                  @RequestParam String status) throws Exception {
        return ResponseEntity.ok(servis.updateStatus(id, status));
    }

    /**
     * Briše račun sa zadatim ID-em.
     *
     * @param id jedinstveni identifikator računa koji se briše
     * @return prazan odgovor sa statusom 204
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        servis.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}