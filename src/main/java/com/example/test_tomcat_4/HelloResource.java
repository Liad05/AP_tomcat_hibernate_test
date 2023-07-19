package com.example.test_tomcat_4;

import RequestsAndResponses.HttpMacros;
import org.hibernate.cfg.Configuration;
import test_db_entities.State;
import test_db_managers.StateManager;

import javax.ws.rs.*;

@Path(HttpMacros.resourcePrefix)
public class HelloResource {
    @GET
    @Path("/test1")
    @Produces("text/plain")
    public String hello() {
        return "Hello, Wodffgdfrld!";
    }

//    @GET
//    @Path("/test2")
//    @Produces("text/plain")
//    public String hel2lo(@QueryParam("ID") int id) {
//
//        return "i know your id is"+id+"!";
//    }

    @POST
    @Path("/saveState")
    @Produces("text/plain")
    public String hello(String stateString) {
        System.out.println("we're in.");
        State testState = new State();
        testState.fromSaveString(stateString);
        StateManager stateManager = new StateManager(new Configuration().configure().buildSessionFactory().openSession());
        stateManager.saveState(testState);
        return stateString+" it works!";
//        State testState = new State();
//        testState.setIp("127.0.0.1");
//        testState.setPort(port);
//        testState.setCurrentTurn(0);
//        testState.setLastScore(0);
//        testState.setPlayers("0:0;Y;R;V;A;N;S;M,1:18;L;S;T;X;Q;L;D,");
//        testState.setBag("AAAAAAAABBCCDDDEEEEEEEEEEEEFFGGGHHIIIIIIIIIKLLMNNNNNOOOOOOOOPPRRRRRSSTTTTUUUUVWWYZ");
//        testState.setLastTurnBoard("__________________________________________________________");
//        testState.setBoard("___________________________________________________A______");
//        try {
//            StateManager stateManager = new StateManager(new Configuration().configure().buildSessionFactory().openSession());
//            stateManager.saveState(testState);
//            return "save success";
//        }
//        catch (Exception e){
//            return "save failed";
//        }
//        return "save failed";

    }
}

