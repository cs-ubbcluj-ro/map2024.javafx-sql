package com.example.curs9javafx;

import com.example.repository.SQLSquareRepository;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {


        SQLSquareRepository repository = new SQLSquareRepository();

        // 1. Adaugam date in SQL
//            repository.add(UnitSquare.getInstance());
//            repository.add(new Square(100, "my square", 3));
//            repository.add(new Square(101, "you're square", 4));


        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 640, 480);

        HelloController controller = fxmlLoader.getController();
        controller.init();
        controller.setRepository(repository);


        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();


    }

    public static void main(String[] args) {
        launch();
    }
}