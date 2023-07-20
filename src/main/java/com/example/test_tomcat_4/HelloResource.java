package com.example.test_tomcat_4;

import RequestsAndResponses.HttpMacros;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;
import db_entities.State;
import db_managers.StateManager;

import javax.ws.rs.*;
import java.util.List;

@Path(HttpMacros.resourcePrefix)
public class HelloResource {
    @GET
    @Path("/getState")
    @Produces("text/plain")
    public String getStates (@QueryParam("IP") String IP, @QueryParam("port") int port) {
        String gameIp = IP;
        int gamePort = port;
        String query = String.format("from State where ip  = \'%s\' and port = \'%d\'", gameIp, gamePort);
        SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
        Session session = sessionFactory.openSession();
        Query query1 = session.createQuery(query);
        List<State> list1 = query1.list();
        return list1.get(list1.size()-1).toSaveString();

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
    public String saveState(String stateString) {
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

