module org.unibl.etf.epj2 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.xml;


    opens org.unibl.etf.epj2.kontroleri to javafx.fxml;
    opens org.unibl.etf.epj2.model to javafx.base;
    opens org.unibl.etf.epj2.izvjestaji to javafx.base;
    exports org.unibl.etf.epj2;
}