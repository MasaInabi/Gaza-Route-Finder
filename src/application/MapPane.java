package application;

import javafx.scene.layout.Pane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;

import java.util.ArrayList;

public class MapPane extends Pane {

    private Graph graph;
    private ImageView mapImageView;

    private double mapWidth = 700;
    private double mapHeight = 700;

    private double minLat = 31.20;
    private double maxLat = 31.60;
    private double minLon = 34.20;
    private double maxLon = 34.60;

    public MapPane(Graph graph, String imagePath) {
        this.graph = graph;

        Image image = new Image(imagePath);
        mapImageView = new ImageView(image);

        mapImageView.setFitWidth(mapWidth);
        mapImageView.setFitHeight(mapHeight);

        getChildren().add(mapImageView);

        drawCities();
    }

    public void drawCities() {
        getChildren().removeIf(node -> node instanceof Circle || node instanceof Text || node instanceof Line);

        for (City city : graph.getCities()) {
            double x = convertLongitudeToX(city.getLongitude());
            double y = convertLatitudeToY(city.getLatitude());

            city.setScreenPosition(x, y);

            Circle circle = new Circle(x, y, 5);
            circle.setFill(Color.RED);

            Text text = new Text(x + 7, y - 7, city.getName());
            text.setFill(Color.BLACK);
            text.setStyle("-fx-font-size: 11px;");

            getChildren().addAll(circle, text);
        }
    }

    public void drawPath(ArrayList<City> path) {
        getChildren().removeIf(node -> node instanceof Line);

        if (path == null || path.size() < 2) {
            return;
        }

        for (int i = 0; i < path.size() - 1; i++) {
            City from = path.get(i);
            City to = path.get(i + 1);

            Line line = new Line(from.getX(), from.getY(), to.getX(), to.getY());
            line.setStroke(Color.BLUE);
            line.setStrokeWidth(4);

            getChildren().add(line);
        }
    }

    private double convertLongitudeToX(double lon) {
        return ((lon - minLon) / (maxLon - minLon)) * mapWidth;
    }

    private double convertLatitudeToY(double lat) {
        return ((maxLat - lat) / (maxLat - minLat)) * mapHeight;
    }
}