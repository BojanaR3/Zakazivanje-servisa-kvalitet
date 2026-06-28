/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.njt_mavenproject.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;

/**
 * Filter za JWT autentifikaciju koji se izvršava jednom po zahtevu.
 * Čita JWT token iz Authorization zaglavlja, validira ga i postavlja
 * autentifikaciju u Spring Security kontekst ako je token ispravan.
 *
 * @author Bojana
 */
@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtService jwt;
    private final AppUserDetailsService uds;

    /**
     * Konstruktor koji injektuje JWT servis i servis za učitavanje korisnika.
     *
     * @param jwt servis za rad sa JWT tokenima
     * @param uds servis za učitavanje korisničkih podataka
     */
    public JwtAuthFilter(JwtService jwt, AppUserDetailsService uds) {
        this.jwt = jwt;
        this.uds = uds;
    }

    /**
     * Obrađuje svaki HTTP zahtev i proverava prisustvo validnog JWT tokena.
     * Ako zahtev sadrži ispravno Authorization zaglavlje sa Bearer tokenom,
     * korisnik se autentifikuje i autentifikacija se postavlja u Security kontekst.
     * Ako token nedostaje ili je neispravan, zahtev se prosleđuje dalje bez autentifikacije.
     *
     * @param req    dolazni HTTP zahtev
     * @param res    HTTP odgovor
     * @param chain  lanac filtera koji se nastavlja
     * @throws ServletException u slučaju greške u obradi zahteva
     * @throws IOException      u slučaju ulazno-izlazne greške
     */
    @Override
    protected void doFilterInternal(HttpServletRequest req,
                                    HttpServletResponse res,
                                    FilterChain chain) throws ServletException, IOException {
        String auth = req.getHeader("Authorization");

        System.out.println("=== JWT FILTER === AUTH HEADER: " + auth);
        if (auth == null || !auth.startsWith("Bearer ")) {
            chain.doFilter(req, res);
            return;
        }

        String token = auth.substring(7);
        String username = null;
        try { username = jwt.extractUsername(token); } catch (Exception ignored) {}

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails ud = uds.loadUserByUsername(username);
            if (jwt.isValid(token, ud)) {
                var at = new UsernamePasswordAuthenticationToken(ud, null, ud.getAuthorities());
                at.setDetails(new WebAuthenticationDetailsSource().buildDetails(req));
                SecurityContextHolder.getContext().setAuthentication(at);
            }
        }
        chain.doFilter(req, res);
    }
}