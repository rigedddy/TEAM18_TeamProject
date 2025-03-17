module com.example.myjavafx {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.myjavafx to javafx.fxml;
    exports com.example.myjavafx;
}