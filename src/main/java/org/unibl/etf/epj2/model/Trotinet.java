package org.unibl.etf.epj2.model;

import javafx.scene.paint.Color;

import java.util.HashMap;
import java.util.Map;

/**
 * Klasa koja predstavlja entitet trotineta sa specifičnim atributima kao što je maksimalna brzina
 * i prihodi trotineta.
 */
public class Trotinet extends Vozilo {
    private int maxBrzina;

    public static Map<Trotinet, Double> prihodiTrotineta = new HashMap<>();

    public Trotinet(String id, String proizvodjac, String model, double cijenaNabavke, int nivoBaterije, int maxBrzina) {
        super(id, proizvodjac, model, cijenaNabavke, nivoBaterije);
        this.maxBrzina = maxBrzina;
        prihodiTrotineta.put(this, 0.0);
    }

    public int getMaxBrzina() {
        return maxBrzina;
    }

    public void setMaxBrzina(int maxBrzina) {
        this.maxBrzina = maxBrzina;
    }

    @Override
    public String toString() {
        return "Trotinet{" + super.toString() + ", maxBrzina=" + maxBrzina + '}';
    }
}
