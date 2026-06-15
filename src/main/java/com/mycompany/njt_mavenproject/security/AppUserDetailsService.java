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
 *
 * @author Korisnik
 */
@Service

public class AppUserDetailsService implements UserDetailsService{
    
    private final VlasnikRepository users;

    public AppUserDetailsService(VlasnikRepository users) {
        this.users = users;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Vlasnik u = users.findByUsername(username);
        if (u == null) throw new UsernameNotFoundException("Not found");
        System.out.println("ULOGA IZ BAZE: " + u.getUloga());
        System.out.println("AUTHORITY: ROLE_" + u.getUloga().name());
        return new org.springframework.security.core.userdetails.User(
                u.getUsername(),
                u.getLozinka(),
                u.isEnabled(),
                true, // accountNonExpired
                true,// credentialsNonExpired
                true,// accountNonLocked
                List.of(new SimpleGrantedAuthority("ROLE_" + u.getUloga().name()))
        );
    }
    
}
