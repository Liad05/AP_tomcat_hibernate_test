package db_entities;
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

}
