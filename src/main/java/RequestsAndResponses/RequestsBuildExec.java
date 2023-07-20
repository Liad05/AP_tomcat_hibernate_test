package RequestsAndResponses;

import db_entities.State;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

//http://localhost:8080/test_tomcat_4_war_exploded/api/hello-world/test2?ID=4
public class RequestsBuildExec {
    String prefix = HttpMacros.getFullPrefix();
    boolean isGet;
    String state;

    public RequestsBuildExec(State state) {//post statement
        this.state = state.toSaveString();
        isGet = false;

    }

    //public
    public static String postState(String addPostFix, State state) throws Exception {
        String stateString = state.toSaveString();
        byte[] postData = stateString.getBytes(StandardCharsets.UTF_8);
        int postDataLength = postData.length;
        String request = HttpMacros.getFullPrefix()+addPostFix;
        URL url = new URL(request);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setDoOutput(true);
        conn.setInstanceFollowRedirects(false);
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        conn.setRequestProperty("charset", "utf-8");
        conn.setRequestProperty("Content-Length", Integer.toString(postDataLength));
        conn.setUseCaches(false);
        try (DataOutputStream wr = new DataOutputStream(conn.getOutputStream())) {
            wr.write(postData);
        }
        int responseCode = conn.getResponseCode();
        System.out.println("Response Code: " + responseCode);
        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        return response.toString();


    }

    public static State getState(String addPostFix,String IP,int port) throws Exception{
        State retState = new State();

        URL obj = new URL(HttpMacros.getFullPrefix()+addPostFix+"?"+"IP="+IP+"&"+"port="+port);
        HttpURLConnection conn = (HttpURLConnection) obj.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("User-Agent", "Mozilla/5.0");
        int responseCode = conn.getResponseCode();
        System.out.println("GET Response Code :: " + responseCode);
        StringBuffer response = new StringBuffer();
        if (responseCode == HttpURLConnection.HTTP_OK) { // success
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String inputLine;


            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            // print result
            System.out.println(response.toString());
        } else {
            System.out.println("GET request did not work.");
        }

        retState.fromSaveString(response.toString());
        return retState;

    }

}
