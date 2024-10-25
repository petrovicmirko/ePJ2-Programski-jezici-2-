package org.unibl.etf.epj2;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/FXMLFajlovi/glavniProzor.fxml"));
        Scene scene = new Scene(root, 900,700);
        stage.setScene(scene);
        stage.setTitle("Simulacija kretanja vozila");
        stage.setResizable(false);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}