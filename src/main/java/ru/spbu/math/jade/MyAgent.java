package ru.spbu.math.jade;

import jade.core.Agent;
import ru.spbu.math.jade.FindAverage;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collector;


public class MyAgent extends Agent {

    AgentData agentData;
    private static final int maxStep = 1000;

    public Double getStoredNumber() {
        return this.agentData.storedNumber;
    }

    public void setStoredNumber(double numberToStore) {
        this.agentData.storedNumber = numberToStore;
    }

    @Override
    protected void setup() {
        agentData = (AgentData) getArguments()[0];

        addBehaviour(new FindAverage(this, TimeUnit.SECONDS.toMillis(1), maxStep));
    }
}

