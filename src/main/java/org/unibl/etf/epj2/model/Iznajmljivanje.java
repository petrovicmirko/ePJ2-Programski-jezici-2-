package org.unibl.etf.epj2.model;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import org.unibl.etf.epj2.izvjestaji.MaxPrihodPoVozilu;
import org.unibl.etf.epj2.kontroleri.GlavniProzorController;
import org.unibl.etf.epj2.simulacija.Simulator;
import org.unibl.etf.epj2.util.UcitavanjePodataka;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Klasa koja predstavlja sistem za iznajmljivanje vozila.
 * Ova klasa omogućava simulaciju kretanja vozila, praćenje iznajmljivanja i generisanje računa za iznajmljivanje.
 */
public class Iznajmljivanje extends Thread {
    private LocalDateTime vrijemeIznajmljivanja;
    private Korisnik korisnik;
    private String imeKorisnika;
    private String idVozila;
    private int startX;
    private int startY;
    private int krajX;
    private int krajY;
    private int trajanje;
    private String kvar;
    private String promocija;
    private boolean zavrsenoKretanje = false;


    public Iznajmljivanje(LocalDateTime vrijemeIznajmljivanja, String korisnik, String idVozila, int startX, int startY, int krajX, int krajY, int trajanje, String kvar, String promocija) {
        this.vrijemeIznajmljivanja = vrijemeIznajmljivanja;
        this.korisnik = null;
        this.imeKorisnika = korisnik;
        this.idVozila = idVozila;
        this.startX = startX;
        this.startY = startY;
        this.krajX = krajX;
        this.krajY = krajY;
        this.trajanje = trajanje;
        this.kvar = kvar;
        this.promocija = promocija;
    }

    public Korisnik getKorisnik() {
        return korisnik;
    }

    public void setKorisnik(Korisnik korisnik) {
        this.korisnik = korisnik;
    }

    public LocalDateTime getVrijemeIznajmljivanja() {
        return vrijemeIznajmljivanja;
    }

    public void setVrijemeIznajmljivanja(LocalDateTime vrijemeIznajmljivanja) {
        this.vrijemeIznajmljivanja = vrijemeIznajmljivanja;
    }

    public String getImeKorisnika() {
        return imeKorisnika;
    }

    public void setKorisnik(String imeKorisnika) {
        this.imeKorisnika = imeKorisnika;
    }

    public String getIdVozila() {
        return idVozila;
    }

    public void setIdVozila(String idVozila) {
        this.idVozila = idVozila;
    }

    public int getStartX() {
        return startX;
    }

    public void setStartX(int startX) {
        this.startX = startX;
    }

    public int getStartY() {
        return startY;
    }

    public void setStartY(int startY) {
        this.startY = startY;
    }

    public int getKrajX() {
        return krajX;
    }

    public void setKrajX(int krajX) {
        this.krajX = krajX;
    }

    public int getKrajY() {
        return krajY;
    }

    public void setKrajY(int krajY) {
        this.krajY = krajY;
    }

    public int getTrajanje() {
        return trajanje;
    }

    public void setTrajanje(int trajanje) {
        this.trajanje = trajanje;
    }

    public String getKvar() {
        return kvar;
    }

    public void setKvar(String kvar) {
        this.kvar = kvar;
    }

    public String getPromocija() {
        return promocija;
    }

    public void setPromocija(String promocija) {
        this.promocija = promocija;
    }

    /**
     * Izračunava putanju vozila od početne pozicije do krajnje pozicije.
     * Putanja se računa tako što se pomera vozilo horizontalno prvo, pa vertikalno.
     *
     * @param startX početna X koordinata
     * @param startY početna Y koordinata
     * @param krajX krajnja X koordinata
     * @param krajY krajnja Y koordinata
     * @return Lista koordinata putanje kroz koje vozilo prolazi
     */
    public List<int[]> izracunajPutanju(int startX, int startY, int krajX, int krajY) {
        List<int[]> putanja = new ArrayList<>();

        if (startX != krajX) {
            int xIncrement = startX < krajX ? 1 : -1;
            while (startX != krajX) {
                putanja.add(new int[]{startX, startY});
                startX += xIncrement;
            }
        }

        if (startY != krajY) {
            int yIncrement = startY < krajY ? 1 : -1;
            while (startY != krajY) {
                putanja.add(new int[]{startX, startY});
                startY += yIncrement;
            }
        }
        putanja.add(new int[]{krajX, krajY});

        return putanja;
    }

    /**
     * Proverava da li je vozilo prošlo u širi dio grada tokom putovanja.
     *
     * @param putanja Lista koordinata putanje vozila
     * @return True ako je vozilo prošlo u širi deo grada, inače False
     */
    public boolean isPresloUSiriDioGrada(List<int[]> putanja) {
        for(int[] pozicija : putanja) {
            int x = pozicija[0];
            int y = pozicija[1];

            if(isUSiremDijeluGrada(x,y)) {
                return true;
            }
        }
        return false;
    }

    private  boolean isUSiremDijeluGrada(int x, int y) {
        return x < 5 || x > 14 || y < 5 || y > 14;
    }

    public void setZavrsenoKretanje(boolean zavrsenoKretanje) {
        this.zavrsenoKretanje = zavrsenoKretanje;
    }

    public boolean isZavrsenoKretanje() {
        return zavrsenoKretanje;
    }

    /**
     * Izvršava simulaciju iznajmljivanja vozila i ažurira stanje u sistemu.
     * Uključuje izračunavanje putanje, ažuriranje stanja vozila i generisanje računa.
     */
    @Override
    public void run() {
        String idVozila = getIdVozila();

        LocalDateTime datumIVrijeme = getVrijemeIznajmljivanja();
        LocalDate datum = getVrijemeIznajmljivanja().toLocalDate();

        Optional<Vozilo> trazenoVozilo = UcitavanjePodataka.getVozila().stream()
                .filter(vozilo -> vozilo.getId().equals(idVozila))
                .findFirst();

        Vozilo vozilo = trazenoVozilo.get();

        if(vozilo instanceof Automobil) {
            this.korisnik = new Korisnik(imeKorisnika);
        }

        List<int[]> putanja = izracunajPutanju(startX, startY, krajX, krajY);
        double zadrzavanjeNaPolju = (double) trajanje / putanja.size();

        int[] prethodnaPozicija = null;

        for(int[] pozicija : putanja) {
            int x = pozicija[0];
            int y = pozicija[1];

            Platform.runLater(() -> azurirajGridPane());

            if (prethodnaPozicija != null) {
                int prethodniX = prethodnaPozicija[0];
                int prethodniY = prethodnaPozicija[1];
                Simulator.mapa[prethodniX][prethodniY] = null;
            }

            postaviVozilo(vozilo, x, y);
            vozilo.prazniBateriju();

            prethodnaPozicija = pozicija;
            try {
                Thread.sleep((long) (zadrzavanjeNaPolju * 1000));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if(x == krajX && y == krajY) {
                zavrsenoKretanje = true;
                Simulator.mapa[x][y] = null;
            }
        }

        while(vozilo.getNivoBaterije() <= 98)
            vozilo.puniBateriju();

        boolean presloUSiriDioGrada = isPresloUSiriDioGrada(putanja);

        Simulator.korisnikIznajmljivanja.put(imeKorisnika, Simulator.korisnikIznajmljivanja.getOrDefault(imeKorisnika, 0) + 1);
        int brojIznajmljivanja = Simulator.korisnikIznajmljivanja.get(imeKorisnika);

        Racun.brojacRacuna++;
        generisiRacun(imeKorisnika, vozilo, trajanje, kvar, promocija, brojIznajmljivanja, presloUSiriDioGrada, datum);

        if(kvar.equals("da")) {
            Simulator.kvarovi.add(new Kvar(vozilo, datumIVrijeme));
        }
    }

    /**
     * Generiše račun na osnovu informacija o iznajmljivanju, vozilu i korisniku.
     *
     * @param korisnik Ime korisnika koji iznajmljuje vozilo
     * @param vozilo Vozilo koje se iznajmljuje
     * @param trajanje Trajanje iznajmljivanja u sekundama
     * @param kvar Da li je vozilo imalo kvar (da/ne)
     * @param promocija Da li je korišćena promocija (da/ne)
     * @param brojIznajmljivanja Broj trenutnog iznajmljivanja korisnika
     * @param presloUSiriDioGrada Da li je vozilo prešlo u širi dio grada
     * @param datum Datum iznajmljivanja
     */
    private void generisiRacun(String korisnik, Vozilo vozilo, int trajanje, String kvar, String promocija, int brojIznajmljivanja, boolean presloUSiriDioGrada, LocalDate datum) {
        double ukupnoZaPlacanje = 0.00;
        double osnovnaCijena = 0.00;
        double iznos = 0.00;
        double vrijednostPopusta = 0.00;
        double vrijednostPromocije = 0.00;

        Racun racun = new Racun();
        racun.setVozilo(vozilo);
        racun.setDatum(datum);

        if(kvar.equals("da")) {
            racun.setUkupnoZaPlacanje(0.00);
            if(presloUSiriDioGrada) {
                Racun.brojacSiriDioGrada++;
            } else {
                Racun.brojacUziDioGrada++;
            }
            racun.setDesioSeKvar(true);
        } else{
            if(presloUSiriDioGrada) {
                Racun.brojacSiriDioGrada++;
                double DISTANCE_WIDE = Double.parseDouble(UcitavanjePodataka.getProperties().getProperty("DISTANCE_WIDE"));
                if(vozilo instanceof Automobil) {
                    double CAR_UNIT_PRICE = Double.parseDouble(UcitavanjePodataka.getProperties().getProperty("CAR_UNIT_PRICE"));
                    osnovnaCijena = CAR_UNIT_PRICE * trajanje;
                } else if(vozilo instanceof Bicikl) {
                    double BIKE_UNIT_PRICE = Double.parseDouble(UcitavanjePodataka.getProperties().getProperty("BIKE_UNIT_PRICE"));
                    osnovnaCijena = BIKE_UNIT_PRICE * trajanje;
                } else {
                    double SCOOTER_UNIT_PRICE = Double.parseDouble(UcitavanjePodataka.getProperties().getProperty("SCOOTER_UNIT_PRICE"));
                    osnovnaCijena = SCOOTER_UNIT_PRICE * trajanje;
                }
                racun.setOsnovnaCijena(osnovnaCijena);
                iznos = osnovnaCijena * DISTANCE_WIDE;
                racun.setIznos(iznos);
            } else {
                Racun.brojacUziDioGrada++;
                double DISTANCE_NARROW = Double.parseDouble(UcitavanjePodataka.getProperties().getProperty("DISTANCE_NARROW"));
                if(vozilo instanceof Automobil) {
                    double CAR_UNIT_PRICE = Double.parseDouble(UcitavanjePodataka.getProperties().getProperty("CAR_UNIT_PRICE"));
                    osnovnaCijena = CAR_UNIT_PRICE * trajanje;
                } else if(vozilo instanceof Bicikl) {
                    double BIKE_UNIT_PRICE = Double.parseDouble(UcitavanjePodataka.getProperties().getProperty("BIKE_UNIT_PRICE"));
                    osnovnaCijena = BIKE_UNIT_PRICE * trajanje;
                } else {
                    double SCOOTER_UNIT_PRICE = Double.parseDouble(UcitavanjePodataka.getProperties().getProperty("SCOOTER_UNIT_PRICE"));
                    osnovnaCijena = SCOOTER_UNIT_PRICE * trajanje;
                }
                racun.setOsnovnaCijena(osnovnaCijena);
                iznos = osnovnaCijena * DISTANCE_NARROW;
                racun.setIznos(iznos);
            }

            if(brojIznajmljivanja % 10 == 0) {
                double popust = Double.parseDouble(UcitavanjePodataka.getProperties().getProperty("DISCOUNT"));
                vrijednostPopusta = popust * iznos;
                racun.setVrijednostPopusta(vrijednostPopusta);
            }

            if(promocija.equals("da")) {
                double promocijaDouble = Double.parseDouble(UcitavanjePodataka.getProperties().getProperty("DISCOUNT_PROM"));
                vrijednostPromocije = promocijaDouble * iznos;
                racun.setVrijednostPromocije(vrijednostPromocije);
            }
            ukupnoZaPlacanje = iznos - vrijednostPopusta - vrijednostPromocije;
            racun.setUkupnoZaPlacanje(ukupnoZaPlacanje);
        }

        String racunTekst = generisiRacunTekst(vozilo, trajanje, kvar, promocija, brojIznajmljivanja, presloUSiriDioGrada,
                racun.getOsnovnaCijena(), racun.getIznos(), racun.getUkupnoZaPlacanje(), racun.getVrijednostPopusta(), racun.getVrijednostPromocije());

        Racun.azurirajBrojacPoDatumima(datum, presloUSiriDioGrada);

        upisiRacunUFajl(korisnik, racunTekst);

        Simulator.racuni.add(racun);

        Double prihod = new BigDecimal(ukupnoZaPlacanje).setScale(3, RoundingMode.HALF_UP).doubleValue();
        if(vozilo instanceof Automobil) {
            Automobil automobil = (Automobil) vozilo;

            double stariPrihodAutomobila = Automobil.prihodiAutomobila.get(automobil);
            double noviPrihodAutomobila = stariPrihodAutomobila + prihod;
            Automobil.prihodiAutomobila.put(automobil, noviPrihodAutomobila);
            MaxPrihodPoVozilu.azurirajMaxAutomobil(automobil, noviPrihodAutomobila);
        } else if(vozilo instanceof Bicikl) {
            Bicikl bicikl = (Bicikl) vozilo;

            double stariPrihodBicikla = Bicikl.prihodiBicikla.get(bicikl);
            double noviPrihodBicikla = stariPrihodBicikla + prihod;
            Bicikl.prihodiBicikla.put(bicikl, noviPrihodBicikla);
            MaxPrihodPoVozilu.azurirajMaxBicikl(bicikl, noviPrihodBicikla);
        } else {
            Trotinet trotinet = (Trotinet) vozilo;

            double stariPrihodTrotineta = Trotinet.prihodiTrotineta.get(trotinet);
            double noviPrihodTrotineta = stariPrihodTrotineta + prihod;
            Trotinet.prihodiTrotineta.put(trotinet, noviPrihodTrotineta);
            MaxPrihodPoVozilu.azurirajMaxTrotinet(trotinet, noviPrihodTrotineta);
        }
    }

    /**
     * Upisuje račun u fajl sa odgovarajućim imenom i lokacijom.
     *
     * @param korisnik Ime korisnika za kojeg se generiše račun
     * @param racun Tekst računa koji treba upisati u fajl
     */
    private void upisiRacunUFajl(String korisnik, String racun) {
        String direktorijum = UcitavanjePodataka.getProperties().getProperty("RACUNI");
        String folderPath = Paths.get(direktorijum, korisnik).toString();
        String putanjaDoFajla = Paths.get(folderPath, korisnik + "_racun" + Racun.brojacRacuna + ".txt").toString();

        try {
            Files.createDirectories(Paths.get(folderPath));
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(putanjaDoFajla))) {
                writer.write(racun);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Generiše tekst računa na osnovu informacija o iznajmljivanju.
     *
     * @param vozilo Vozilo koje se iznajmljuje
     * @param trajanje Trajanje iznajmljivanja u sekundama
     * @param kvar Da li je vozilo imalo kvar (da/ne)
     * @param promocija Da li je korišćena promocija (da/ne)
     * @param brojIznajmljivanja Broj trenutnog iznajmljivanja korisnika
     * @param presloUSiriDioGrada Da li je vozilo prošlo u širi dio grada
     * @param osnovnaCijena Osnovna cijena za iznajmljivanje
     * @param iznos Ukupni iznos prije popusta i promocije
     * @param ukupnoZaPlacanje Ukupna cijena za plaćanje nakon popusta i promocije
     * @param vrijednostPopusta Vrijednost popusta
     * @param vrijednostPromocije Vrijednost promocije
     * @return Tekst računa
     */
    private String generisiRacunTekst(Vozilo vozilo, int trajanje, String kvar, String promocija, int brojIznajmljivanja, boolean presloUSiriDioGrada, double osnovnaCijena, double iznos, double ukupnoZaPlacanje, double vrijednostPopusta, double vrijednostPromocije) {
        StringBuilder racun = new StringBuilder();
        racun.append("Račun za iznajmljivanje\n");
        racun.append("=========================\n");
        racun.append("Korisnik: " + imeKorisnika + "\n");
        if(korisnik != null) {
            racun.append("      " + korisnik.getIdentifikacioniDokument() + "\n");
            racun.append("      " + korisnik.getBrojVozacke() + "\n");
        }
        racun.append("ID Vozila: ").append(vozilo.getId()).append("\n");
        racun.append("Trajanje: ").append(trajanje).append(" sekundi\n");
        racun.append("Kvar: ").append(kvar).append("\n");
        racun.append("Promocija: ").append(promocija).append("\n");
        racun.append("Broj iznajmljivanja: ").append(brojIznajmljivanja).append("\n");
        racun.append("Prešlo u širi dio grada: ").append(presloUSiriDioGrada).append("\n");
        racun.append("Osnovna cijena: ").append(String.format("%.2f", osnovnaCijena)).append("\n");
        racun.append("Iznos: ").append(String.format("%.2f", iznos)).append("\n");
        if (vrijednostPopusta > 0) {
            racun.append("Popust: -").append(String.format("%.2f", vrijednostPopusta)).append("\n");
        }
        if (vrijednostPromocije > 0) {
            racun.append("Promocija: -").append(String.format("%.2f", vrijednostPromocije)).append("\n");
        }
        racun.append("=========================\n");
        racun.append("Ukupna cijena za plaćanje: ").append(String.format("%.2f", ukupnoZaPlacanje)).append("\n");

        return racun.toString();
    }

    /**
     * Postavlja vozilo na određenu poziciju na mapi.
     *
     * @param vozilo Vozilo koje treba postaviti
     * @param red X koordinata pozicije
     * @param kolona Y koordinata pozicije
     */
    private void postaviVozilo(Vozilo vozilo, int red, int kolona) {
        Simulator.mapa[red][kolona] = vozilo;
    }

    public void ispisiMapu(Vozilo[][] mapa) {
        for (int i = 0; i < 20; i++) {
            for (int j = 0; j < 20; j++) {
                if (mapa[i][j] != null) {
                    System.out.print("V ");
                } else {
                    System.out.print(". ");
                }
            }
            System.out.println();
        }
    }

    /**
     * Ažurira GridPane na osnovu trenutnog stanja mape.
     * Poziva metod za generisanje Timeline objekta koji osvježava prikaz na ekranu.
     */
    public static void azurirajGridPane(){
        Timeline timeline1 = generisiGridTimeline(GlavniProzorController.gridPane, GlavniProzorController.simulator.mapa, 700);
        timeline1.play();
    }

    /**
     * Generiše Timeline objekat koji ažurira GridPane u definisanom intervalu.
     *
     * @param grid GridPane koji treba ažurirati
     * @param mapa Mapa vozila
     * @param interval Interval osvežavanja u milisekundama
     * @return Timeline objekat za ažuriranje GridPane
     */
    public static Timeline generisiGridTimeline(GridPane grid, Vozilo[][] mapa, int interval) {
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(interval), actionEvent -> {
            azurirajMapu(grid, mapa);
        }));

        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();

        return timeline;
    }

    /**
     * Ažurira GridPane prema trenutnom stanju mape vozila.
     *
     * @param gridPane GridPane koji se ažurira
     * @param mapa Mapa vozila
     */
    public static void azurirajMapu(GridPane gridPane, Vozilo[][] mapa) {
        for (Node n : gridPane.getChildren()) {

            int red = GridPane.getRowIndex(n) == null ? 0 : GridPane.getRowIndex(n);
            int kolona = GridPane.getColumnIndex(n) == null ? 0 : GridPane.getColumnIndex(n);

            if (n instanceof StackPane) {
                StackPane stackPane = (StackPane) n;
                Rectangle rectangle = (Rectangle) stackPane.getChildren().get(0);
                Label idLabel = (Label) stackPane.getChildren().get(1);
                Label nivoBaterije = (Label) stackPane.getChildren().get(2);

                StackPane.setAlignment(idLabel, Pos.TOP_CENTER);
                StackPane.setAlignment(nivoBaterije, Pos.BOTTOM_CENTER);
                StackPane.setMargin(idLabel, new Insets(0, 0, 10, 0));
                StackPane.setMargin(nivoBaterije, new Insets(10, 0, 0, 0));

                if (mapa[kolona][red] != null) {
                    Vozilo vozilo = mapa[kolona][red];
                    idLabel.setText("ID: " + vozilo.getId());
                    nivoBaterije.setText(vozilo.getNivoBaterije() + "%");

                    if(vozilo instanceof Automobil) {
                        rectangle.setFill(Color.RED);
                    } else if(vozilo instanceof Bicikl) {
                        rectangle.setFill(Color.ORANGE);
                    } else {
                        rectangle.setFill(Color.GREEN);
                    }

                } else {
                        if (red >= 5 && red <= 14 && kolona >= 5 && kolona <= 14) {
                            rectangle.setFill(Color.ROYALBLUE);
                        } else {
                            rectangle.setFill(Color.WHITE);
                        }
                        idLabel.setText("");
                        nivoBaterije.setText("");
                }
            }
        }
    }



    @Override
    public String toString() {
        return "Iznajmljivanje {" +
                " vrijemeIznajmljivanja=" + vrijemeIznajmljivanja +
                ", korisnik='" + imeKorisnika + '\'' +
                ", idVozila='" + idVozila + '\'' +
                ", startX=" + startX +
                ", startY=" + startY +
                ", krajX=" + krajX +
                ", krajY=" + krajY +
                ", trajanje=" + trajanje +
                ", kvar='" + kvar + '\'' +
                ", promocija='" + promocija + '\'' +
                '}';
    }
}
