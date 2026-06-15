/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
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
 *
 * @author Korisnik
 */
@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/auth")
@Tag(name = "Auth")

public class AuthKontroler {
    
    private final AuthService authService;
    private final VerificationTokenRepository tokens;
    private final VlasnikRepository users;

    public AuthKontroler(AuthService authService, VerificationTokenRepository tokens, VlasnikRepository users) {
        this.authService = authService;
        this.tokens = tokens;
        this.users = users;
    }

 
    @PostMapping("/register")
    public ResponseEntity<VlasnikDto> register(@Valid @RequestBody RegisterRequest req) throws Exception {
        return ResponseEntity.ok(authService.register(req));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest req) {
    try {
        return ResponseEntity.ok(authService.login(req));
    } catch (DisabledException ex) {
        // nalog postoji ali nije aktiviran (enabled=false)
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(Map.of(
                        "code", "ACCOUNT_DISABLED",
                        "message", "Nalog nije aktiviran. Proveri email i klikni na link za potvrdu."
                ));
    } catch (BadCredentialsException ex) {
        // korisničko ime ili lozinka nisu tačni
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of(
                        "code", "BAD_CREDENTIALS",
                        "message", "Pogrešno korisničko ime ili lozinka."
                ));
    }
}

    @PostMapping("/logout")
    public ResponseEntity<Void> logout() {
        // JWT je stateless -> "logout" na klijentu (obriši token).
        return ResponseEntity.ok().build();
    }

    @GetMapping("/me")
    public ResponseEntity<VlasnikDto> me(Authentication auth) throws Exception {
        // auth.getName() je username iz SecurityContext-a
        Vlasnik u = users.findByUsername(auth.getName());
        VlasnikDto dto = new VlasnikDto(u.getId(), u.getIme(), u.getPrezime(), u.getUsername(), u.getEmail(), u.getUloga());
        return ResponseEntity.ok(dto);
    }
    
    @GetMapping("/verify")
    public ResponseEntity<?> verify(@RequestParam String token){
        VerificationToken vt = tokens.find(token);
        if (vt == null) return ResponseEntity.badRequest().body("Neispravan token.");
        if (vt.isExpired()){
            tokens.delete(vt);
            return ResponseEntity.badRequest().body("Token je istekao.");
        }
        Vlasnik u = vt.getVlasnik();
        u.setEnabled(true);
        users.save(u);      // merge/update po tvom repou
        tokens.delete(vt);  // potroši token

        // možeš vratiti plain tekst, ili redirect na frontend
          return ResponseEntity.ok("Nalog aktiviran. Sada se možete prijaviti.");
       /* return ResponseEntity.status(302)
            .header("Location", frontendUrl + "/login?verified=1")
            .build();*/
    }

    
    @PostMapping("/forgot-password")
    public ResponseEntity<Void> forgotPassword(@RequestBody Map<String,String> body) {
        String email = body.get("email");
        // čak i ako je null – vrati 200 da ne otkrivamo ništa
        authService.requestPasswordReset(email);
        return ResponseEntity.ok().build();
    }
    
    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody Map<String,String> body) {
        String token = body.get("token");
        String password = body.get("password");
        if (password == null || password.length() < 6) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Lozinka mora imati bar 6 karaktera.");
        }
        authService.resetPassword(token, password);
        return ResponseEntity.ok("Lozinka je promenjena. Sada se možete prijaviti.");
    }
    
}
