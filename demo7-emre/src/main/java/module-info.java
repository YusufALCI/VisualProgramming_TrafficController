module com.example.demo7emre {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.tr.edu.erciyes.bm.group25321 to javafx.fxml;
    exports com.example.tr.edu.erciyes.bm.group25321;
}