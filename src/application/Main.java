package application;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class Main extends Application {

    private Graph graph;
    private MapPane mapPane;

    private ComboBox<City> sourceComboBox;
    private ComboBox<City> targetComboBox;

    private TextArea pathArea;
    private TextField distanceField;

    private TextField cityNameField;
    private TextField latitudeField;
    private TextField longitudeField;
    private ComboBox<City> neighborComboBox;

    private final String FILE_PATH = "data/gaza_map.txt";
    @Override
    public void start(Stage stage) {
        try {
            graph = new Graph();
            graph.loadFromFile(FILE_PATH);

            BorderPane root = new BorderPane();

            mapPane = new MapPane(graph, "file:images/gaza_map.png");            root.setCenter(mapPane);

            VBox controlPanel = createControlPanel();
            root.setRight(controlPanel);

            Scene scene = new Scene(root, 1050, 720);

            stage.setTitle("Dijkstra Gaza Map");
            stage.setScene(scene);
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", e.toString());
        }
    }

    private VBox createControlPanel() {
        VBox box = new VBox(10);
        box.setPadding(new Insets(15));
        box.setPrefWidth(330);

        Label sourceLabel = new Label("Source:");
        sourceComboBox = new ComboBox<>();
        sourceComboBox.setItems(FXCollections.observableArrayList(graph.getCities()));

        Label targetLabel = new Label("Target:");
        targetComboBox = new ComboBox<>();
        targetComboBox.setItems(FXCollections.observableArrayList(graph.getCities()));

        Button runButton = new Button("Run Dijkstra");
        runButton.setMaxWidth(Double.MAX_VALUE);
        runButton.setOnAction(e -> runDijkstra());

        Label pathLabel = new Label("Path:");
        pathArea = new TextArea();
        pathArea.setPrefHeight(120);
        pathArea.setEditable(false);

        Label distanceLabel = new Label("Distance:");
        distanceField = new TextField();
        distanceField.setEditable(false);

        Separator separator = new Separator();

        Label addCityLabel = new Label("Add New City:");

        cityNameField = new TextField();
        cityNameField.setPromptText("City Name");

        latitudeField = new TextField();
        latitudeField.setPromptText("Latitude");

        longitudeField = new TextField();
        longitudeField.setPromptText("Longitude");

        neighborComboBox = new ComboBox<>();
        neighborComboBox.setPromptText("Neighbor City");
        neighborComboBox.setItems(FXCollections.observableArrayList(graph.getCities()));

        Button addButton = new Button("Add City");
        addButton.setMaxWidth(Double.MAX_VALUE);
        addButton.setOnAction(e -> addNewCity());

        box.getChildren().addAll(
                sourceLabel,
                sourceComboBox,
                targetLabel,
                targetComboBox,
                runButton,
                pathLabel,
                pathArea,
                distanceLabel,
                distanceField,
                separator,
                addCityLabel,
                cityNameField,
                latitudeField,
                longitudeField,
                neighborComboBox,
                addButton
        );

        return box;
    }

    private void runDijkstra() {
        City source = sourceComboBox.getValue();
        City target = targetComboBox.getValue();

        if (source == null || target == null) {
            showAlert("Warning", "Please select source and target cities.");
            return;
        }

        Dijkstra.Result result = Dijkstra.shortestPath(graph, source, target);

        if (result.getPath().isEmpty()) {
            pathArea.setText("No path found.");
            distanceField.setText("");
            return;
        }

        StringBuilder pathText = new StringBuilder();

        for (int i = 0; i < result.getPath().size(); i++) {
            pathText.append(result.getPath().get(i).getName());

            if (i != result.getPath().size() - 1) {
                pathText.append(" -> ");
            }
        }

        pathArea.setText(pathText.toString());
        distanceField.setText(String.format("%.2f km", result.getDistance()));

        mapPane.drawPath(result.getPath());
    }

    private void addNewCity() {
        try {
            String name = cityNameField.getText().trim();
            double lat = Double.parseDouble(latitudeField.getText().trim());
            double lon = Double.parseDouble(longitudeField.getText().trim());
            City neighbor = neighborComboBox.getValue();

            if (name.isEmpty()) {
                showAlert("Warning", "City name is required.");
                return;
            }

            if (graph.containsCity(name)) {
                showAlert("Warning", "City already exists.");
                return;
            }

            if (neighbor == null) {
                showAlert("Warning", "Please select neighbor city.");
                return;
            }

            City newCity = new City(name, lat, lon);
            graph.addCity(newCity);

            graph.addDirectedEdge(name, neighbor.getName());
            graph.addDirectedEdge(neighbor.getName(), name);

            graph.saveToFile(FILE_PATH);

            refreshComboBoxes();
            mapPane.drawCities();

            cityNameField.clear();
            latitudeField.clear();
            longitudeField.clear();

            showAlert("Success", "City added successfully.");

        } catch (NumberFormatException e) {
            showAlert("Error", "Latitude and Longitude must be numbers.");
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", e.toString());
        }
    }

    private void refreshComboBoxes() {
        sourceComboBox.setItems(FXCollections.observableArrayList(graph.getCities()));
        targetComboBox.setItems(FXCollections.observableArrayList(graph.getCities()));
        neighborComboBox.setItems(FXCollections.observableArrayList(graph.getCities()));
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}