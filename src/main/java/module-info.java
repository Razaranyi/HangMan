module com.example.q2_mmn13 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens com.example.q2_mmn13 to javafx.fxml;
    exports com.example.q2_mmn13;
}