package db_managers;
import org.hibernate.Session;
import org.hibernate.query.Query;
import db_entities.State;

import java.util.List;

public class StateManager {
    private Session session = null;
    public StateManager(Session session) {
        if(session == null){
            throw new RuntimeException("invalid session");
        }
        this.session = session;
    }

    public void saveState(State state){
        session.save(state);
        session.flush();
    }
    public void updateState(State state){
        session.update(state);
        session.flush();
    }


    public void deleteState(State state){
        session.delete(state);
        session.flush();
    }

    public State getGameStateByIpAndPort(String gameIp, int gamePort) {
        Query query1 = session.createQuery(String.format("from State where ip  = \"%s\" and port = \"%d\"", gameIp, gamePort));
        List<State> list1 = query1.list();
        State gameState = list1.get(0);
        return gameState;
    }
}
