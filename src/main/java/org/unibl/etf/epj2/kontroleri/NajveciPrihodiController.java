package org.unibl.etf.epj2.kontroleri;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import org.unibl.etf.epj2.izvjestaji.MaxPrihodPoVozilu;
import org.unibl.etf.epj2.model.Automobil;
import org.unibl.etf.epj2.model.Bicikl;
import org.unibl.etf.epj2.model.Trotinet;

import java.util.Map;

/**
 * Kontroler za prikazivanje informacija o najvećim prihodima po vozilu u tabelama.
 * Koristi se u JavaFX aplikaciji za upravljanje tabelama prikaza prihoda za automobile, bicikle i trotinete.
 */
public class NajveciPrihodiController {

    @FXML
    TableView<Map.Entry<Automobil, Double>> tvAutomobili;

    @FXML
    TableView<Map.Entry<Bicikl, Double>> tvBicikli;

    @FXML
    TableView<Map.Entry<Trotinet, Double>> tvTrotineti;

    @FXML
    TableColumn<Map.Entry<Automobil, Double>, String> tcIdAutomobila;

    @FXML
    TableColumn<Map.Entry<Automobil, Double>, Double> tcPrihodAutomobila;

    @FXML
    TableColumn<Map.Entry<Bicikl, Double>, String> tcIdBicikla;

    @FXML
    TableColumn<Map.Entry<Bicikl, Double>, Double> tcPrihodBicikla;

    @FXML
    TableColumn<Map.Entry<Trotinet, Double>, String> tcIdTrotineta;

    @FXML
    TableColumn<Map.Entry<Trotinet, Double>, Double> tcPrihodTrotineta;

    ObservableList<Map.Entry<Automobil, Double>> automobilList;
    ObservableList<Map.Entry<Bicikl, Double>> bicikliList;
    ObservableList<Map.Entry<Trotinet, Double>> trotinetiList;

    /**
     * Metoda koja se automatski poziva pri inicijalizaciji kontrolera.
     * Inicijalizuje TableView sa podacima o prihodima za automobile, bicikle i trotinete.
     */
    public void initialize() {
        tcIdAutomobila.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getKey().getId())
        );
        tcPrihodAutomobila.setCellValueFactory(cellData ->
                new SimpleDoubleProperty(cellData.getValue().getValue()).asObject()
        );

        tcIdBicikla.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getKey().getId())
        );
        tcPrihodBicikla.setCellValueFactory(cellData ->
                new SimpleDoubleProperty(cellData.getValue().getValue()).asObject()
        );

        tcIdTrotineta.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getKey().getId())
        );
        tcPrihodTrotineta.setCellValueFactory(cellData ->
                new SimpleDoubleProperty(cellData.getValue().getValue()).asObject()
        );


        automobilList = FXCollections.observableArrayList(MaxPrihodPoVozilu.deserijalizujAutomobil().entrySet());
        bicikliList = FXCollections.observableArrayList(MaxPrihodPoVozilu.deserijalizujBicikl().entrySet());
        trotinetiList = FXCollections.observableArrayList(MaxPrihodPoVozilu.deserijalizujTrotinet().entrySet());

        tvAutomobili.setItems(automobilList);
        tvBicikli.setItems(bicikliList);
        tvTrotineti.setItems(trotinetiList);
    }
}
