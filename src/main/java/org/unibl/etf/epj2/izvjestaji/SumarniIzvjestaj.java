package org.unibl.etf.epj2.izvjestaji;

import org.unibl.etf.epj2.model.Automobil;
import org.unibl.etf.epj2.model.Bicikl;
import org.unibl.etf.epj2.model.Racun;
import org.unibl.etf.epj2.model.Vozilo;
import org.unibl.etf.epj2.simulacija.Simulator;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Klasa koja generiše sumarni izvještaj na osnovu liste računa.
 * Izračunava ukupni prihod, popust, promocije, kvarove, troškove, održavanje i porez.
 */
public class SumarniIzvjestaj {
    private double ukupanPrihod = 0.00;
    private double ukupanPopust = 0.00;
    private double ukupnoPromocije = 0.00;
    private int ukupanIznosVoznjiUUzemDijelu = 0;
    private int ukupanIznosVoznjiUSiremDijelu = 0;
    private double ukupnoOdrzavanje = 0.00;
    private double ukupnoKvarovi = 0.00;
    private double ukupniTroskovi = 0.00;
    private double ukupanPorez = 0.00;
    private List<Racun> racuni;

    /**
     * Konstruktor klase SumarniIzvjestaj.
     *
     * @param racuni Lista računa koja se koristi za izračunavanje sumarnog izvještaja.
     */
    public SumarniIzvjestaj(List<Racun> racuni) {
        this.racuni = racuni;
        izracunajPrihodPopustPromocijeKvarove();
        izracunajOdrzavanjeTroskove();
        izracunajPorez();
        ukupanIznosVoznjiUUzemDijelu = Racun.brojacUziDioGrada;
        ukupanIznosVoznjiUSiremDijelu = Racun.brojacSiriDioGrada;

        formatiraj();
    }

    /**
     * Metoda koja formatira sve izračunate vrijednosti na tri decimalna mjesta.
     */
    private void formatiraj() {
        ukupanPrihod = Math.round(ukupanPrihod * 1000.0) / 1000.0;
        ukupanPopust = Math.round(ukupanPopust * 1000.0) / 1000.0;
        ukupnoPromocije = Math.round(ukupnoPromocije * 1000.0) / 1000.0;
        ukupnoOdrzavanje = Math.round(ukupnoOdrzavanje * 1000.0) / 1000.0;
        ukupnoKvarovi = Math.round(ukupnoKvarovi * 1000.0) / 1000.0;
        ukupniTroskovi = Math.round(ukupniTroskovi * 1000.0) / 1000.0;
        ukupanPorez = Math.round(ukupanPorez * 1000.0) / 1000.0;
    }

    public double getUkupanPrihod() {
        return ukupanPrihod;
    }

    public double getUkupanPorez() {
        return ukupanPorez;
    }

    public double getUkupniTroskovi() {
        return ukupniTroskovi;
    }

    public double getUkupnoKvarovi() {
        return ukupnoKvarovi;
    }

    public double getUkupnoOdrzavanje() {
        return ukupnoOdrzavanje;
    }

    public int getUkupanIznosVoznjiUSiremDijelu() {
        return ukupanIznosVoznjiUSiremDijelu;
    }

    public int getUkupanIznosVoznjiUUzemDijelu() {
        return ukupanIznosVoznjiUUzemDijelu;
    }

    public double getUkupnoPromocije() {
        return ukupnoPromocije;
    }

    public double getUkupanPopust() {
        return ukupanPopust;
    }

    /**
     * Metoda koja izračunava ukupni prihod, popust, promocije i iznos kvarova.
     */
    private void izracunajPrihodPopustPromocijeKvarove() {
        for(Racun racun : racuni) {
            Vozilo vozilo = racun.getVozilo();

            ukupanPrihod += racun.getUkupnoZaPlacanje();
            ukupanPopust += racun.getVrijednostPopusta();
            ukupnoPromocije += racun.getVrijednostPromocije();
            if(racun.isDesioSeKvar()) {
                if(vozilo instanceof Automobil) {
                    ukupnoKvarovi += 0.07 * vozilo.getCijenaNabavke();
                } else if(vozilo instanceof Bicikl) {
                    ukupnoKvarovi += 0.04 * vozilo.getCijenaNabavke();
                } else {
                    ukupnoKvarovi += 0.02 * vozilo.getCijenaNabavke();
                }
            }
        }
    }

    /**
     * Metoda koja izračunava troškove održavanja i ukupne troškove.
     */
    private void izracunajOdrzavanjeTroskove() {
        ukupnoOdrzavanje = 0.2 * ukupanPrihod;
        ukupniTroskovi = 0.2 * ukupanPrihod;
    }

    /**
     * Metoda koja izračunava ukupni porez na osnovu prihoda, održavanja, kvarova i troškova.
     */
    private void izracunajPorez() {
        ukupanPorez = Math.abs((ukupanPrihod - ukupnoOdrzavanje - ukupnoKvarovi - ukupniTroskovi) * 0.1);
    }

    /**
     * @return String reprezentacija sumarnog izvještaja sa svim izračunatim vrijednostima.
     */
    @Override
    public String toString() {
        return String.format(
                "Sumarni izvjestaj:\n" +
                        "============================================\n" +
                        "ukupan prihod = %.2f\n" +
                        "ukupan popust = %.2f\n" +
                        "ukupno promocije = %.2f\n" +
                        "ukupan iznos voznji u uzem dijelu = %d\n" +
                        "ukupan iznos voznji u sirem dijelu = %d\n" +
                        "ukupno odrzavanje = %.2f\n" +
                        "ukupan iznos kvarova = %.2f\n" +
                        "ukupni troskovi = %.2f\n" +
                        "ukupan porez = %.2f",
                ukupanPrihod,
                ukupanPopust,
                ukupnoPromocije,
                ukupanIznosVoznjiUUzemDijelu,
                ukupanIznosVoznjiUSiremDijelu,
                ukupnoOdrzavanje,
                ukupnoKvarovi,
                ukupniTroskovi,
                ukupanPorez
        );
    }
}
