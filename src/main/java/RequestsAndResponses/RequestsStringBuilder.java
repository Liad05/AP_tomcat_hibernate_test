package RequestsAndResponses;

import test_db_entities.State;

//http://localhost:8080/test_tomcat_4_war_exploded/api/hello-world/test2?ID=4
public class RequestsStringBuilder {
    String prefix = HttpMacros.getFullPrefix();
    boolean isGet;
    String state;

    public RequestsStringBuilder(State state) {//post statement
        this.state = state.toSaveString();
        isGet = false;

    }


}
