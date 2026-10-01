package application;

public class Edge {

    private City from;
    private City to;
    private double distance;

    public Edge(City from, City to) {
        this.from = from;
        this.to = to;
        this.distance = calculateDistance(from, to);
    }

    private double calculateDistance(City c1, City c2) {
        final double R = 6371.0;

        double lat1 = Math.toRadians(c1.getLatitude());
        double lat2 = Math.toRadians(c2.getLatitude());

        double dLat = Math.toRadians(c2.getLatitude() - c1.getLatitude());
        double dLon = Math.toRadians(c2.getLongitude() - c1.getLongitude());

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(lat1) * Math.cos(lat2)
                * Math.sin(dLon / 2) * Math.sin(dLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return R * c;
    }

    public City getFrom() {
        return from;
    }

    public City getTo() {
        return to;
    }

    public double getDistance() {
        return distance;
    }
}
