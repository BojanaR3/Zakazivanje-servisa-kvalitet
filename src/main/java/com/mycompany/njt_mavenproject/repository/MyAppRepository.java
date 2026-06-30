/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.mycompany.njt_mavenproject.repository;
import java.util.List;

import com.mycompany.njt_mavenproject.exception.EntityNotFoundException;
/**
 * Generički interfejs koji definiše osnovne CRUD operacije
 * za sve repozitorijume u aplikaciji.
 *
 * @param <T>  tip entiteta sa kojim repozitorijum radi
 * @param <ID> tip identifikatora entiteta
 * @author Bojana
 */
public interface MyAppRepository<T, ID> {
    /**
     * Vraća listu svih entiteta iz baze podataka.
     *
     * @return lista svih entiteta
     */
    List<T> findAll();
    /**
     * Pronalazi entitet na osnovu prosleđenog identifikatora.
     *
     * @param id identifikator entiteta koji se traži
     * @return pronađeni entitet
     * @throws EntityNotFoundException ako entitet sa datim ID-jem ne postoji
     */
    T findById(ID id) throws EntityNotFoundException;
    /**
     * Čuva novi entitet ili ažurira postojeći u bazi podataka.
     *
     * @param entity entitet koji se čuva
     */
    void save(T entity);
    /**
     * Briše entitet sa datim identifikatorom iz baze podataka.
     *
     * @param id identifikator entiteta koji se briše
     */
    void deleteById(ID id);
}