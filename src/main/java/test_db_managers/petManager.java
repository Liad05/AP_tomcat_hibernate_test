package test_db_managers;

import org.hibernate.Session;
import test_db_entities.Pet;

public class petManager {
    private Session session = null;
    public petManager(Session session) {
        if(session == null){
            throw new RuntimeException("invalid session");
        }
        this.session = session;
    }

    public void savePet(Pet pet){
        session.save(pet);
        session.flush();
    }
    public void updatePet(Pet pet){
        session.update(pet);
        session.flush();
    }

    public void deletePet(Pet pet){
        session.delete(pet);
        session.flush();
    }
}
