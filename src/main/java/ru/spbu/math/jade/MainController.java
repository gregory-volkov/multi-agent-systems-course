package ru.spbu.math.jade;

import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;

import java.util.ArrayList;
import java.util.Arrays;

class MainController {

    private static final Integer[][] adjacencyLists = {
            {5, 2, 4, 8},
            {15, 1, 3, 6},
            {10, 1, 4, 5},
            {3, 1, 3, 8},
            {2, 4, 3, 7, 10},
            {10, 3, 2, 1},
            {10, 6, 2, 10},
            {5, 6, 7, 5, 4},
            {8, 1, 6, 4, 3},
            {10, 9, 7, 5}
    };

//    private static final ArrayList<String []> adjencyLists = new ArrayList<>(Arrays.asList(
//            new String[]{"5", "2", "4", "8"},
//            new String[]{"15", "1", "3", "6"},
//            new String[]{"10", "1", "4", "5"},
//            new String[]{"3", "1", "3", "8"},
//            new String[]{"2", "4", "3", "7", "10"},
//            new String[]{"10", "3", "2", "1"},
//            new String[]{"10", "6", "2", "10"},
//            new String[]{"5", "6", "7", "5", "4"},
//            new String[]{"8", "1", "6", "4", "3"},
//            new String[]{"10", "9", "7", "5"}

            /*new String[]{"20", "2", "3", "4","9"},
            new String[]{"5", "1", "3", "5","6","9"},
            new String[]{"10", "1", "2","7","9","10","3"},
            new String[]{"5", "1", "5","6","7"},
            new String[]{"20", "2", "4","6","8"},
            new String[]{"1", "5", "2", "4","8","10"},
            new String[]{"10", "3", "4", "8"},
            new String[]{"30", "6", "7", "5"},
            new String[]{"20", "1", "2", "3"},
            new String[]{"3", "6", "8", "3"}));*/


    void initAgents() {
        Runtime rt = Runtime.instance();
        Profile p = new ProfileImpl();
        p.setParameter(Profile.MAIN_HOST, "localhost");
        p.setParameter(Profile.MAIN_PORT, "10098");

        ContainerController cc = rt.createMainContainer(p);
        int agentId = 0;

        try {
            double rnd1, rnd2;
            for (Integer[] adjacencyList: adjacencyLists) {
                agentId++;
                rnd1 = Math.random();
                rnd2 = Math.random();
                Object[] agentData = { new AgentData(
                        Arrays.copyOfRange(adjacencyList, 1, adjacencyList.length),
                        adjacencyList[0],
                        rnd1 > 0.5,
                        rnd2 > 0.5)
                };

                AgentController agent = cc.createNewAgent(
                        Integer.toString(agentId),
                        "ru.spbu.math.jade.MyAgent",
                        agentData
                );

                agent.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}