package com.mycompany.njt_mavenproject.mapper;

/**
 * Generički interfejs za mapiranje između DTO objekata i entiteta.
 * Definiše metode za konverziju entiteta u DTO i DTO objekata u entitete.
 *
 * @param <T> tip DTO objekta
 * @param <E> tip entiteta
 * 
 * @author Bojana
 */
public interface DtoEntityMapper<T,E> {

    /**
     * Konvertuje entitet u DTO objekat.
     *
     * @param e entitet koji se konvertuje
     * @return DTO objekat dobijen konverzijom entiteta
     */
    T toDto(E e);

    /**
     * Konvertuje DTO objekat u entitet.
     *
     * @param t DTO objekat koji se konvertuje
     * @return entitet dobijen konverzijom DTO objekta
     */
    E toEntity(T t);

}

