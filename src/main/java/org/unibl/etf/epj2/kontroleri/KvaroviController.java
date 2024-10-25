package org.unibl.etf.epj2.kontroleri;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.unibl.etf.epj2.model.Kvar;
import org.unibl.etf.epj2.simulacija.Simulator;

import java.util.ArrayList;
import java.util.List;

/**
 * Kontroler za prikazivanje informacija o kvarovima u tabeli.
 * Koristi se u JavaFX aplikaciji za upravljanje tabelom prikaza kvarova na prevoznim sredstvima.
 */
public class KvaroviController {

    @FXML
    private TableView<Kvar> tvKvarovi;

    @FXML
    private TableColumn<Kvar, String> prevoznoSredstvo;

    @FXML
    private TableColumn<Kvar, String> identifikator;

    @FXML
    private TableColumn<Kvar, String> vrijeme;

    @FXML
    private TableColumn<Kvar, String> opis;

    List<Kvar> kvaroviSimulator;

    ObservableList<Kvar> kvarovi = FXCollections.observableArrayList();

    /**
     * Metoda koja se automatski poziva pri inicijalizaciji kontrolera.
     * Inicijalizuje TableView sa podacima o kvarovima.
     */
    @FXML
    public void initialize() {
        prevoznoSredstvo.setCellValueFactory(new PropertyValueFactory<>("vozilo"));
        identifikator.setCellValueFactory(new PropertyValueFactory<>("id"));
        vrijeme.setCellValueFactory(new PropertyValueFactory<>("vrijemeIDatum"));
        opis.setCellValueFactory(new PropertyValueFactory<>("opis"));

        kvaroviSimulator = Simulator.kvarovi;

        for(Kvar kvar : kvaroviSimulator) {
            kvarovi.add(kvar);
        }

        if(kvarovi != null) {
            tvKvarovi.setItems(kvarovi);
        }
    }
}
