/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.mycompany.njt_mavenproject.repository;

import java.util.List;

/**
 * Generički interfejs koji definiše osnovne CRUD operacije
 * za sve repozitorijume u aplikaciji.
 *
 * @param <E>  tip entiteta sa kojim repozitorijum radi
 * @param <ID> tip identifikatora entiteta
 * @author Bojana
 */
public interface MyAppRepository<E, ID> {

    /**
     * Vraća listu svih entiteta iz baze podataka.
     *
     * @return lista svih entiteta
     */
    List<E> findAll();

    /**
     * Pronalazi entitet na osnovu prosleđenog identifikatora.
     *
     * @param id identifikator entiteta koji se traži
     * @return pronađeni entitet
     * @throws Exception ako entitet sa datim ID-jem ne postoji
     */
    E findById(ID id) throws Exception;

    /**
     * Čuva novi entitet ili ažurira postojeći u bazi podataka.
     *
     * @param entity entitet koji se čuva
     */
    void save(E entity);

    /**
     * Briše entitet sa datim identifikatorom iz baze podataka.
     *
     * @param id identifikator entiteta koji se briše
     */
    void deleteById(ID id);
}