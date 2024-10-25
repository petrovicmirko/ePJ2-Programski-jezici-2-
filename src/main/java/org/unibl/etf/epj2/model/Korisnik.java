package org.unibl.etf.epj2.model;

import java.util.Random;

/**
 * Klasa koja predstavlja korisnika sistema za iznajmljivanje vozila.
 * Ova klasa sadrži informacije o korisniku, kao što su ime, identifikacioni dokument,
 * broj vozačke dozvole i da li je korisnik domaći ili strani.
 *
 * <p>Korisnik može biti domaći ili strani, a njegov identifikacioni dokument i broj vozačke dozvole
 * se generišu nasumično prilikom kreiranja objekta klase.</p>
 */
public class Korisnik {
    private String ime;
    private String identifikacioniDokument;
    private String brojVozacke;
    private boolean jeDomaciKorisnik;

    private Random random = new Random();

    /**
     * Konstruktor koji inicijalizuje korisnika sa zadatim imenom.
     * Nasumično dodeljuje status domaćeg ili stranog korisnika, kao i generiše
     * identifikacioni dokument i broj vozačke dozvole.
     *
     * @param ime Ime korisnika
     */
    public Korisnik(String ime) {
        this.ime = ime;
        jeDomaciKorisnik = random.nextBoolean();

        if(jeDomaciKorisnik) {
            identifikacioniDokument = "licna karta: " + (random.nextInt(900000) + 100000);
        } else {
            identifikacioniDokument = "pasos: " + (random.nextInt(90000) + 10000);
        }

        brojVozacke = "vozacka dozvola: " + (random.nextInt(9000) + 1000);
    }

    public String getIme() {
        return ime;
    }

    public void setIme(String ime) {
        this.ime = ime;
    }

    public boolean isJeDomaciKorisnik() {
        return jeDomaciKorisnik;
    }

    public void setJeDomaciKorisnik(boolean jeDomaciKorisnik) {
        this.jeDomaciKorisnik = jeDomaciKorisnik;
    }

    public String getBrojVozacke() {
        return brojVozacke;
    }

    public void setBrojVozacke(String brojVozacke) {
        this.brojVozacke = brojVozacke;
    }

    public String getIdentifikacioniDokument() {
        return identifikacioniDokument;
    }

    public void setIdentifikacioniDokument(String identifikacioniDokument) {
        this.identifikacioniDokument = identifikacioniDokument;
    }
}
