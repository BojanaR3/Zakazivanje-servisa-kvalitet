package com.mycompany.njt_mavenproject.controller;

import com.mycompany.njt_mavenproject.dto.impl.AuthResponse;
import com.mycompany.njt_mavenproject.dto.impl.LoginRequest;
import com.mycompany.njt_mavenproject.dto.impl.RegisterRequest;
import com.mycompany.njt_mavenproject.dto.impl.VlasnikDto;
import com.mycompany.njt_mavenproject.entity.impl.Uloga;
import com.mycompany.njt_mavenproject.entity.impl.VerificationToken;
import com.mycompany.njt_mavenproject.entity.impl.Vlasnik;
import com.mycompany.njt_mavenproject.repository.impl.VerificationTokenRepository;
import com.mycompany.njt_mavenproject.repository.impl.VlasnikRepository;
import com.mycompany.njt_mavenproject.servis.AuthService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST kontroler za autentifikaciju i upravljanje korisničkim nalozima.
 * Omogućava registraciju, prijavu, odjavu, verifikaciju emaila i reset lozinke.
 *
 * @author Bojana
 */
@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/auth")
@Tag(name = "Auth")
public class AuthKontroler {

    private final AuthService authService;
    private final VerificationTokenRepository tokens;
    private final VlasnikRepository users;

    /**
     * Konstruktor koji injektuje potrebne zavisnosti.
     *
     * @param authService servis za autentifikaciju
     * @param tokens      repozitorijum za verifikacione tokene
     * @param users       repozitorijum za korisnike
     */
    public AuthKontroler(AuthService authService, VerificationTokenRepository tokens, VlasnikRepository users) {
        this.authService = authService;
        this.tokens = tokens;
        this.users = users;
    }

    /**
     * Registruje novog korisnika i šalje email za verifikaciju naloga.
     *
     * @param req podaci za registraciju
     * @return DTO novokreiranog korisnika
     * @throws Exception u slučaju da username ili email već postoje
     */
    @PostMapping("/register")
    public ResponseEntity<VlasnikDto> register(@Valid @RequestBody RegisterRequest req) throws Exception {
        return ResponseEntity.ok(authService.register(req));
    }

    /**
     * Prijavljuje korisnika i vraća JWT token.
     *
     * @param req podaci za prijavu (username i lozinka)
     * @return JWT token i podaci o korisniku, ili poruka o grešci
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest req) {
        try {
            return ResponseEntity.ok(authService.login(req));
        } catch (DisabledException ex) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of(
                            "code", "ACCOUNT_DISABLED",
                            "message", "Nalog nije aktiviran. Proveri email i klikni na link za potvrdu."
                    ));
        } catch (BadCredentialsException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of(
                            "code", "BAD_CREDENTIALS",
                            "message", "Pogrešno korisničko ime ili lozinka."
                    ));
        }
    }

    /**
     * Odjavljuje korisnika. Pošto je JWT stateless, odjava se vrši brisanjem tokena na klijentu.
     *
     * @return prazan odgovor sa statusom 200
     */
    @PostMapping("/logout")
    public ResponseEntity<Void> logout() {
        return ResponseEntity.ok().build();
    }

    /**
     * Vraća podatke o trenutno prijavljenom korisniku.
     *
     * @param auth objekat autentifikacije iz Spring Security konteksta
     * @return DTO trenutno prijavljenog korisnika
     * @throws Exception u slučaju da korisnik nije pronađen
     */
    @GetMapping("/me")
    public ResponseEntity<VlasnikDto> me(Authentication auth) throws Exception {
        Vlasnik u = users.findByUsername(auth.getName());
        VlasnikDto dto = new VlasnikDto(u.getId(), u.getIme(), u.getPrezime(), u.getUsername(), u.getEmail(), u.getUloga());
        return ResponseEntity.ok(dto);
    }

    /**
     * Verifikuje email adresu korisnika putem tokena poslatog na email.
     *
     * @param token verifikacioni token
     * @return poruka o uspešnoj aktivaciji ili grešci
     */
    @GetMapping("/verify")
    public ResponseEntity<?> verify(@RequestParam String token) {
        VerificationToken vt = tokens.find(token);
        if (vt == null) return ResponseEntity.badRequest().body("Neispravan token.");
        if (vt.isExpired()) {
            tokens.delete(vt);
            return ResponseEntity.badRequest().body("Token je istekao.");
        }
        Vlasnik u = vt.getVlasnik();
        u.setEnabled(true);
        users.save(u);
        tokens.delete(vt);
        return ResponseEntity.ok("Nalog aktiviran. Sada se možete prijaviti.");
    }

    /**
     * Šalje email sa linkom za reset lozinke na zadatu email adresu.
     * Uvek vraća status 200 kako se ne bi otkrivalo da li email postoji u sistemu.
     *
     * @param body mapa koja sadrži ključ "email"
     * @return prazan odgovor sa statusom 200
     */
    @PostMapping("/forgot-password")
    public ResponseEntity<Void> forgotPassword(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        authService.requestPasswordReset(email);
        return ResponseEntity.ok().build();
    }

    /**
     * Resetuje lozinku korisnika pomoću tokena za reset.
     *
     * @param body mapa koja sadrži ključeve "token" i "password"
     * @return poruka o uspešnoj promeni lozinke ili grešci
     */
    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody Map<String, String> body) {
        String token = body.get("token");
        String password = body.get("password");
        if (password == null || password.length() < 6) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Lozinka mora imati bar 6 karaktera.");
        }
        authService.resetPassword(token, password);
        return ResponseEntity.ok("Lozinka je promenjena. Sada se možete prijaviti.");
    }
}