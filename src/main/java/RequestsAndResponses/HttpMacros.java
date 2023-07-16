package RequestsAndResponses;

public class HttpMacros {
    public static final String ip = "http://localhost:";

    public static final String tomcatDefaultPort = "8080";
    public static final String warPrefix = "/test_tomcat_4_war_exploded";
    public static final String applicationPrefix = "/api";

    public static final String resourcePrefix = "/hello-world";

    public static String getFullPrefix() {
        return ip+tomcatDefaultPort+warPrefix+applicationPrefix+resourcePrefix;
    }
}
