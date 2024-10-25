package org.unibl.etf.epj2.izvjestaji;

import org.unibl.etf.epj2.model.Automobil;
import org.unibl.etf.epj2.model.Bicikl;
import org.unibl.etf.epj2.model.Racun;
import org.unibl.etf.epj2.model.Vozilo;
import org.unibl.etf.epj2.simulacija.Simulator;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Klasa koja predstavlja izveštaj o finansijskim podacima za određeni datum,
 * uključujući prihod, popuste, promocije, troškove održavanja, i kvarove po vrstama vozila.
 *
 * Ova klasa omogućava generisanje, analizu i prikazivanje dnevnih izveštaja na osnovu liste računa.
 */
public class DnevniIzvjestaj {
    private double ukupanPrihod = 0.00;
    private double ukupanPopust = 0.00;
    private double ukupnoPromocije = 0.00;
    private int ukupanIznosVoznjiUUzemDijelu = 0;
    private int ukupanIznosVoznjiUSiremDijelu = 0;
    private double ukupnoOdrzavanje = 0.00;
    private double ukupnoKvarovi = 0.00;
    private String datum;
    private static List<Racun> racuni = Simulator.getRacuni();
    private static Map<LocalDate, List<Racun>> racuniPoDatumima;
    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy.");

    /**
     * Konstruktor za klasu DnevniIzvjestaj.
     *
     * @param datum Datum za koji se generiše izvještaj u formatu "dd.MM.yyyy.".
     * @param racuni Lista računa koji pripadaju datumu izvještaja.
     */
    public DnevniIzvjestaj(String datum, List<Racun> racuni) {
        this.datum = datum;
        this.racuni = racuni;
        izracunajPrihodPopustPromocijeKvarove();
        izracunajOdrzavanje();

        ukupanIznosVoznjiUSiremDijelu = Racun.getBrojVoznjiUSiremDeluGrada(LocalDate.parse(datum, formatter));
        ukupanIznosVoznjiUUzemDijelu = Racun.getBrojVoznjiUUzemDeluGrada(LocalDate.parse(datum, formatter));

        formatiraj();
    }

    /**
     * Metoda za formatiranje numeričkih vrednosti na tri decimale.
     */
    public void formatiraj() {
        ukupanPrihod = Math.round(ukupanPrihod * 1000.0) / 1000.0;
        ukupanPopust = Math.round(ukupanPopust * 1000.0) / 1000.0;
        ukupnoPromocije = Math.round(ukupnoPromocije * 1000.0) / 1000.0;
        ukupnoOdrzavanje = Math.round(ukupnoOdrzavanje * 1000.0) / 1000.0;
        ukupnoKvarovi = Math.round(ukupnoKvarovi * 1000.0) / 1000.0;
    }

    public double getUkupanPrihod() {
        return ukupanPrihod;
    }

    public double getUkupanPopust() {
        return ukupanPopust;
    }

    public int getUkupanIznosVoznjiUUzemDijelu() {
        return ukupanIznosVoznjiUUzemDijelu;
    }

    public double getUkupnoPromocije() {
        return ukupnoPromocije;
    }

    public int getUkupanIznosVoznjiUSiremDijelu() {
        return ukupanIznosVoznjiUSiremDijelu;
    }

    public double getUkupnoOdrzavanje() {
        return ukupnoOdrzavanje;
    }

    public double getUkupnoKvarovi() {
        return ukupnoKvarovi;
    }

    public String getDatum() {
        return datum;
    }

    /**
     * Metoda za izračunavanje ukupnog prihoda, popusta, promocija i kvarova na osnovu liste računa.
     */
    public void izracunajPrihodPopustPromocijeKvarove() {
        for (Racun racun : racuni) {
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
     * Metoda za izračunavanje troškova održavanja kao procenat od ukupnog prihoda.
     */
    private void izracunajOdrzavanje() {
        ukupnoOdrzavanje = 0.2 * ukupanPrihod;
    }

    public void generisi() {
        racuniPoDatumima = racuni.stream()
                .collect(Collectors.groupingBy(racun -> racun.getDatum()));

        for (Map.Entry<LocalDate, List<Racun>> entry : racuniPoDatumima.entrySet()) {
            LocalDate datum = entry.getKey();
            List<Racun> racuniZaDatum = entry.getValue();

            String datumString = datum.format(formatter);
            DnevniIzvjestaj dnevniIzvjestaj = new DnevniIzvjestaj(datumString, racuniZaDatum);
            System.out.println(dnevniIzvjestaj);
        }
    }

    /**
     * Statička metoda za generisanje liste dnevnih izvještaja na osnovu liste računa.
     *
     * @param racuni Lista računa koji se koriste za generisanje dnevnih izvještaja.
     * @return Lista dnevnih izvještaja grupisanih po datumu.
     */
    public static List<DnevniIzvjestaj> generisiListu(List<Racun> racuni) {
        List<DnevniIzvjestaj> dnevniIzvjestaji = new ArrayList<>();

        racuniPoDatumima = racuni.stream()
                .collect(Collectors.groupingBy(racun -> racun.getDatum()));

        for (Map.Entry<LocalDate, List<Racun>> entry : racuniPoDatumima.entrySet()) {
            LocalDate datum = entry.getKey();
            List<Racun> racuniZaDatum = entry.getValue();

            String datumString = datum.format(formatter);

            dnevniIzvjestaji.add(new DnevniIzvjestaj(datumString, racuniZaDatum));
        }

        return dnevniIzvjestaji;
    }

    /**
     * Metoda koja vraća string reprezentaciju objekta DnevniIzvjestaj.
     *
     * @return String koji predstavlja detaljan izvještaj za određeni datum.
     */
    @Override
    public String toString() {
        return String.format("Dnevni izvjestaj:\n" +
                        "============================================\n" +
                        "Datum: %s\n" +
                        "Ukupan prihod = %.2f\n" +
                        "Ukupan popust = %.2f\n" +
                        "Ukupno promocije = %.2f\n" +
                        "Ukupan iznos voznji u uzem dijelu = %d\n" +
                        "Ukupan iznos voznji u sirem dijelu = %d\n" +
                        "Ukupno odrzavanje = %.2f\n" +
                        "Ukupno kvarovi = %.2f\n",
                datum, ukupanPrihod, ukupanPopust, ukupnoPromocije, ukupanIznosVoznjiUUzemDijelu, ukupanIznosVoznjiUSiremDijelu, ukupnoOdrzavanje, ukupnoKvarovi);
    }
}
