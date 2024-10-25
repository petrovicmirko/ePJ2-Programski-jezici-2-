package org.unibl.etf.epj2.model;

import java.io.Serializable;

/**
 * Klasa koja predstavlja osnovne karakteristike vozila.
 * Sadrži atribute za identifikator vozila, proizvođača, model,
 * cijenu nabavke i nivo baterije. Takođe pruža metode za punjenje i pražnjenje
 * baterije.
 */
public class Vozilo implements Serializable {
    private String id;
    private String proizvodjac;
    private String model;
    private double cijenaNabavke;
    private int nivoBaterije;

    public Vozilo(String id, String proizvodjac, String model, double cijenaNabavke, int nivoBaterije) {
        this.id = id;
        this.proizvodjac = proizvodjac;
        this.cijenaNabavke = cijenaNabavke;
        this.model = model;
        this.nivoBaterije = nivoBaterije;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getCijenaNabavke() {
        return cijenaNabavke;
    }

    public void setCijenaNabavke(double cijenaNabavke) {
        this.cijenaNabavke = cijenaNabavke;
    }

    public String getProizvodjac() {
        return proizvodjac;
    }

    public void setProizvodjac(String proizvodjac) {
        this.proizvodjac = proizvodjac;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public int getNivoBaterije() {
        return nivoBaterije;
    }

    public void setNivoBaterije(int nivoBaterije) {
        this.nivoBaterije = nivoBaterije;
    }

    /**
     * Puni bateriju vozila povećanjem nivoa baterije za 2 jedinice.
     */
    public void puniBateriju() {
        this.nivoBaterije += 2;
    }

    /**
     * Pražnjenje baterije vozila smanjivanjem nivoa baterije za 1 jedinicu.
     */
    public void prazniBateriju() {
        this.nivoBaterije -= 1;
    }

    @Override
    public String toString() {
        return "Vozilo{" +
                "id='" + id + '\'' +
                ", proizvodjac='" + proizvodjac + '\'' +
                ", model='" + model + '\'' +
                ", cijenaNabavke=" + cijenaNabavke +
                ", nivoBaterije=" + nivoBaterije +
                '}';
    }
}
