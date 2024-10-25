package org.unibl.etf.epj2.kontroleri;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import org.unibl.etf.epj2.izvjestaji.DnevniIzvjestaj;
import org.unibl.etf.epj2.izvjestaji.SumarniIzvjestaj;
import org.unibl.etf.epj2.model.Racun;
import org.unibl.etf.epj2.simulacija.Simulator;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Kontroler za prikaz rezultata poslovanja.
 * Upravljava prikazom dnevnih i sumarnih izvještaja u aplikaciji.
 */
public class RezultatiPoslovanjaController {
    @FXML
    private TableView<DnevniIzvjestaj> tvDnevniIzvjestaj;

    @FXML
    private TableColumn<DnevniIzvjestaj, LocalDate> tcTerminDnevni;

    @FXML
    private TableColumn<DnevniIzvjestaj, String> tcPrihodDnevni;

    @FXML
    private TableColumn<DnevniIzvjestaj, String> tcPopustDnevni;

    @FXML
    private TableColumn<DnevniIzvjestaj, String> tcPromocijaDnevni;

    @FXML
    private TableColumn<DnevniIzvjestaj, String> tcSiriDioDnevni;

    @FXML
    private TableColumn<DnevniIzvjestaj, String> tcUziDioDnevni;

    @FXML
    private TableColumn<DnevniIzvjestaj, String> tcOdrzavanjeDnevni;

    @FXML
    private TableColumn<DnevniIzvjestaj, String> tcPopravkeDnevni;

    @FXML
    private TableColumn<SumarniIzvjestaj, String> tcPrihodSumarni;

    @FXML
    private TableColumn<SumarniIzvjestaj, String> tcPopustSumarni;

    @FXML
    private TableColumn<SumarniIzvjestaj, String> tcPromocijaSumarni;

    @FXML
    private TableColumn<SumarniIzvjestaj, String> tcSiriDioSumarni;

    @FXML
    private TableColumn<SumarniIzvjestaj, String> tcUziDioSumarni;

    @FXML
    private TableColumn<SumarniIzvjestaj, String> tcOdrzavanjeSumarni;

    @FXML
    private TableColumn<SumarniIzvjestaj, String> tcPopravkeSumarni;

    @FXML
    private TableColumn<SumarniIzvjestaj, String> tcTroskoviSumarni;

    @FXML
    private TableColumn<SumarniIzvjestaj, String> tcPorezSumarni;

    @FXML
    private Button bSumarniIzvjestaj;

    @FXML
    private Button bDnevniIzvjestaj;

    @FXML
    private Label lbNaslov;

    @FXML
    private VBox vbRoot;

    private TableView<SumarniIzvjestaj> tvSumarniIzvjestaj;

    List<DnevniIzvjestaj> dnevniIzvjestaji = new ArrayList<>();

    ObservableList<DnevniIzvjestaj> dnevniIzvjestajObservableList = FXCollections.observableArrayList();
    ObservableList<SumarniIzvjestaj> sumarniIzvjestajObservableList = FXCollections.observableArrayList();

    List<Racun> racuni = Simulator.getRacuni();

    private final DecimalFormat decimalFormat = new DecimalFormat("#.00");

    /**
     * Metoda koja se poziva prilikom inicijalizacije kontrolera.
     * Postavlja vrijednosti za kolone u tabelama i priprema podatke za prikaz.
     */
    @FXML
    public void initialize() {
        tcTerminDnevni.setCellValueFactory(new PropertyValueFactory<>("datum"));
        tcPrihodDnevni.setCellValueFactory(new PropertyValueFactory<>("ukupanPrihod"));
        tcPopustDnevni.setCellValueFactory(new PropertyValueFactory<>("ukupanPopust"));
        tcPromocijaDnevni.setCellValueFactory(new PropertyValueFactory<>("ukupnoPromocije"));
        tcSiriDioDnevni.setCellValueFactory(new PropertyValueFactory<>("ukupanIznosVoznjiUSiremDijelu"));
        tcUziDioDnevni.setCellValueFactory(new PropertyValueFactory<>("ukupanIznosVoznjiUUzemDijelu"));
        tcOdrzavanjeDnevni.setCellValueFactory(new PropertyValueFactory<>("ukupnoOdrzavanje"));
        tcPopravkeDnevni.setCellValueFactory(new PropertyValueFactory<>("ukupnoKvarovi"));

        dnevniIzvjestaji = DnevniIzvjestaj.generisiListu(racuni);

        for(DnevniIzvjestaj dnevniIzvjestaj : dnevniIzvjestaji){
            dnevniIzvjestajObservableList.add(dnevniIzvjestaj);
        }

        if(dnevniIzvjestajObservableList != null){
            tvDnevniIzvjestaj.setItems(dnevniIzvjestajObservableList);
        }

        sumarniIzvjestajObservableList.add(new SumarniIzvjestaj(racuni));

        bSumarniIzvjestaj.setOnAction(actionEvent -> {
            prikaziSumarniIzvjestaj();
        });

        bDnevniIzvjestaj.setOnAction(actionEvent -> {
            prikaziDnevniIzvjestaj();
        });

        bDnevniIzvjestaj.setDisable(true);
        lbNaslov.setText("DNEVNI IZVJEŠTAJ");
    }

    /**
     * Prikazuje dnevni izvještaj u tabeli.
     * Omogućava korisniku pregled detalja dnevnih izvještaja.
     */
    @FXML
    private void prikaziDnevniIzvjestaj() {
        vbRoot.getChildren().remove(tvSumarniIzvjestaj);

        tvDnevniIzvjestaj = new TableView<>();
        tvDnevniIzvjestaj.setPrefHeight(300.0);
        tvDnevniIzvjestaj.setPrefWidth(740.0);

        TableColumn<DnevniIzvjestaj, String> tcDatumDnevni = new TableColumn<>("Termin");
        tcDatumDnevni.setPrefWidth(92.0);
        tcDatumDnevni.setCellValueFactory(new PropertyValueFactory<>("datum"));

        TableColumn<DnevniIzvjestaj, String> tcPrihodDnevni = new TableColumn<>("Prihod");
        tcPrihodDnevni.setPrefWidth(93.0);
        tcPrihodDnevni.setCellValueFactory(new PropertyValueFactory<>("ukupanPrihod"));

        TableColumn<DnevniIzvjestaj, String> tcPopustDnevni = new TableColumn<>("Popust");
        tcPopustDnevni.setPrefWidth(92.0);
        tcPopustDnevni.setCellValueFactory(new PropertyValueFactory<>("ukupanPopust"));

        TableColumn<DnevniIzvjestaj, String> tcPromocijaDnevni = new TableColumn<>("Promocija");
        tcPromocijaDnevni.setPrefWidth(93.0);
        tcPromocijaDnevni.setCellValueFactory(new PropertyValueFactory<>("ukupnoPromocije"));

        TableColumn<DnevniIzvjestaj, String> tcSiriDioDnevni = new TableColumn<>("Širi dio");
        tcSiriDioDnevni.setPrefWidth(92.0);
        tcSiriDioDnevni.setCellValueFactory(new PropertyValueFactory<>("ukupanIznosVoznjiUSiremDijelu"));

        TableColumn<DnevniIzvjestaj, String> tcUziDioDnevni = new TableColumn<>("Uži dio");
        tcUziDioDnevni.setPrefWidth(93.0);
        tcUziDioDnevni.setCellValueFactory(new PropertyValueFactory<>("ukupanIznosVoznjiUUzemDijelu"));

        TableColumn<DnevniIzvjestaj, String> tcOdrzavanjeDnevni = new TableColumn<>("Održavanje");
        tcOdrzavanjeDnevni.setPrefWidth(92.0);
        tcOdrzavanjeDnevni.setCellValueFactory(new PropertyValueFactory<>("ukupnoOdrzavanje"));

        TableColumn<DnevniIzvjestaj, String> tcPopravkeDnevni = new TableColumn<>("Popravke");
        tcPopravkeDnevni.setPrefWidth(92.0);
        tcPopravkeDnevni.setCellValueFactory(new PropertyValueFactory<>("ukupnoKvarovi"));

        tvDnevniIzvjestaj.getColumns().addAll(tcDatumDnevni, tcPrihodDnevni, tcPopustDnevni, tcPromocijaDnevni,
                tcSiriDioDnevni, tcUziDioDnevni, tcOdrzavanjeDnevni,
                tcPopravkeDnevni);

        vbRoot.getChildren().add(tvDnevniIzvjestaj);
        bSumarniIzvjestaj.setDisable(false);
        bDnevniIzvjestaj.setDisable(true);

        if(dnevniIzvjestajObservableList != null){
            tvDnevniIzvjestaj.setItems(dnevniIzvjestajObservableList);
        }
        lbNaslov.setText("DNEVNI IZVJEŠTAJ");
    }

    /**
     * Prikazuje sumarni izvještaj u tabeli.
     * Omogućava korisniku pregled sažetka poslovanja.
     */
    @FXML
    private void prikaziSumarniIzvjestaj() {
        vbRoot.getChildren().remove(tvDnevniIzvjestaj);

        tvSumarniIzvjestaj = new TableView<>();
        tvSumarniIzvjestaj.setPrefHeight(300.0);
        tvSumarniIzvjestaj.setPrefWidth(740.0);

        TableColumn<SumarniIzvjestaj, String> tcPrihodSumarni = new TableColumn<>("Prihod");
        tcPrihodSumarni.setPrefWidth(82.0);
        tcPrihodSumarni.setCellValueFactory(new PropertyValueFactory<>("ukupanPrihod"));

        TableColumn<SumarniIzvjestaj, String> tcPopustSumarni = new TableColumn<>("Popust");
        tcPopustSumarni.setPrefWidth(82.0);
        tcPopustSumarni.setCellValueFactory(new PropertyValueFactory<>("ukupanPopust"));

        TableColumn<SumarniIzvjestaj, String> tcPromocijaSumarni = new TableColumn<>("Promocija");
        tcPromocijaSumarni.setPrefWidth(82.0);
        tcPromocijaSumarni.setCellValueFactory(new PropertyValueFactory<>("ukupnoPromocije"));

        TableColumn<SumarniIzvjestaj, String> tcSiriDioSumarni = new TableColumn<>("Širi dio");
        tcSiriDioSumarni.setPrefWidth(82.0);
        tcSiriDioSumarni.setCellValueFactory(new PropertyValueFactory<>("ukupanIznosVoznjiUSiremDijelu"));

        TableColumn<SumarniIzvjestaj, String> tcUziDioSumarni = new TableColumn<>("Uži dio");
        tcUziDioSumarni.setPrefWidth(83.0);
        tcUziDioSumarni.setCellValueFactory(new PropertyValueFactory<>("ukupanIznosVoznjiUUzemDijelu"));

        TableColumn<SumarniIzvjestaj, String> tcOdrzavanjeSumarni = new TableColumn<>("Održavanje");
        tcOdrzavanjeSumarni.setPrefWidth(82.0);
        tcOdrzavanjeSumarni.setCellValueFactory(new PropertyValueFactory<>("ukupnoOdrzavanje"));

        TableColumn<SumarniIzvjestaj, String> tcPopravkeSumarni = new TableColumn<>("Popravke");
        tcPopravkeSumarni.setPrefWidth(82.0);
        tcPopravkeSumarni.setCellValueFactory(new PropertyValueFactory<>("ukupnoKvarovi"));

        TableColumn<SumarniIzvjestaj, String> tcTroskoviSumarni = new TableColumn<>("Troškovi");
        tcTroskoviSumarni.setPrefWidth(82.0);
        tcTroskoviSumarni.setCellValueFactory(new PropertyValueFactory<>("ukupniTroskovi"));

        TableColumn<SumarniIzvjestaj, String> tcPorezSumarni = new TableColumn<>("Porez");
        tcPorezSumarni.setPrefWidth(82.0);
        tcPorezSumarni.setCellValueFactory(new PropertyValueFactory<>("ukupanPorez"));

        tvSumarniIzvjestaj.getColumns().addAll(tcPrihodSumarni, tcPopustSumarni, tcPromocijaSumarni,
                tcSiriDioSumarni, tcUziDioSumarni, tcOdrzavanjeSumarni,
                tcPopravkeSumarni, tcTroskoviSumarni, tcPorezSumarni);

        vbRoot.getChildren().add(tvSumarniIzvjestaj);
        bSumarniIzvjestaj.setDisable(true);
        bDnevniIzvjestaj.setDisable(false);

        if(sumarniIzvjestajObservableList != null) {
            tvSumarniIzvjestaj.setItems(sumarniIzvjestajObservableList);
        }

        lbNaslov.setText("SUMARNI IZVJEŠTAJ");
    }
}
