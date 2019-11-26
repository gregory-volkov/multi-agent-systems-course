package ru.spbu.math.jade;

import java.util.HashMap;
import ru.spbu.math.jade.EdgeType;


public class AgentData {
    public class Destination {
        EdgeType defect;
        String destination;
        Double probability;

        Destination(EdgeType defect, String destination) {
            this.destination = destination;
            this.defect = defect;
        }

        Destination(EdgeType defect, String destination, Double probability) {
            this.destination = destination;
            this.defect = defect;
            this.probability = probability;
        }
    }

    public Double storedNumber;
    final double defaultProb = 0.3;
    final double defaultAlpha = 0.2;
    final long delay = 5;
    public HashMap<String, Destination> neighbors;

    public AgentData(Integer[] destinationIds, Integer storedNumber, boolean addMissing, boolean addDelayed) {
        neighbors = new HashMap<>();
        this.storedNumber = (double) storedNumber;
        String destination;
        for (Integer destinationId : destinationIds) {
            destination = destinationId.toString();
            neighbors.put(destination, new Destination(EdgeType.NONE, destination));
        }

        if (addMissing) {
            destination = destinationIds[0].toString();
            neighbors.put(destination, new Destination(EdgeType.MISSING, destination, defaultProb));
        }

        if (addDelayed) {
            destination = destinationIds[1].toString();
            neighbors.put(destination, new Destination(EdgeType.DELAYED, destination, defaultProb));
        }
    }
}
