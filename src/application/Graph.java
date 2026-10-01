package application;

import java.io.*;
import java.util.*;

public class Graph {

    private Map<String, City> cities;
    private Map<City, ArrayList<Edge>> adjacencyList;

    public Graph() {
        cities = new LinkedHashMap<>();
        adjacencyList = new HashMap<>();
    }

    public void loadFromFile(String filePath) throws IOException {
        cities.clear();
        adjacencyList.clear();

        Scanner scanner = new Scanner(new File(filePath));

        int numberOfCities = scanner.nextInt();
        int numberOfEdges = scanner.nextInt();

        for (int i = 0; i < numberOfCities; i++) {
            String name = scanner.next();
            double latitude = scanner.nextDouble();
            double longitude = scanner.nextDouble();

            City city = new City(name, latitude, longitude);
            addCity(city);
        }

        for (int i = 0; i < numberOfEdges; i++) {
            String from = scanner.next();
            String to = scanner.next();

            addDirectedEdge(from, to);
        }

        scanner.close();
    }

    public void saveToFile(String filePath) throws IOException {
        PrintWriter writer = new PrintWriter(new FileWriter(filePath));

        int numberOfCities = cities.size();
        int numberOfEdges = 0;

        for (City city : adjacencyList.keySet()) {
            numberOfEdges += adjacencyList.get(city).size();
        }

        writer.println(numberOfCities + " " + numberOfEdges);

        for (City city : cities.values()) {
            writer.println(city.getName() + " " + city.getLatitude() + " " + city.getLongitude());
        }

        for (City city : cities.values()) {
            for (Edge edge : adjacencyList.get(city)) {
                writer.println(edge.getFrom().getName() + " " + edge.getTo().getName());
            }
        }

        writer.close();
    }

    public void addCity(City city) {
        cities.put(city.getName(), city);
        adjacencyList.put(city, new ArrayList<>());
    }

    public boolean containsCity(String cityName) {
        return cities.containsKey(cityName);
    }

    public void addDirectedEdge(String fromName, String toName) {
        City from = cities.get(fromName);
        City to = cities.get(toName);

        if (from == null || to == null) {
            return;
        }

        adjacencyList.get(from).add(new Edge(from, to));
    }

    public City getCity(String name) {
        return cities.get(name);
    }

    public Collection<City> getCities() {
        return cities.values();
    }

    public ArrayList<Edge> getNeighbors(City city) {
        return adjacencyList.get(city);
    }
}
