package org.unibl.etf.epj2.izvjestaji;

import org.unibl.etf.epj2.model.Automobil;
import org.unibl.etf.epj2.model.Bicikl;
import org.unibl.etf.epj2.model.Trotinet;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Klasa koja čuva informacije o maksimalnom prihodu po vozilu
 * (Automobil, Bicikl, Trotinet) i omogućava serijalizaciju i deserijalizaciju
 * tih informacija.
 */
public class MaxPrihodPoVozilu {
    private static final long serialVersionUID = 1L;
    private static final String FOLDER_PATH = "src/main/resources/serijalizovaniPodaci";

    private static Map<Automobil,Double> maxAutomobil = new HashMap<>();
    private static Map<Bicikl,Double> maxBicikl = new HashMap<>();
    private static Map<Trotinet,Double> maxTrotinet = new HashMap<>();

    public static Map<Automobil, Double> getMaxAutomobil() {
        return maxAutomobil;
    }

    public static Map<Bicikl, Double> getMaxBicikl() {
        return maxBicikl;
    }

    public static Map<Trotinet, Double> getMaxTrotinet() {
        return maxTrotinet;
    }

    /**
     * Ažurira mapu maksimalnih prihoda za automobil.
     *
     * @param automobil Automobil koji se ažurira.
     * @param prihod Prihod od automobila.
     */
    public static void azurirajMaxAutomobil(Automobil automobil, double prihod) {
        Double maxPrihod = maxAutomobil.values().stream().max(Double::compare).orElse(null);
        prihod = Math.round(prihod * 1000.0) / 1000.0;

        if (maxPrihod == null || prihod > maxPrihod) {
            maxAutomobil.clear();
            maxAutomobil.put(automobil, prihod);
        } else if (prihod == maxPrihod) {
            maxAutomobil.put(automobil, prihod);
        }
    }

    /**
     * Ažurira mapu maksimalnih prihoda za bicikl.
     *
     * @param bicikl Bicikl koji se ažurira.
     * @param prihod Prihod od bicikla.
     */
    public static void azurirajMaxBicikl(Bicikl bicikl, double prihod) {
        Double maxPrihod = maxBicikl.values().stream().max(Double::compare).orElse(null);
        prihod = Math.round(prihod * 1000.0) / 1000.0;

        if (maxPrihod == null || prihod > maxPrihod) {
            maxBicikl.clear();
            maxBicikl.put(bicikl, prihod);
        } else if (prihod == maxPrihod) {
            maxBicikl.put(bicikl, prihod);
        }
    }

    /**
     * Ažurira mapu maksimalnih prihoda za trotinet.
     *
     * @param trotinet Trotinet koji se ažurira.
     * @param prihod Prihod od trotineta.
     */
    public static void azurirajMaxTrotinet(Trotinet trotinet, double prihod) {
        Double maxPrihod = maxTrotinet.values().stream().max(Double::compare).orElse(null);
        prihod = Math.round(prihod * 1000.0) / 1000.0;

        if (maxPrihod == null || prihod > maxPrihod) {
            maxTrotinet.clear();
            maxTrotinet.put(trotinet, prihod);
        } else if (prihod == maxPrihod) {
            maxTrotinet.put(trotinet, prihod);
        }
    }

    /**
     * Serijalizuje podatke o maksimalnim prihodima po vozilu u fajlove.
     */
    public static void serijalizujPodatke() {
        File folder = new File(FOLDER_PATH);
        if (!folder.exists()) {
            folder.mkdirs();
        }

        try (ObjectOutputStream oosAutomobil = new ObjectOutputStream(new FileOutputStream(FOLDER_PATH + "/maxAutomobil.ser"));
             ObjectOutputStream oosBicikl = new ObjectOutputStream(new FileOutputStream(FOLDER_PATH + "/maxBicikl.ser"));
             ObjectOutputStream oosTrotinet = new ObjectOutputStream(new FileOutputStream(FOLDER_PATH + "/maxTrotinet.ser"))) {

            oosAutomobil.writeObject(maxAutomobil);
            oosBicikl.writeObject(maxBicikl);
            oosTrotinet.writeObject(maxTrotinet);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Deserijalizuje podatke o maksimalnim prihodima za automobile iz fajla.
     *
     * @return Mapa automobila i njihovih maksimalnih prihoda.
     */
    public static Map<Automobil, Double> deserijalizujAutomobil() {
        Map<Automobil, Double> automobili = new HashMap<>();
        File file = new File(FOLDER_PATH + "/maxAutomobil.ser");
        if (file.exists()) {
            try (ObjectInputStream oisAutomobil = new ObjectInputStream(new FileInputStream(file))) {
                automobili = (Map<Automobil, Double>) oisAutomobil.readObject();
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Fajl za automobile ne postoji.");
        }
        return automobili;
    }

    /**
     * Deserijalizuje podatke o maksimalnim prihodima za bicikle iz fajla.
     *
     * @return Mapa bicikala i njihovih maksimalnih prihoda.
     */
    public static Map<Bicikl, Double> deserijalizujBicikl() {
        Map<Bicikl, Double> bicikli = new HashMap<>();
        File file = new File(FOLDER_PATH + "/maxBicikl.ser");
        if (file.exists()) {
            try (ObjectInputStream oisBicikl = new ObjectInputStream(new FileInputStream(file))) {
                bicikli = (Map<Bicikl, Double>) oisBicikl.readObject();
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Fajl za bicikle ne postoji.");
        }
        return bicikli;
    }

    /**
     * Deserijalizuje podatke o maksimalnim prihodima za trotinete iz fajla.
     *
     * @return Mapa trotineta i njihovih maksimalnih prihoda.
     */
    public static Map<Trotinet, Double> deserijalizujTrotinet() {
        Map<Trotinet, Double> trotineti = new HashMap<>();
        File file = new File(FOLDER_PATH + "/maxTrotinet.ser");
        if (file.exists()) {
            try (ObjectInputStream oisTrotinet = new ObjectInputStream(new FileInputStream(file))) {
                trotineti = (Map<Trotinet, Double>) oisTrotinet.readObject();
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Fajl za trotinete ne postoji.");
        }
        return trotineti;
    }
}
