package org.unibl.etf.epj2.util;

import javafx.scene.control.Alert.AlertType;
import org.unibl.etf.epj2.model.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Utility klasa za učitavanje podataka o vozilima i iznajmljivanjima iz fajlova.
 * Ova klasa pruža metode za učitavanje vozila i iznajmljivanja, kao i za učitavanje konfiguracija iz properties fajla.
 */
public class UcitavanjePodataka {

    private static String DATE_TIME_FORMAT = "d.M.yyyy HH:mm";
    private static String DATE_FORMAT = "d.M.yyyy.";
    private static String NEW_DATE_FORMAT = "dd.MM.yyyy.";
    private static String[] validValues = {"da", "ne"};

    private static List<Vozilo> vozila;

    public static List<Vozilo> getVozila() {
        return vozila;
    }

    public static void setVozila(List<Vozilo> vozila) {
        UcitavanjePodataka.vozila = vozila;
    }

    private static Map<LocalDateTime, Set<String>> iznajmljivanjaMap = new HashMap<>();
    private static Properties properties = ucitajPropertyFajl();

    public static Properties getProperties() {
        return properties;
    }

    public static void setProperties(Properties properties) {
        UcitavanjePodataka.properties = properties;
    }

    /**
     * Učitava vozila iz CSV fajla i vraća listu vozila.
     *
     * @return Lista vozila.
     */
    public static List<Vozilo> ucitajVozila() {
        System.out.println("Učitavanje vozila...");
        Path putanjaVozila = Path.of(properties.getProperty("FAJL_INICIJALIZACIJA"));
        Stream<String> sadrzaj;
        vozila = new ArrayList<>();

        try {
            sadrzaj = Files.lines(putanjaVozila);
            sadrzaj.forEach(s ->{
                if(!s.contains("ID")) {
                    String[] niz = s.split(",");

                    if(niz.length == 9) {
                        String id = niz[0];
                        String proizvodjac = niz[1];
                        String model = niz[2];
                        double cijenaNabavke = Double.parseDouble(niz[4]);
                        String vrsta = niz[8];

                        if(!isIdVoziloExists(id, vozila)) {
                            System.out.println("Vozilo sa ID " + id + " već postoji u listi vozila.");
                        } else {
                            switch (vrsta) {
                                case "automobil":
                                    if(isValidAutomobilData(niz)) {
                                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT);
                                        LocalDate datum = LocalDate.parse(niz[3], formatter);
                                        formatter = DateTimeFormatter.ofPattern(NEW_DATE_FORMAT);
                                        String datumNabavke = datum.format(formatter);
                                        String opis = niz[7];

                                        vozila.add(new Automobil(id, proizvodjac, model, cijenaNabavke,100, datumNabavke, opis));
                                    }
                                    break;
                                case "bicikl":
                                    if(isValidBiciklData(niz)) {
                                        int domet = Integer.parseInt(niz[5]);

                                        vozila.add(new Bicikl(id, proizvodjac, model, cijenaNabavke, 100, domet));
                                    }
                                    break;
                                case "trotinet":
                                    if(isValidTrotinetData(niz)) {
                                        int maxBrzina = Integer.parseInt(niz[6]);

                                        vozila.add(new Trotinet(id, proizvodjac, model, cijenaNabavke, 100, maxBrzina ));
                                    }
                                    break;
                                default:
                                    break;
                            }
                        }
                    } else {

                    }
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        }

        return vozila;
    }

    /**
     * Učitava iznajmljivanja iz CSV fajla i vraća listu iznajmljivanja.
     *
     * @return Lista iznajmljivanja.
     */
    public static List<Iznajmljivanje> ucitajIznajmljivanja() {
        System.out.println("Učitavanje iznajmljivanja...");
        UcitavanjePodataka.ucitajVozila();

        Path putanjaIznajmljivanje = Path.of(properties.getProperty("FAJL_IZNAJMLJIVANJE"));
        Stream<String> sadrzaj;
        List<Iznajmljivanje> iznajmljivanja = new ArrayList<>();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_TIME_FORMAT);

        try {
            sadrzaj = Files.lines(putanjaIznajmljivanje);
            sadrzaj.forEach(s -> {
                if(!s.contains("Datum")) {
                    String[] niz = s.split(",");

                    if(niz.length == 10 ) {
                        String datum = niz[0];
                        String korisnik = niz[1];
                        String id = niz[2];
                        String x1 = niz[3];
                        String y1 = niz[4];
                        String x2 = niz[5];
                        String y2 = niz[6];
                        String trajanje = niz[7];
                        String kvar  = niz[8];
                        String promocija = niz[9];

                        if(!isDateTimeValidFormat(datum) || "".equals(korisnik) || isIdVoziloExists(id, vozila) ||
                                !isValidKoordinataX(x1) || !isValidKoordinataY(y1) ||
                                !isValidKoordinataX(x2) || !isValidKoordinataY(y2) ||
                                !isTrajanjeValid(trajanje) || !isKvarValid(kvar) || !isPromocijaValid(promocija)) {
                            System.out.println("Linija: '" + s + "' sadrži neispravne podatke!");
                        } else {
                            LocalDateTime termin = LocalDateTime.parse(datum, formatter);

                            if(!iznajmljivanjaMap.containsKey(termin)) {
                                iznajmljivanjaMap.put(termin, new HashSet<>());
                            }

                            Set<String> idsUTerminu = iznajmljivanjaMap.get(termin);

                            if (idsUTerminu.contains(id)) {
                                System.out.println("Vozilo sa ID " + id + " već iznajmljeno u terminu " + datum + ".");
                            } else {
                                idsUTerminu.add(id);

                                int startX = Integer.parseInt(x1.substring(1));
                                int startY = Integer.parseInt(y1.substring(0, y1.length() - 1));
                                int krajX = Integer.parseInt(x2.substring(1));
                                int krajY = Integer.parseInt(y2.substring(0, y2.length() - 1));

                                int trajanjeInt = Integer.parseInt(trajanje);

                                iznajmljivanja.add(new Iznajmljivanje(termin, korisnik, id, startX, startY, krajX, krajY, trajanjeInt, kvar, promocija));
                            }
                        }
                    } else {

                    }
                }
            });
        } catch (IOException e) {
          e.printStackTrace();
        }

        iznajmljivanja.sort((i1, i2) -> i1.getVrijemeIznajmljivanja().compareTo(i2.getVrijemeIznajmljivanja()));

        return iznajmljivanja;
    }

    /**
     * Učitava konfiguracije iz properties fajla.
     *
     * @return Properties objekat sa konfiguracijama.
     */
    public static Properties ucitajPropertyFajl() {
        Properties properties = new Properties();
        Path putanja = Paths.get("src", "main", "resources", "config.properties");

        try(FileInputStream fis = new FileInputStream(putanja.toString())){
            properties.load(fis);
        }catch (IOException e){
            e.printStackTrace();
        }

        return properties;
    }

    /**
     * Proverava da li je koordinata validna prema zadatom obrascu.
     *
     * @param koordinata Koordinata za proveru.
     * @param startX Da li se proverava X koordinata.
     * @return True ako je koordinata validna, inače false.
     */
    private static boolean isValidKoordinata(String koordinata, boolean startX) {
        String regex;
        if (startX) {
            regex = "^\"(1[0-9]|[0-9])$";
        } else {
            regex = "^(1[0-9]|[0-9])\"$";
        }

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(koordinata);

        return matcher.matches();
    }

    private static boolean isValidKoordinataX(String koordinataX) {
        return isValidKoordinata(koordinataX, true);
    }

    private static boolean isValidKoordinataY(String koordinataY) {
        return isValidKoordinata(koordinataY, false);
    }

    /**
     * Provjerava da li je datum u validnom formatu.
     *
     * @param dateTimeStr Datum za provjeru.
     * @return True ako je datum validan, inače false.
     */
    private static boolean isDateTimeValidFormat(String dateTimeStr) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_TIME_FORMAT);
            LocalDateTime dateTime = LocalDateTime.parse(dateTimeStr, formatter);

            if (!dateTimeStr.equals(dateTime.format(formatter))) {
                return false;
            }

            if (dateTime.getMonthValue() == 2 && dateTime.getDayOfMonth() == 29) {
                return dateTime.toLocalDate().isLeapYear();
            }

            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    /**
     * Provjerava da li je trajanje validno.
     *
     * @param trajanje Trajanje za provjeru.
     * @return True ako je trajanje validno, inače false.
     */
    private static boolean isTrajanjeValid(String trajanje) {
        try {
            int trajanjeInt = Integer.parseInt(trajanje);
            if (trajanjeInt < 1) {
                return false;
            }
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Provjerava da li su proslijedjene vrijednosti validne.
     * Vrijednost je validna ako je parametar value ekvivalentan stringu "da" ili "ne".
     * @param value
     * @param validValues
     * @return True ako je proslijedjena vrijednost validna, inače false.
     */
    private static boolean isValid(String value, String[] validValues) {
        for (String validValue : validValues) {
            if (validValue.equals(value)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Provjerava da li je vrijednost kvara validna.
     * @param kvar
     * @return True ako je vrijednost validna, inače false.
     */
    private static boolean isKvarValid(String kvar) {
        return isValid(kvar, validValues);
    }

    /**
     * Provjerava da li je vrijednost promocije validna.
     * @param promocija
     * @return True ako je vrijednost validna, inače false.
     */
    private static boolean isPromocijaValid(String promocija) {
        return isValid(promocija, validValues);
    }

    /**
     * Provjerava da li je ID vozila već prisutan u listi vozila.
     *
     * @param id ID vozila.
     * @param vozila Lista vozila.
     * @return True ako ID vozila postoji, inače false.
     */
    private static boolean isIdVoziloExists(String id, List<Vozilo> vozila) {
        for(Vozilo vozilo : vozila) {
            if(id.equals(vozilo.getId())) {
                return false;
            }
        }
        return true;
    }

    /**
     * Provjerava da li su podaci o automobilu validni.
     *
     * @param data Niz sa podacima o automobilu.
     * @return True ako su podaci validni, inače false.
     */
    private static boolean isValidAutomobilData(String[] data) {
        try {
            LocalDate.parse(data[3], DateTimeFormatter.ofPattern(DATE_FORMAT));

            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Provjerava da li su podaci o biciklu validni.
     *
     * @param data Niz sa podacima o biciklu.
     * @return True ako su podaci validni, inače false.
     */
    private static boolean isValidBiciklData(String[] data) {
        try {
            Integer.parseInt(data[5]);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Provjerava da li su podaci o trotinetu validni.
     *
     * @param data Niz sa podacima o trotinetu.
     * @return True ako su podaci validni, inače false.
     */
    private static boolean isValidTrotinetData(String[] data) {
        try {
            Integer.parseInt(data[6]);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
