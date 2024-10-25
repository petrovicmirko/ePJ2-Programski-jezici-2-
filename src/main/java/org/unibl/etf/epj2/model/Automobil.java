package org.unibl.etf.epj2.model;

import javafx.scene.paint.Color;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Klasa Automobil koja predstavlja entitet automobila sa specifičnim atributima kao što su datum nabavke,
 * opis, broj putnika i prihodi automobila.
 */
public class Automobil extends Vozilo {
    private String datumNabavke;
    private String opis;
    private int brojPutnika;

    public static Map<Automobil, Double> prihodiAutomobila = new HashMap<>();

    private Random random = new Random();

    public Automobil(String id, String proizvodjac, String model, double cijenaNabavke, int nivoBaterije, String datumNabavke, String opis) {
        super(id, proizvodjac, model, cijenaNabavke, nivoBaterije);
        this.datumNabavke = datumNabavke;
        this.brojPutnika = random.nextInt(5) + 1;
        this.opis = opis;
        prihodiAutomobila.put(this, 0.0);
    }

    public String getDatumNabavke() {
        return datumNabavke;
    }

    public void setDatumNabavke(String datumNabavke) {
        this.datumNabavke = datumNabavke;
    }

    public String getOpis() {
        return opis;
    }

    public int getBrojPutnika() {
        return brojPutnika;
    }

    public void setOpis(String opis) {
        this.opis = opis;
    }

    public void setBrojPutnika(int brojPutnika) {
        this.brojPutnika = brojPutnika;
    }



    @Override
    public String toString() {
        return "Automobil{" + super.toString() +
                "datumNabavke=" + datumNabavke +
                ", opis='" + opis + '\'' +
                ", brojPutnika=" + brojPutnika +
                '}';
    }
}
