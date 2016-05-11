package com.albertocolella.rest_bootstrap;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;

import com.albertocolella.rest_bootstrap.resources.ContentResource;

public class App {

	public static void main(String[] args) throws Exception {
		ResourceConfig resourceConfig = new ResourceConfig();
		resourceConfig.register(new ContentResource());
		resourceConfig.packages("org.glassfish.jersey.examples.jackson");
		resourceConfig.register(JacksonFeature.class);
		resourceConfig.register(io.swagger.jaxrs.listing.ApiListingResource.class);
		resourceConfig.register(io.swagger.jaxrs.listing.SwaggerSerializers.class);
		
		ServletContextHandler contextHandler = new ServletContextHandler(ServletContextHandler.SESSIONS);
		contextHandler.setContextPath("/api/v1");
		ServletHolder servletHolder = new ServletHolder(new ServletContainer(resourceConfig));
		contextHandler.addServlet(servletHolder, "/*");
		
		
		Server server = new Server(7070);
		server.setHandler(contextHandler);
		server.start();
	}
	
}

