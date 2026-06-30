/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.njt_mavenproject.security;

import com.mycompany.njt_mavenproject.entity.impl.Vlasnik;
import com.mycompany.njt_mavenproject.repository.impl.VlasnikRepository;
import java.util.List;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Implementacija Spring Security servisa za učitavanje korisničkih podataka.
 * Koristi se tokom autentifikacije za pronalaženje korisnika po korisničkom imenu
 * i mapiranje uloge iz baze podataka u Spring Security autoritet.
 *
 * @author Bojana
 */
@Service
public class AppUserDetailsService implements UserDetailsService {

    private final VlasnikRepository users;

    /**
     * Konstruktor koji injektuje repozitorijum vlasnika.
     *
     * @param users repozitorijum za pristup podacima o vlasnicima
     */
    public AppUserDetailsService(VlasnikRepository users) {
        this.users = users;
    }

    /**
     * Učitava korisničke podatke na osnovu korisničkog imena.
     * Pronalazi vlasnika u bazi podataka i kreira Spring Security
     * {@link UserDetails} objekat sa odgovarajućom ulogom.
     *
     * @param username korisničko ime korisnika koji se autentifikuje
     * @return {@link UserDetails} objekat sa podacima o korisniku i njegovim pravima
     * @throws UsernameNotFoundException ako korisnik sa datim korisničkim imenom ne postoji
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Vlasnik u = users.findByUsername(username);
        if (u == null) throw new UsernameNotFoundException("Not found");
        logger.info("ULOGA IZ BAZE: {}", u.getUloga());
        logger.info("AUTHORITY: ROLE_{}", u.getUloga().name());
        return new org.springframework.security.core.userdetails.User(
                u.getUsername(),
                u.getLozinka(),
                u.isEnabled(),
                true, // accountNonExpired
                true, // credentialsNonExpired
                true, // accountNonLocked
                List.of(new SimpleGrantedAuthority("ROLE_" + u.getUloga().name()))
        );
    }
}