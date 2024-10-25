package org.unibl.etf.epj2.kontroleri;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.unibl.etf.epj2.model.Automobil;
import org.unibl.etf.epj2.model.Bicikl;
import org.unibl.etf.epj2.model.Trotinet;
import org.unibl.etf.epj2.model.Vozilo;
import org.unibl.etf.epj2.util.UcitavanjePodataka;

import java.time.LocalDate;
import java.util.List;

/**
 * Kontroler za upravljanje tabelama vozila u JavaFX aplikaciji.
 * Pruža mogućnost prikaza različitih tipova vozila (Automobili, Bicikli, Trotineti)
 */
public class VozilaController {

    @FXML
    private TableView<Automobil> tvAutomobili;

    @FXML
    private TableView<Bicikl> tvBicikli;

    @FXML
    private TableView<Trotinet> tvTrotineti;

    @FXML
    private TableColumn<Automobil, String> identifikatorAutomobil;

    @FXML
    private TableColumn<Automobil, String> proizvodjacAutomobil;

    @FXML
    private TableColumn<Automobil, String> modelAutomobil;

    @FXML
    private TableColumn<Automobil, Double> cijenaNabavkeAutomobil;

    @FXML
    private TableColumn<Automobil, Integer> nivoBaterijeAutomobil;

    @FXML
    private TableColumn<Automobil, LocalDate> datumNabavkeAutomobil;

    @FXML
    private TableColumn<Automobil, String> opisAutomobil;

    @FXML
    private TableColumn<Automobil, Integer> brojPutnikaAutomobil;

    @FXML
    private TableColumn<Bicikl, String> identifikatorBicikl;

    @FXML
    private TableColumn<Bicikl, String> proizvodjacBicikl;

    @FXML
    private TableColumn<Bicikl, String> modelBicikl;

    @FXML
    private TableColumn<Bicikl, String> cijenaNabavkeBicikl;

    @FXML
    private TableColumn<Bicikl, String> nivoBaterijeBicikl;

    @FXML
    private TableColumn<Bicikl, String> dometBicikl;

    @FXML
    private TableColumn<Trotinet, String> identifikatorTrotinet;

    @FXML
    private TableColumn<Trotinet, String> proizvodjacTrotinet;

    @FXML
    private TableColumn<Trotinet, String> modelTrotinet;

    @FXML
    private TableColumn<Trotinet, String> cijenaNabavkeTrotinet;

    @FXML
    private TableColumn<Trotinet, String> nivoBaterijeTrotinet;

    @FXML
    private TableColumn<Trotinet, String> maksimalnaBrzinaTrotinet;


    List<Vozilo> vozila;

    ObservableList<Automobil> automobili = FXCollections.observableArrayList();
    ObservableList<Bicikl> bicikli = FXCollections.observableArrayList();
    ObservableList<Trotinet> trotineti = FXCollections.observableArrayList();

    /**
     * Inicijalizuje podatke u tabelama za prikaz vozila.
     * Postavlja vrednosti za svaku kolonu na osnovu atributa modela i popunjava tabele sa odgovarajućim podacima.
     */
    @FXML
    public void initialize() {
        identifikatorAutomobil.setCellValueFactory(new PropertyValueFactory<>("id"));
        proizvodjacAutomobil.setCellValueFactory(new PropertyValueFactory<>("proizvodjac"));
        modelAutomobil.setCellValueFactory(new PropertyValueFactory<>("model"));
        nivoBaterijeAutomobil.setCellValueFactory(new PropertyValueFactory<>("nivoBaterije"));
        cijenaNabavkeAutomobil.setCellValueFactory(new PropertyValueFactory<>("cijenaNabavke"));
        datumNabavkeAutomobil.setCellValueFactory(new PropertyValueFactory<>("datumNabavke"));
        opisAutomobil.setCellValueFactory(new PropertyValueFactory<>("opis"));
        brojPutnikaAutomobil.setCellValueFactory(new PropertyValueFactory<>("brojPutnika"));

        identifikatorBicikl.setCellValueFactory(new PropertyValueFactory<>("id"));
        proizvodjacBicikl.setCellValueFactory(new PropertyValueFactory<>("proizvodjac"));
        modelBicikl.setCellValueFactory(new PropertyValueFactory<>("model"));
        cijenaNabavkeBicikl.setCellValueFactory(new PropertyValueFactory<>("cijenaNabavke"));
        nivoBaterijeBicikl.setCellValueFactory(new PropertyValueFactory<>("nivoBaterije"));
        dometBicikl.setCellValueFactory(new PropertyValueFactory<>("domet"));

        identifikatorTrotinet.setCellValueFactory(new PropertyValueFactory<>("id"));
        proizvodjacTrotinet.setCellValueFactory(new PropertyValueFactory<>("proizvodjac"));
        modelTrotinet.setCellValueFactory(new PropertyValueFactory<>("model"));
        cijenaNabavkeTrotinet.setCellValueFactory(new PropertyValueFactory<>("cijenaNabavke"));
        nivoBaterijeTrotinet.setCellValueFactory(new PropertyValueFactory<>("nivoBaterije"));
        maksimalnaBrzinaTrotinet.setCellValueFactory(new PropertyValueFactory<>("maxBrzina"));


        vozila = UcitavanjePodataka.getVozila();

        for (Vozilo vozilo : vozila) {
            if (vozilo instanceof Automobil) {
                automobili.add((Automobil) vozilo);
            } else if (vozilo instanceof Bicikl) {
                bicikli.add((Bicikl) vozilo);
            } else if (vozilo instanceof Trotinet) {
                trotineti.add((Trotinet) vozilo);
            }
        }

        tvAutomobili.setItems(automobili);
        tvBicikli.setItems(bicikli);
        tvTrotineti.setItems(trotineti);
    }
}
