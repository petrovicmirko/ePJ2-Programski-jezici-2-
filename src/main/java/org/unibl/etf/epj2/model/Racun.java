package org.unibl.etf.epj2.model;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

/**
 * Klasa koja predstavlja račun za iznajmljivanje vozila.
 * Ova klasa sadrži sve informacije vezane za finansijski dio iznajmljivanja,
 * uključujući ukupno za plaćanje, osnovnu cenu, iznos, popuste i promocije,
 * kao i podatke o kvarovima i vozilu. Takođe, upravlja brojačima za broj
 * vožnji u širem i užem delu grada na osnovu datuma.
 */
public class Racun {
    private double ukupnoZaPlacanje = 0.00;
    private double osnovnaCijena = 0.00;
    private double iznos = 0.00;
    private double vrijednostPopusta = 0.00;
    private double vrijednostPromocije = 0.00;
    private boolean desioSeKvar = false;
    private Vozilo vozilo;

    private LocalDate datum;

    public static int brojacSiriDioGrada = 0;
    public static int brojacUziDioGrada = 0;
    public static int brojacRacuna = 0;

    public static Map<LocalDate, Integer> brojacSiriDioGradaPoDatumima = new HashMap<>();
    public static Map<LocalDate, Integer> brojacUziDioGradaPoDatumima = new HashMap<>();

    public Racun() {
    }

    public Racun(double ukupnoZaPlacanje, double osnovnaCijena, double iznos, double vrijednostPopusta, double vrijednostPromocije) {
        this.ukupnoZaPlacanje = ukupnoZaPlacanje;
        this.osnovnaCijena = osnovnaCijena;
        this.iznos = iznos;
        this.vrijednostPopusta = vrijednostPopusta;
        this.vrijednostPromocije = vrijednostPromocije;
    }

    public double getUkupnoZaPlacanje() {
        return ukupnoZaPlacanje;
    }

    public void setUkupnoZaPlacanje(double ukupnoZaPlacanje) {
        this.ukupnoZaPlacanje = ukupnoZaPlacanje;
    }

    public double getOsnovnaCijena() {
        return osnovnaCijena;
    }

    public void setOsnovnaCijena(double osnovnaCijena) {
        this.osnovnaCijena = osnovnaCijena;
    }

    public double getIznos() {
        return iznos;
    }

    public void setIznos(double iznos) {
        this.iznos = iznos;
    }

    public double getVrijednostPopusta() {
        return vrijednostPopusta;
    }

    public void setVrijednostPopusta(double vrijednostPopusta) {
        this.vrijednostPopusta = vrijednostPopusta;
    }

    public double getVrijednostPromocije() {
        return vrijednostPromocije;
    }

    public void setVrijednostPromocije(double vrijednostPromocije) {
        this.vrijednostPromocije = vrijednostPromocije;
    }

    public boolean isDesioSeKvar() {
        return desioSeKvar;
    }

    public void setDesioSeKvar(boolean desioSeKvar) {
        this.desioSeKvar = desioSeKvar;
    }

    public static int getBrojacRacuna() {
        return brojacRacuna;
    }

    public static void setBrojacRacuna(int brojacRacuna) {
        Racun.brojacRacuna = brojacRacuna;
    }

    public Vozilo getVozilo() {
        return vozilo;
    }

    public void setVozilo(Vozilo vozilo) {
        this.vozilo = vozilo;
    }

    public LocalDate getDatum() {
        return datum;
    }

    public void setDatum(LocalDate datum) {
        this.datum = datum;
    }

    public static Map<LocalDate, Integer> getBrojacSiriDioGradaPoDatumima() {
        return brojacSiriDioGradaPoDatumima;
    }

    public static void setBrojacSiriDioGradaPoDatumima(Map<LocalDate, Integer> brojacSiriDioGradaPoDatumima) {
        Racun.brojacSiriDioGradaPoDatumima = brojacSiriDioGradaPoDatumima;
    }

    public static Map<LocalDate, Integer> getBrojacUziDioGradaPoDatumima() {
        return brojacUziDioGradaPoDatumima;
    }

    public static void setBrojacUziDioGradaPoDatumima(Map<LocalDate, Integer> brojacUziDioGradaPoDatumima) {
        Racun.brojacUziDioGradaPoDatumima = brojacUziDioGradaPoDatumima;
    }

    public static int getBrojVoznjiUSiremDeluGrada(LocalDate datum) {
        return brojacSiriDioGradaPoDatumima.getOrDefault(datum, 0);
    }

    public static int getBrojVoznjiUUzemDeluGrada(LocalDate datum) {
        return brojacUziDioGradaPoDatumima.getOrDefault(datum, 0);
    }

    public static void azurirajBrojacPoDatumima(LocalDate datum, boolean presloUSiriDioGrada) {
        if (presloUSiriDioGrada) {
            brojacSiriDioGradaPoDatumima.put(datum, brojacSiriDioGradaPoDatumima.getOrDefault(datum, 0) + 1);
        } else {
            brojacUziDioGradaPoDatumima.put(datum, brojacUziDioGradaPoDatumima.getOrDefault(datum, 0) + 1);
        }
    }
}
