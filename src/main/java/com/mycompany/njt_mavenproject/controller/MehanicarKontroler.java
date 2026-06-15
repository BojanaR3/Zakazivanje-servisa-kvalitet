package com.mycompany.njt_mavenproject.controller;

import com.mycompany.njt_mavenproject.dto.impl.MehanicarDto;
import com.mycompany.njt_mavenproject.servis.MehanicarServis;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/mehanicari")
public class MehanicarKontroler {

    private final MehanicarServis servis;

    public MehanicarKontroler(MehanicarServis servis) {
        this.servis = servis;
    }

    @GetMapping
    public List<MehanicarDto> findAll() { return servis.findAll(); }

    @GetMapping("/{id}")
    public ResponseEntity<MehanicarDto> findById(@PathVariable Long id) throws Exception {
        return ResponseEntity.ok(servis.findById(id));
    }

    @GetMapping("/servis/{servisId}")
    public List<MehanicarDto> findByServis(@PathVariable Long servisId) {
        return servis.findByServis(servisId);
    }

    @PostMapping
    public ResponseEntity<MehanicarDto> create(@RequestBody MehanicarDto dto) {
        return ResponseEntity.ok(servis.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MehanicarDto> update(@PathVariable Long id, @RequestBody MehanicarDto dto) throws Exception {
        dto.setId(id);
        return ResponseEntity.ok(servis.update(dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        servis.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}