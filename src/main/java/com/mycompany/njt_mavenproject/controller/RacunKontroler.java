package com.mycompany.njt_mavenproject.controller;

import com.mycompany.njt_mavenproject.dto.impl.RacunDto;
import com.mycompany.njt_mavenproject.servis.RacunServis;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/racuni")
public class RacunKontroler {

    private final RacunServis servis;

    public RacunKontroler(RacunServis servis) {
        this.servis = servis;
    }

    @GetMapping
    public List<RacunDto> findAll() { return servis.findAll(); }

    @GetMapping("/{id}")
    public ResponseEntity<RacunDto> findById(@PathVariable Long id) throws Exception {
        return ResponseEntity.ok(servis.findById(id));
    }

    @GetMapping("/rezervacija/{rezervacijaId}")
    public ResponseEntity<RacunDto> findByRezervacija(@PathVariable Long rezervacijaId) {
        RacunDto dto = servis.findByRezervacija(rezervacijaId);
        return dto != null ? ResponseEntity.ok(dto) : ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<RacunDto> create(@RequestBody RacunDto dto) {
        return ResponseEntity.ok(servis.create(dto));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<RacunDto> updateStatus(@PathVariable Long id,
                                                  @RequestParam String status) throws Exception {
        return ResponseEntity.ok(servis.updateStatus(id, status));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        servis.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}