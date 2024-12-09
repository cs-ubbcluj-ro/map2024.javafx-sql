module com.example.curs9javafx {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.xerial.sqlitejdbc;


    opens com.example.curs9javafx to javafx.fxml;
    exports com.example.curs9javafx;
    exports com.example.repository;
    exports com.example.domain;
}