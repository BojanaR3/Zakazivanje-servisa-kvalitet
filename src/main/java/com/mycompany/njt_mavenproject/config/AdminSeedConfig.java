/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.njt_mavenproject.config;
import com.mycompany.njt_mavenproject.entity.impl.Uloga;
import com.mycompany.njt_mavenproject.entity.impl.Vlasnik;
import com.mycompany.njt_mavenproject.repository.impl.VlasnikRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
/**
 *
 * @author Korisnik
 */
@Configuration

public class AdminSeedConfig {
    
    @Bean
    CommandLineRunner seedAdmin(VlasnikRepository vlasnikRepo, PasswordEncoder encoder) {
        return args -> {
            // promeni po želji
            final String adminUsername = "admin";
            final String adminEmail    = "admin@lokal.host";
            final String adminPass     = "admin123"; // biće bcrypt-ovan

            // ako već postoji (po username ili email), preskoči
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
