package org.unibl.etf.epj2.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Klasa koja predstavlja kvar na vozilu u sistemu za iznajmljivanje vozila.
 * Ova klasa sadrži informacije o vozilu koje je doživelo kvar, vrijeme i datum kada je kvar nastao,
 * opis kvara i jedinstveni identifikator kvara.
 */
public class Kvar {

    private String vozilo;
    private String id;
    private String vrijemeIDatum;
    private String opis;
    public static int BROJAC_KVAROVA = 0;

    private DateTimeFormatter formatter;

    /**
     * Konstruktor koji inicijalizuje kvar sa datim vozilom i vremenom i datumom kvara.
     * Povećava brojač kvarova i formatira vreme i datum kvara prema zadatom obrascu.
     *
     * @param vozilo Vozilo koje je doživjelo kvar
     * @param vrijemeIDatum Vrijeme i datum kada je kvar nastao
     */
    public Kvar(Vozilo vozilo, LocalDateTime vrijemeIDatum) {
        BROJAC_KVAROVA++;
        formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy. HH.mm");

        this.vozilo = dodajVrstuVozila(vozilo);
        this.id = vozilo.getId();
        this.vrijemeIDatum = vrijemeIDatum.format(formatter);
        this.opis = "opis" + BROJAC_KVAROVA;
    }

    public String getVozilo() {
        return vozilo;
    }

    public void setVozilo(String vozilo) {
        this.vozilo = vozilo;
    }

    public String getOpis() {
        return opis;
    }

    public void setOpis(String opis) {
        this.opis = opis;
    }

    public String getVrijemeIDatum() {
        return vrijemeIDatum;
    }

    public void setVrijemeIDatum(String vrijemeIDatum) {
        this.vrijemeIDatum = vrijemeIDatum;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    /**
     * Privatna metoda koja određuje vrstu vozila na osnovu njegove klase.
     *
     * @param vozilo Vozilo koje se koristi za određivanje vrste
     * @return Vrsta vozila kao String
     */
    private String dodajVrstuVozila(Vozilo vozilo) {
        if(vozilo instanceof Automobil) {
            return "Automobil";
        } else if(vozilo instanceof Bicikl) {
            return "Bicikl";
        } else {
            return "Trotinet";
        }
    }
}
