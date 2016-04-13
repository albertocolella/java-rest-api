package com.albertocolella.rest_bootstrap;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;

import com.albertocolella.rest_bootstrap.resources.ExampleResource;

public class App {

	public static void main(String[] args) throws Exception {
		ResourceConfig resourceConfig = new ResourceConfig();
		resourceConfig.register(new ExampleResource());
		 
		ServletContextHandler contextHandler = new ServletContextHandler(ServletContextHandler.SESSIONS);
		ServletHolder servletHolder = new ServletHolder(new ServletContainer(resourceConfig));
		contextHandler.addServlet(servletHolder, "/*");
		 
		Server server = new Server(7070);
		server.setHandler(contextHandler);
		server.start();
		
		/*Server server = new Server(7070);
		ServletContextHandler handler = new ServletContextHandler(server, "/api/v1");
		handler.addServlet(ExampleResource.class, "/example");
		try {
		     server.start();
		     server.join();
		 } finally {
		     server.destroy();
		 }*/

	}

}

