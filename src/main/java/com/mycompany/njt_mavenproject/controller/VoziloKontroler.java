package com.mycompany.njt_mavenproject.controller;

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

/**
 * REST kontroler za upravljanje vozilima.
 * Omogućava prijavljenom vlasniku da pregleda svoja vozila i dodaje nova.
 *
 * @author Bojana
 */
@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/vozilo")
public class VoziloKontroler {

    private final VoziloRepository vozila;
    private final VlasnikRepository vlasnici;

    /**
     * Konstruktor koji injektuje potrebne zavisnosti.
     *
     * @param vozila    repozitorijum za vozila
     * @param vlasnici  repozitorijum za vlasnike
     */
    public VoziloKontroler(VoziloRepository vozila, VlasnikRepository vlasnici) {
        this.vozila = vozila;
        this.vlasnici = vlasnici;
    }

    /**
     * DTO za kreiranje novog vozila.
     * Prihvata oba naziva polja za godinu proizvodnje radi kompatibilnosti sa frontom.
     *
     * @param marka              marka vozila
     * @param model              model vozila
     * @param godProizvodnje     godina proizvodnje (naziv koji šalje front)
     * @param godinaProizvodnje  godina proizvodnje (alternativni naziv)
     * @param registracija       registarska oznaka vozila
     */
    public record CreateVoziloReq(
            @NotBlank String marka,
            @NotBlank String model,
            Integer godProizvodnje,
            Integer godinaProizvodnje,
            @NotBlank String registracija
    ) {}

    /**
     * Vraća listu vozila trenutno prijavljenog vlasnika.
     *
     * @param auth objekat autentifikacije iz Spring Security konteksta
     * @return lista vozila prijavljenog vlasnika, ili 401 ako korisnik nije prijavljen
     */
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

    /**
     * Kreira novo vozilo za trenutno prijavljenog vlasnika.
     *
     * @param req  podaci novog vozila
     * @param auth objekat autentifikacije iz Spring Security konteksta
     * @return kreirano vozilo sa statusom 201, ili 401 ako korisnik nije prijavljen
     */
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
        Integer god = req.godProizvodnje() != null ? req.godProizvodnje() : req.godinaProizvodnje();
        v.setGodProizvodnje(god);
        v.setRegistracija(req.registracija().trim());
        v.setVlasnik(vlasnik);

        vozila.save(v);

        return new ResponseEntity<>(toDto(v), HttpStatus.CREATED);
    }

    /**
     * Konvertuje entitet vozila u DTO objekat.
     *
     * @param v entitet vozila
     * @return DTO objekat vozila
     */
    private static VoziloDto toDto(Vozilo v) {
        VoziloDto dto = new VoziloDto();
        dto.setId(v.getId());
        dto.setMarka(n(v.getMarka()));
        dto.setModel(n(v.getModel()));
        dto.setRegistracija(n(v.getRegistracija()));
        dto.setGodinaProizvodnje(v.getGodProizvodnje());
        return dto;
    }

    /**
     * Pomoćna metoda koja vraća zadatu vrednost ili null.
     *
     * @param s string vrednost
     * @return ista string vrednost ili null
     */
    private static String n(String s) { return s == null ? null : s; }
}