package org.unibl.etf.epj2.model;

import javafx.scene.paint.Color;

import java.util.HashMap;
import java.util.Map;

/**
 * Klasa Bicikl koja predstavlja entitet bicikla sa specifičnim atributima kao što je domet i prihodi bicikala.
 */
public class Bicikl extends Vozilo {
    private int domet;

    public static Map<Bicikl, Double> prihodiBicikla = new HashMap<>();

    public Bicikl(String id, String proizvodjac, String model, double cijenaNabavke, int nivoBaterije, int domet) {
        super(id, proizvodjac, model, cijenaNabavke, nivoBaterije);
        this.domet = domet;
        prihodiBicikla.put(this, 0.0);
    }

    public int getDomet() {
        return domet;
    }

    public void setDomet(int domet) {
        this.domet = domet;
    }

    @Override
    public String toString() {
        return "Bicikl{" + super.toString() + ", domet=" + domet + '}';
    }
}
