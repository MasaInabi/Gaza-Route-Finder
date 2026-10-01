package application;

public class City {

    private String name;
    private double latitude;
    private double longitude;

    private double x;
    private double y;

    public City(String name, double latitude, double longitude) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getName() {
        return name;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public void setScreenPosition(double x, double y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        return name;
    }
}
