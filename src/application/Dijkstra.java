package application;

import java.util.*;

public class Dijkstra {

    public static class Result {

        private ArrayList<City> path;
        private double distance;

        public Result(ArrayList<City> path, double distance) {
            this.path = path;
            this.distance = distance;
        }

        public ArrayList<City> getPath() {
            return path;
        }

        public double getDistance() {
            return distance;
        }
    }

    public static Result shortestPath(Graph graph, City source, City target) {
        Map<City, Double> distance = new HashMap<>();
        Map<City, City> previous = new HashMap<>();
        HashSet<City> visited = new HashSet<>();

        for (City city : graph.getCities()) {
            distance.put(city, Double.MAX_VALUE);
        }

        distance.put(source, 0.0);

        PriorityQueue<City> pq = new PriorityQueue<>(
                Comparator.comparingDouble(distance::get)
        );

        pq.add(source);

        while (!pq.isEmpty()) {
            City current = pq.poll();

            if (visited.contains(current)) {
                continue;
            }

            visited.add(current);

            if (current == target) {
                break;
            }

            for (Edge edge : graph.getNeighbors(current)) {
                City neighbor = edge.getTo();

                if (visited.contains(neighbor)) {
                    continue;
                }

                double newDistance = distance.get(current) + edge.getDistance();

                if (newDistance < distance.get(neighbor)) {
                    distance.put(neighbor, newDistance);
                    previous.put(neighbor, current);
                    pq.add(neighbor);
                }
            }
        }

        ArrayList<City> path = new ArrayList<>();

        if (!source.equals(target) && !previous.containsKey(target)) {
            return new Result(path, Double.MAX_VALUE);
        }

        City step = target;

        while (step != null) {
            path.add(0, step);
            step = previous.get(step);
        }

        return new Result(path, distance.get(target));
    }
}