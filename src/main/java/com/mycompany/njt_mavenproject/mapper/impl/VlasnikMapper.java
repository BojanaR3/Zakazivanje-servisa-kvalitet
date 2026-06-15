/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.njt_mavenproject.mapper.impl;

import com.mycompany.njt_mavenproject.dto.impl.VlasnikDto;
import com.mycompany.njt_mavenproject.entity.impl.Uloga;
import com.mycompany.njt_mavenproject.entity.impl.Vlasnik;
import com.mycompany.njt_mavenproject.mapper.DtoEntityMapper;
import org.springframework.stereotype.Component;

/**
 *
 * @author Korisnik
 */
@Component

public class VlasnikMapper implements DtoEntityMapper<VlasnikDto, Vlasnik>{
    
    @Override
    public VlasnikDto toDto(Vlasnik e) {
        if (e == null) return null;
        return new VlasnikDto(e.getId(), e.getIme(), e.getPrezime(), e.getUsername(), e.getEmail(), e.getUloga());
        // password NIKAD ne punimo u DTO iz entiteta
    }

    @Override
    public Vlasnik toEntity(VlasnikDto t) {
        if (t == null) return null;
        Vlasnik u = new Vlasnik();
        u.setId(t.getId());
        u.setIme(t.getIme());
        u.setPrezime(t.getPrezime());
        u.setUsername(t.getUsername());
        u.setEmail(t.getEmail());
        // passwordHash se postavlja u servisu (posle encoder-a), ne ovde
        u.setUloga(t.getUloga());
        return u;
    }
    
}
