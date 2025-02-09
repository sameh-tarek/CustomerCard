package controllers;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;


@ApplicationPath("/")
public class RestWebServiceApplication extends Application {

	@Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new HashSet<>();
        
        resources.add(CustomerCardController.class);
        
        return resources;
    }

}
