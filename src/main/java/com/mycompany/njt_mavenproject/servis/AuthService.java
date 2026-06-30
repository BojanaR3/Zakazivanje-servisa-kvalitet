/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.njt_mavenproject.servis;

import com.mycompany.njt_mavenproject.dto.impl.AuthResponse;
import com.mycompany.njt_mavenproject.dto.impl.LoginRequest;
import com.mycompany.njt_mavenproject.dto.impl.RegisterRequest;
import com.mycompany.njt_mavenproject.dto.impl.VlasnikDto;
import com.mycompany.njt_mavenproject.entity.impl.PasswordResetToken;
import com.mycompany.njt_mavenproject.entity.impl.Uloga;
import com.mycompany.njt_mavenproject.entity.impl.VerificationToken;
import com.mycompany.njt_mavenproject.entity.impl.Vlasnik;
import com.mycompany.njt_mavenproject.exception.RegistrationException;
import com.mycompany.njt_mavenproject.mapper.impl.VlasnikMapper;
import com.mycompany.njt_mavenproject.repository.impl.PasswordResetTokenRepository;
import com.mycompany.njt_mavenproject.repository.impl.VerificationTokenRepository;
import com.mycompany.njt_mavenproject.repository.impl.VlasnikRepository;
import com.mycompany.njt_mavenproject.security.JwtService;
import jakarta.transaction.Transactional;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Servis koji upravlja autentifikacijom i autorizacijom korisnika.
 * Pruža funkcionalnosti registracije, prijave, verifikacije email adrese
 * i resetovanja lozinke putem email tokena.
 *
 * @author Bojana
 */
@Service
public class AuthService {

    /** Menadžer za autentifikaciju korisnika putem Spring Security. */
    private final AuthenticationManager authManager;

    /** Servis za generisanje i validaciju JWT tokena. */
    private final JwtService jwt;

    /** Repozitorijum za pristup podacima o vlasnicima. */
    private final VlasnikRepository users;

    /** Repozitorijum za čuvanje i pretragu verifikacionih tokena. */
    private final VerificationTokenRepository tokens;

    /** Enkoder koji se koristi za hashovanje lozinki pre čuvanja u bazi. */
    private final PasswordEncoder encoder;

    /** Mapper za konverziju između entiteta i DTO objekata vlasnika. */
    private final VlasnikMapper userMapper;

    /** Repozitorijum za čuvanje i pretragu tokena za resetovanje lozinke. */
    private final PasswordResetTokenRepository resetTokens;

    /** Servis za slanje email poruka korisnicima. */
    private final MailService mail;

    /** URL adresa frontend aplikacije, koristi se za generisanje linkova u emailovima. */
    @Value("${app.frontend.url}")
    private String frontendUrl;

    /**
     * Konstruktor koji injektuje sve potrebne zavisnosti za autentifikaciju.
     *
     * @param authManager  Spring Security menadžer za autentifikaciju
     * @param jwt          servis za generisanje i validaciju JWT tokena
     * @param users        repozitorijum za pristup podacima o vlasnicima
     * @param tokens       repozitorijum za verifikacione tokene
     * @param encoder      enkoder za hashovanje lozinki
     * @param userMapper   mapper za konverziju između entiteta i DTO objekata vlasnika
     * @param resetTokens  repozitorijum za tokene resetovanja lozinke
     * @param mail         servis za slanje email poruka
     */
    public AuthService(AuthenticationManager authManager, JwtService jwt, VlasnikRepository users,
                       VerificationTokenRepository tokens, PasswordEncoder encoder,
                       VlasnikMapper userMapper, PasswordResetTokenRepository resetTokens,
                       MailService mail) {
        this.authManager = authManager;
        this.jwt = jwt;
        this.users = users;
        this.tokens = tokens;
        this.encoder = encoder;
        this.userMapper = userMapper;
        this.resetTokens = resetTokens;
        this.mail = mail;
    }

    /**
     * Registruje novog vlasnika u sistemu.
     * Proverava jedinstvenost korisničkog imena i email adrese,
     * kreira nalog sa deaktiviranim statusom i šalje verifikacioni email.
     *
     * @param req podaci za registraciju novog korisnika
     * @return DTO objekat novokreiranog vlasnika
     * @throws RegistrationException ako korisničko ime ili email adresa već postoje u sistemu
     */
    public VlasnikDto register(RegisterRequest req) throws RegistrationException {
        if (users.existsByUsername(req.getUsername()))
            throw new RegistrationException("Username already taken");
        if (users.existsByEmail(req.getEmail()))
            throw new RegistrationException("Email already taken");

        Vlasnik u = new Vlasnik();
        u.setIme(req.getIme());
        u.setPrezime(req.getPrezime());
        u.setUsername(req.getUsername());
        u.setEmail(req.getEmail());
        u.setLozinka(encoder.encode(req.getLozinka()));
        u.setUloga(Uloga.VLASNIK);
        u.setEnabled(false);

        users.save(u);
        var vt = VerificationToken.of(u, 86400);
        tokens.save(vt);

        String verifyUrl = "http://localhost:8080/api/auth/verify?token=" + vt.getToken();
        String body = """
                Zdravo %s,

                Hvala na registraciji. Molimo potvrdite email klikom na link:

                %s

                Link važi 24h.
                """.formatted(u.getUsername(), verifyUrl);

        mail.send(u.getEmail(), "Potvrda naloga", body);

        return userMapper.toDto(u);
    }

    /**
     * Autentifikuje korisnika i generiše JWT token za pristup sistemu.
     *
     * @param req podaci za prijavu koji sadrže korisničko ime i lozinku
     * @return odgovor koji sadrži JWT token i podatke o prijavljenom korisniku
     */
    public AuthResponse login(LoginRequest req) {
        Authentication auth = authManager.authenticate(
            new UsernamePasswordAuthenticationToken(req.getUsername(), req.getLozinka())
        );

        Vlasnik me = users.findByUsername(req.getUsername());

        String token = jwt.generate(
            (org.springframework.security.core.userdetails.User) auth.getPrincipal(),
            Map.of("role", me.getUloga() != null ? me.getUloga().name() : "VLASNIK")
        );

        return new AuthResponse(token, userMapper.toDto(me));
    }

    /**
     * Pokreće proces resetovanja lozinke za korisnika sa datom email adresom.
     * Kreira token koji važi 30 minuta i šalje HTML email sa linkom za reset.
     * Ako email ne postoji u sistemu, metoda se završava bez greške kako se
     * ne bi otkrivalo koji emailovi postoje u bazi.
     *
     * @param email email adresa korisnika koji želi da resetuje lozinku
     */
    @Transactional
    public void requestPasswordReset(String email) {
        Vlasnik u = users.findByEmail(email);
        if (u == null) {
            return;
        }

        PasswordResetToken t = PasswordResetToken.of(u, 1800);
        resetTokens.save(t);

        String link = frontendUrl + "/reset?token=" + t.getToken();
        String html = buildResetEmailHtml(u.getUsername(), link);

        mail.sendHtml(u.getEmail(), "Reset lozinke", html);
    }

    /**
     * Kreira HTML sadržaj emaila za resetovanje lozinke.
     *
     * @param username korisničko ime primaoca emaila
     * @param link     URL link za resetovanje lozinke
     * @return HTML string spreman za slanje emailom
     */
    private String buildResetEmailHtml(String username, String link) {
        return """
        <div style="font-family: Inter,Segoe UI,Arial,sans-serif; max-width: 560px; margin: 0 auto; padding: 24px; background:#f7f8fb;">
          <div style="background:#fff; border:1px solid #e6e8ef; border-radius:14px; padding:24px;">
            <h2 style="margin:0 0 8px; color:#0f172a;">Pozdrav %s,</h2>
            <p style="margin:0 0 16px; color:#475569;">Dobili smo zahtev za reset lozinke. Klikni na dugme ispod da postaviš novu lozinku.</p>
            <div style="text-align:center; margin:24px 0;">
              <a href="%s" style="display:inline-block; padding:12px 18px; background:#2563eb; color:#fff; text-decoration:none; border-radius:10px; font-weight:700;">
                Postavi novu lozinku
              </a>
            </div>
            <p style="margin:0 0 6px; color:#64748b; font-size:14px;">Ako dugme ne radi, otvori ovaj link u pregledaču:</p>
            <p style="margin:0; word-break:break-all; color:#0f172a; font-size:13px;">%s</p>
            <hr style="border:none; border-top:1px solid #e6e8ef; margin:20px 0;">
            <p style="margin:0; color:#94a3b8; font-size:12px;">Link važi 30 minuta. Ako nisi tražio reset, ignoriši ovaj mejl.</p>
          </div>
        </div>
        """.formatted(username, link, link);
    }

    /**
     * Resetuje lozinku korisnika na osnovu prosleđenog tokena.
     * Token mora biti validan, neiskorišćen i ne sme biti istekao.
     * Nakon uspešnog resetovanja, token se označava kao iskorišćen.
     *
     * @param token    string vrednost tokena za resetovanje lozinke
     * @param password nova lozinka u čistom tekstu koja će biti enkodovana
     * @throws RuntimeException ako je token neispravan, iskorišćen ili istekao
     */
    public void resetPassword(String token, String password) {
        PasswordResetToken t = resetTokens.find(token);
        if (t == null || t.isUsed() || t.isExpired()) {
            throw new RuntimeException("Neispravan ili istekao token.");
        }
        Vlasnik u = t.getVlasnik();
        u.setLozinka(encoder.encode(password));
        users.save(u);

        t.setUsed(true);
        resetTokens.save(t);
    }
}