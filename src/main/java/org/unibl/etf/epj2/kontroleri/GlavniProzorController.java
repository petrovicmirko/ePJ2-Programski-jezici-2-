package org.unibl.etf.epj2.kontroleri;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.unibl.etf.epj2.simulacija.Simulator;

import java.io.IOException;

/**
 * Kontroler za glavni prozor aplikacije koji rukovodi prikazom i interakcijom sa glavnim korisničkim interfejsom.
 * Ova klasa sadrži logiku za inicijalizaciju simulacije, upravljanje različitim prikazima i ažuriranje GUI elemenata.
 */
public class GlavniProzorController {
    private static final int BROJ_KOLONA = 20;
    private static final int BROJ_REDOVA = 20;

    public static GridPane gridPane;

    public static Simulator simulator;

    @FXML
    private VBox vbGrid;

    @FXML
    private Button bPokreniSimulaciju;

    @FXML
    private Button bVozila;

    @FXML
    private Button bKvarovi;

    @FXML
    private Button bRezultatiPoslovanja;

    @FXML
    private Button bNajveciPrihodi;

    @FXML
    private Label lbDatumIVrijeme;

    public static Button prikaziVozila;
    public static Button prikaziKvarove;
    public static Button prikaziRezultatePoslovanja;
    public static Button prikaziNajvecePrihode;

    /**
     * Metoda koja se poziva prilikom klika na dugme "Pokreni Simulaciju".
     * Inicijalizuje simulaciju, generiše mrežu i ažurira korisnički interfejs.
     */
    @FXML
    void pokreniSimulaciju() {
        gridPane = generisiMapu();
        VBox.setVgrow(gridPane, Priority.ALWAYS);
        vbGrid.getChildren().add(gridPane);

        simulator = new Simulator();
        simulator.start();
        bPokreniSimulaciju.setDisable(true);

        prikaziVozila = bVozila;
        prikaziKvarove = bKvarovi;
        prikaziRezultatePoslovanja = bRezultatiPoslovanja;
        prikaziNajvecePrihode = bNajveciPrihodi;

        Timeline timeline = new Timeline(
                new KeyFrame(Duration.millis(50), event -> {
                    lbDatumIVrijeme.setText(Simulator.vrijemeIDatum);
                })
        );
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();

        bVozila.setOnAction(actionEvent -> {
            try {
                prikaziVozila();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        bKvarovi.setOnAction(actionEvent -> {
            try {
                prikaziKvarove();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        bRezultatiPoslovanja.setOnAction(actionEvent -> {
            try {
                prikaziRezultatePoslovanja();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        bNajveciPrihodi.setOnAction(actionEvent -> {
            try {
                prikaziNajvecePrihode();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

    }

    /**
     * Prikazuje novi prozor sa listom vozila.
     *
     * @throws IOException ako se desi greška prilikom učitavanja FXML fajla.
     */
    @FXML
    private void prikaziVozila() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXMLFajlovi/vozila.fxml"));

        Stage stage = new Stage();
        stage.setTitle("Vozila");
        stage.setScene(new Scene(loader.load(), 740, 415));
        stage.show();
    }

    /**
     * Prikazuje novi prozor sa listom kvarova.
     *
     * @throws IOException ako se desi greška prilikom učitavanja FXML fajla.
     */
    @FXML
    private void prikaziKvarove() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXMLFajlovi/kvarovi.fxml"));

        Stage stage = new Stage();
        stage.setTitle("Kvarovi");
        stage.setScene(new Scene(loader.load(), 740, 415));
        stage.show();
    }

    /**
     * Prikazuje novi prozor sa rezultatima poslovanja.
     *
     * @throws IOException ako se desi greška prilikom učitavanja FXML fajla.
     */
    @FXML
    private void prikaziRezultatePoslovanja() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXMLFajlovi/rezultatiPoslovanja.fxml"));

        Stage stage = new Stage();
        stage.setTitle("Rezultati poslovanja");
        stage.setScene(new Scene(loader.load(), 740, 415));
        stage.show();
    }

    /**
     * Prikazuje novi prozor sa listom najvećih prihoda.
     *
     * @throws IOException ako se desi greška prilikom učitavanja FXML fajla.
     */
    @FXML
    private void prikaziNajvecePrihode() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXMLFajlovi/najveciPrihodi.fxml"));

        Stage stage = new Stage();
        stage.setTitle("Najveći prihodi");
        stage.setScene(new Scene(loader.load(), 740, 415));
        stage.show();
    }

    /**
     * Generiše mrežu (GridPane) za simulaciju.
     *
     * @return generisani GridPane sa podešenim kvadratima i labelama.
     */
    private GridPane generisiMapu(){
        GridPane grid = new GridPane();
        for(int i = 0; i < BROJ_REDOVA; i++){
            for(int j = 0; j < BROJ_KOLONA; j++){
                Rectangle rectangle = new Rectangle();
                rectangle.setWidth(40.0);
                rectangle.setHeight(25.0);

                if(i > 4 && i < 15 && j > 4 && j < 15){
                    rectangle.setFill(Color.ROYALBLUE);
                }else {
                    rectangle.setFill(Color.WHITE);
                }

                Label idLabel = new Label();
                Label nivoBaterijeLabel = new Label();

                idLabel.setText("");
                nivoBaterijeLabel.setText("");

                idLabel.setStyle("-fx-text-size: 2;");
                //idLabel.setStyle("-fx-font-weight: bold;");
                nivoBaterijeLabel.setStyle("-fx-text-size: 2;");
                //nivoBaterijeLabel.setStyle("-fx-font-weight: bold;");

                StackPane stackPane = new StackPane();
                stackPane.getChildren().addAll(rectangle, idLabel,nivoBaterijeLabel);

                GridPane.setRowIndex(stackPane, i);
                GridPane.setColumnIndex(stackPane, j);

                grid.getChildren().add(stackPane);

            }
        }
        grid.setGridLinesVisible(true);

        return grid;
    }
}
