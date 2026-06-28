package com.mycompany.njt_mavenproject.config;

import com.mycompany.njt_mavenproject.entity.impl.Uloga;
import com.mycompany.njt_mavenproject.entity.impl.Vlasnik;
import com.mycompany.njt_mavenproject.repository.impl.VlasnikRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Konfiguraciona klasa koja pri pokretanju aplikacije kreira podrazumevanog ADMIN korisnika
 * ukoliko on već ne postoji u bazi podataka.
 *
 * @author Bojana
 */
@Configuration
public class AdminSeedConfig {

    /**
     * Kreira podrazumevanog ADMIN korisnika pri pokretanju aplikacije.
     * Ako korisnik sa zadatim username-om ili email-om već postoji, preskače kreiranje.
     *
     * @param vlasnikRepo repozitorijum za pristup podacima o vlasnicima
     * @param encoder     enkoder za hešovanje lozinke
     * @return CommandLineRunner koji se izvršava pri pokretanju aplikacije
     */
    @Bean
    CommandLineRunner seedAdmin(VlasnikRepository vlasnikRepo, PasswordEncoder encoder) {
        return args -> {
            final String adminUsername = "admin";
            final String adminEmail    = "admin@lokal.host";
            final String adminPass     = "admin123";

            if (vlasnikRepo.existsByUsername(adminUsername) || vlasnikRepo.existsByEmail(adminEmail)) {
                return;
            }

            Vlasnik a = new Vlasnik();
            a.setUsername(adminUsername);
            a.setEmail(adminEmail);
            a.setIme("Admin");
            a.setPrezime("Korisnik");
            a.setLozinka(encoder.encode(adminPass));
            a.setUloga(Uloga.ADMIN);
            a.setEnabled(true);

            vlasnikRepo.save(a);
            System.out.println("✅ Kreiran podrazumevani ADMIN korisnik: " + adminUsername + " / " + adminEmail);
        };
    }
}