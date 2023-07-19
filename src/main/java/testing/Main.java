package testing;

import RequestsAndResponses.HttpMacros;
import RequestsAndResponses.RequestsStringBuilder;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;
import test_db_entities.State;
import test_db_managers.StateManager;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class Main {
    public static void main(String[] args) throws Exception{
        //System.out.println("hi there");
        //try to save a state using post request
        State testState = new State();
        testState.setIp("127.0.0.1");
        testState.setPort(3000);
        testState.setCurrentTurn(0);
        testState.setLastScore(0);
        testState.setPlayers("0:0;Y;R;V;A;N;S;M,1:18;L;S;T;X;Q;L;D,");
        testState.setBag("AAAAAAAABBCCDDDEEEEEEEEEEEEFFGGGHHIIIIIIIIIKLLMNNNNNOOOOOOOOPPRRRRRSSTTTTUUUUVWWYZ");
        testState.setLastTurnBoard("__________________________________________________________");
        testState.setBoard("___________________________________________________A______");
        String s;
        // s = RequestsStringBuilder.postState("/saveState",testState);
        //System.out.println(s);
        String gameIp = "127.0.0.1";
        int gamePort = 10000;
        String query = String.format("from State where ip  = \'%s\' and port = \'%d\'", gameIp, gamePort);
        testState = RequestsStringBuilder.getState("/getState",gameIp,gamePort);
        //String query = String.format("from State where ip  = \"%s\" and port = \"%d\"", gameIp, gamePort);
//        SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
//        Session session = sessionFactory.openSession();
//        Query query1 = session.createQuery(query);
//        List<State> list1 = query1.list();
        System.out.println(testState.toString());

    }

    }


