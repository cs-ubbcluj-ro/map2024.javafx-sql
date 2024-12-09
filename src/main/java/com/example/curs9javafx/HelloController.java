package com.example.curs9javafx;

import com.example.domain.Square;
import com.example.repository.RepositoryException;
import com.example.repository.SQLSquareRepository;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.ArrayList;

public class HelloController {
    public ListView<Square> listSquares;
    public Label labelId;
    public TextField textFieldId;
    public Label labelName;
    public TextField textFieldName;
    public Label labelWidth;
    public TextField textFieldWidth;
    public Button buttonAdd;
    public TableView<Square> tableSquares;
    public Label labelMinWidth;
    public Slider sliderWidth;
    public Label labelMaxWidth;
    @FXML
    private Label welcomeText;

    /**
     * Asta e o lista Java definita in JavaFX
     * Ce stie in plus fata de java.util.ArrayList?
     * Stie ca atunci cand se modifica continutul listei, sa notifice componenta grafica
     * pe care e utilizata
     * <p>
     * ObservableLis implementeaza sablonul de proiectare Observer
     * https://refactoring.guru/design-patterns/observer
     */
    private ObservableList<Square> dataSquares = FXCollections.observableList(new ArrayList<>());

    private SQLSquareRepository repository;


    public void init() {
        listSquares.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Square>() {
            @Override
            public void changed(ObservableValue<? extends Square> observableValue, Square square, Square t1) {
                ObservableList<Square> changes = listSquares.getSelectionModel().getSelectedItems();
                Square selectedSquare = changes.get(0);

                textFieldId.setText(Integer.toString(selectedSquare.getId()));
                textFieldName.setText(selectedSquare.getName());
                textFieldWidth.setText(Integer.toString(selectedSquare.getWidth()));

            }
        });

        TableColumn<Square, String> columnId = new TableColumn<>("Square Id");
        columnId.setCellValueFactory(square -> new SimpleStringProperty(Integer.toString(square.getValue().getId())));

        TableColumn<Square, String> columnName = new TableColumn<>("Name");
        columnName.setCellValueFactory(square -> new SimpleStringProperty(square.getValue().getName()));

        TableColumn<Square, String> columnWidth = new TableColumn<>("Width");
        columnWidth.setCellValueFactory(square -> new SimpleStringProperty(Integer.toString(square.getValue().getWidth())));

        tableSquares.getColumns().add(columnId);
        tableSquares.getColumns().add(columnName);
        tableSquares.getColumns().add(columnWidth);

        sliderWidth.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                var sliderValue = observableValue.getValue().intValue();

                // TODO De inclus scalarea dintre valorile pe slider si
                // latimea patratelor din lista/tabel
                var listKeepSquares = new ArrayList<Square>();
                for (var square : repository.getAll()) {
                    if (square.getWidth() <= sliderValue) {
                        listKeepSquares.add(square);
                    }
                }
                dataSquares.clear();
                dataSquares.addAll(listKeepSquares);
            }
        });

    }

    public void setRepository(SQLSquareRepository repository) {
        this.repository = repository;
        listSquares.setItems(dataSquares);
        tableSquares.setItems(dataSquares);

        dataSquares.addAll(repository.getAll());

        if (repository.getAll().isEmpty()) {
            labelMinWidth.setText("n/a");
            labelMaxWidth.setText("n/a");
            sliderWidth.setDisable(true);
        } else {
            var minWidth = Integer.MAX_VALUE;
            var maxWidth = Integer.MIN_VALUE;

            for (var square : repository.getAll()) {
                minWidth = Math.min(minWidth, square.getWidth());
                maxWidth = Math.max(maxWidth, square.getWidth());
            }
            labelMinWidth.setText(Integer.toString(minWidth));
            labelMaxWidth.setText(Integer.toString(maxWidth));
        }
    }

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }

    public void onAddButtonClick(ActionEvent actionEvent) {

//        try {
//            Thread.sleep(10000);
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }

        try {
            // TODO Daca id, width sunt < 0
            var squareId = Integer.parseInt(textFieldId.getText());
            var squareName = textFieldName.getText();
            var squareWidth = Integer.parseInt(textFieldWidth.getText());
            Square square = new Square(squareId, squareName, squareWidth);
            repository.add(square);
            dataSquares.add(square);
        } catch (NumberFormatException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK).show();
        } catch (RepositoryException e) {
            Alert hopa = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
            hopa.show();
        } finally {
            textFieldId.clear();
            textFieldName.clear();
            textFieldWidth.clear();
        }
    }
}