package com.example.test_tomcat_4;

import RequestsAndResponses.HttpMacros;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;
import db_entities.State;
import db_entities.StateManager;

import javax.ws.rs.*;
import java.util.List;

@Path(HttpMacros.resourcePrefix)
public class WebResource {
    @GET
    @Path("/test1")
    @Produces("text/plain")
    public String test1 () {

        return "hello world!";

    }

    @GET
    @Path("/getState")
    @Produces("text/plain")
    public String getStates (@QueryParam("IP") String IP, @QueryParam("port") int port) {
        String gameIp = IP;
        int gamePort = port;
        String query = String.format("from State where ip  = \'%s\' and port = \'%d\'", gameIp, gamePort);
        return getString(query);

    }


    @GET //get by id
    @Path("/getStateById")
    @Produces("text/plain")
    public String getStatesById ( @QueryParam("ID") int ID) {
        int gameID = ID;
        String query = String.format("from State where id  = \'%d\'", gameID);
        return getString(query);

    }

    private String getString(String query) {
        SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
        Session session = sessionFactory.openSession();
        Query query1 = session.createQuery(query);
        List<State> list1 = query1.list();
        return list1.get(list1.size()-1).toSaveString();
    }


    @POST
    @Path("/saveState")
    @Consumes("text/plain")
    public String saveState(String stateString) {
        System.out.println("we're in.");
        State testState = new State();
        testState.fromSaveString(stateString);
        StateManager stateManager = new StateManager(new Configuration().configure().buildSessionFactory().openSession());
        stateManager.saveState(testState);
        return stateString+" it works!";


    }
}

