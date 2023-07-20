package com.example.test_tomcat_4;

import RequestsAndResponses.HttpMacros;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

@ApplicationPath(HttpMacros.applicationPrefix)
public class WebApplication extends Application {

}