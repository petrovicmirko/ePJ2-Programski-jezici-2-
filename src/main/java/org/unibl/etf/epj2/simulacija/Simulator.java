package org.unibl.etf.epj2.simulacija;

import org.unibl.etf.epj2.izvjestaji.DnevniIzvjestaj;
import org.unibl.etf.epj2.izvjestaji.MaxPrihodPoVozilu;
import org.unibl.etf.epj2.izvjestaji.SumarniIzvjestaj;
import org.unibl.etf.epj2.kontroleri.GlavniProzorController;
import org.unibl.etf.epj2.model.Iznajmljivanje;
import org.unibl.etf.epj2.model.Kvar;
import org.unibl.etf.epj2.model.Racun;
import org.unibl.etf.epj2.model.Vozilo;
import org.unibl.etf.epj2.util.UcitavanjePodataka;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Klasa koja simulira iznajmljivanje vozila i upravlja svim povezanim procesima.
 * U ovoj klasi se čuva lista iznajmljivanja, mapa vozila, kao i lista kvarova.
 * Simulacija se vrši tako što se iznajmljivanja grupišu po vremenu, pokreću se u tim terminima,
 * a zatim se izvršava pauza prije nego što se nastavi sa sljedećim terminom.
 */
public class Simulator extends Thread {


    private static List<Iznajmljivanje> iznajmljivanja = new ArrayList<>();
    public static Map<String, Integer> korisnikIznajmljivanja;
    private final int BROJ_KOLONA = 20;
    private final int BROJ_REDOVA = 20;

    public static boolean KRAJ_SIMULACIJE;

    GlavniProzorController glavniProzorController;

    public static List<Racun> racuni = new ArrayList<>();

    public static Vozilo[][] mapa;

    public static List<Kvar> kvarovi;

    public static String vrijemeIDatum;

    private DateTimeFormatter formatter;

    /**
     * Konstruktor koji inicijalizuje simulaciju.
     * Učita iznajmljivanja, postavlja početne vrednosti i kreira potrebne objekte.
     */
    public Simulator() {
        iznajmljivanja = UcitavanjePodataka.ucitajIznajmljivanja();
        korisnikIznajmljivanja = new ConcurrentHashMap<>();
        mapa = new Vozilo[BROJ_KOLONA][BROJ_REDOVA];
        KRAJ_SIMULACIJE = false;
        glavniProzorController = new GlavniProzorController();
        kvarovi = new ArrayList<>();
    }

    public static List<Racun> getRacuni() {
        return racuni;
    }

    /**
     * Izvršava simulaciju iznajmljivanja vozila.
     * Grupiše iznajmljivanja po vremenu, pokreće ih u tim terminima i izvršava pauzu između termina.
     * Nakon završetka simulacije, obnavlja stanje glavnog prozora i serijalizuje podatke o najvećem prihodu po vozilu.
     */
    @Override
    public void run() {
        Map<LocalDateTime, List<Iznajmljivanje>> grupeIznajmljivanja = grupisiIznajmljivanjaPoVremenu();

        for (Map.Entry<LocalDateTime, List<Iznajmljivanje>> entry : grupeIznajmljivanja.entrySet()) {
            LocalDateTime vrijeme = entry.getKey();
            List<Iznajmljivanje> iznajmljivanjaPoTerminu = entry.getValue();

            //System.out.println("Pokrećem simulaciju za vrijeme: " + vrijeme);

            formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy. HH.mm");
            vrijemeIDatum = vrijeme.format(formatter);

            for(Iznajmljivanje iznajmljivanje : iznajmljivanjaPoTerminu) {
                iznajmljivanje.start();
            }

            for(Iznajmljivanje iznajmljivanje : iznajmljivanjaPoTerminu) {
                try{
                    iznajmljivanje.join();
                } catch(InterruptedException e) {
                    e.printStackTrace();
                }
            }

            //System.out.println("Završena simulacija za vrijeme: " + vrijeme);
            //System.out.println("Pauza od 5 sekundi pre sledećeg termina...");

            ukloniVozilaSaMape();
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        //System.out.println("Sve simulacije su završene.");

        KRAJ_SIMULACIJE = true;
        vrijemeIDatum = "";
        GlavniProzorController.prikaziVozila.setDisable(false);
        GlavniProzorController.prikaziKvarove.setDisable(false);
        GlavniProzorController.prikaziRezultatePoslovanja.setDisable(false);
        GlavniProzorController.prikaziNajvecePrihode.setDisable(false);

        MaxPrihodPoVozilu.serijalizujPodatke();
    }

    /**
     * Grupiše iznajmljivanja po vremenu.
     *
     * @return Map sa vremenskim ključevima i listama iznajmljivanja
     */
    private static Map<LocalDateTime, List<Iznajmljivanje>> grupisiIznajmljivanjaPoVremenu() {
        Map<LocalDateTime, List<Iznajmljivanje>> grupeIznajmljivanja = new TreeMap<>();
        for (Iznajmljivanje iznajmljivanje : iznajmljivanja) {
            grupeIznajmljivanja
                    .computeIfAbsent(iznajmljivanje.getVrijemeIznajmljivanja(), k -> new ArrayList<>())
                    .add(iznajmljivanje);
        }
        return grupeIznajmljivanja;
    }

    /**
     * Uklanja vozila sa mape tako što postavlja sve položaje na null.
     */
    public void ukloniVozilaSaMape() {
        for (int i = 0; i < BROJ_KOLONA; i++) {
            for (int j = 0; j < BROJ_REDOVA; j++) {
                mapa[i][j] = null;
            }
        }
    }
}
