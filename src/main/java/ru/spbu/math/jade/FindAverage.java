package ru.spbu.math.jade;

import jade.wrapper.StaleProxyException;
import ru.spbu.math.jade.AgentData.Destination;
import ru.spbu.math.jade.EdgeType;
import jade.core.AID;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;

import java.text.DecimalFormat;
import java.time.Instant;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class FindAverage extends TickerBehaviour {

    private final MyAgent agent;
    private int maxStep;
    private int step = 0;
    private State state = State.SEND;

    public FindAverage(MyAgent agent, long period, int maxStep) {
        super(agent, period);
        this.setFixedPeriod(true);
        this.agent = agent;
        this.maxStep = maxStep;
    }

    @Override
    protected void onTick() {
        step++;

        switch (state) {
            case SEND:
                send();
                state = State.RECEIVE;
                break;
            case RECEIVE:
                receive();
                state = State.SEND;
                break;
            case DISPOSE:
                dispose();
                break;
            default:
                block();
        }
    }

    private void send() {
        double rnd;
        ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
        boolean missed = false;
        for (Map.Entry<String, Destination> entry : agent.agentData.neighbors.entrySet()) {
            switch (entry.getValue().defect) {

                case MISSING:
                    rnd = Math.random();
                    if (rnd > agent.agentData.defaultProb) {
                        missed = true;
                        break;
                    }
                case DELAYED:
                    rnd = Math.random();
                    if (rnd < agent.agentData.defaultProb) {
                        try {
                            TimeUnit.MILLISECONDS.sleep(agent.agentData.delay);
                        } catch (InterruptedException e) {
                            System.out.println("Interrupted exception");
                        }
                        break;
                    }
                case NONE:
                    break;
            }

            if (missed) {
                missed = false;
                continue;
            }

            msg.addReceiver(new AID(entry.getKey(), AID.ISLOCALNAME));
        }
        double noisyNumber = agent.getStoredNumber() + (Math.random() * 0.2 - 0.1);
        msg.setContent(String.valueOf(noisyNumber));
        agent.send(msg);
    }

    private void receive() {
        double res = 0;
        Set<String> processed = new HashSet<>();
        double agentNumber = agent.getStoredNumber();
        while ((agent.receive()) != null) {
            ACLMessage msg = agent.receive();
            if (msg != null) {
                if (processed.isEmpty() || !processed.contains(msg.getSender().getLocalName())) {
                    double numberReceived = Double.parseDouble(msg.getContent());

                    System.out.println(step + ") " +"Agent:" + agent.getAID().getLocalName()
                            +" Received " + numberReceived);

                    res += numberReceived - agentNumber;
                    processed.add(msg.getSender().getLocalName());

                }
                else{
                    if (processed.size()==agent.agentData.neighbors.size()){
                        break;
                    }
                }
            }
        }
        agent.setStoredNumber(agentNumber + agent.agentData.defaultAlpha * res);

        if (step >= this.maxStep) {
            dispose();
        }
    }

    private void dispose() {
        String name = agent.getAID().getLocalName();

        DecimalFormat df = new DecimalFormat("#.######");
        System.out.println(step + ") " + "Agent: " + name
                + " calculated average : " + df.format(agent.getStoredNumber()));

        jade.wrapper.AgentContainer container = agent.getContainerController();

        agent.doDelete();

        new Thread(() -> {
            try {
                container.kill();
            } catch (StaleProxyException ignored) {
            }
        }).start();

    }
}
