package com.albertocolella.rest_bootstrap;

/*
 * TODO swagger ui
 * TODO authentication, users, roles, permissions
 * TODO logger
 * TODO handle empty resultsets
 */
import java.util.EnumSet;

import javax.servlet.DispatcherType;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.servlets.CrossOriginFilter;
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

		FilterHolder cors = contextHandler.addFilter(CrossOriginFilter.class, "/*", EnumSet.of(DispatcherType.REQUEST));
		cors.setInitParameter(CrossOriginFilter.ALLOWED_ORIGINS_PARAM, "*");
		cors.setInitParameter(CrossOriginFilter.ACCESS_CONTROL_ALLOW_ORIGIN_HEADER, "*");
		cors.setInitParameter(CrossOriginFilter.ALLOWED_METHODS_PARAM, "GET,POST,HEAD");
		cors.setInitParameter(CrossOriginFilter.ALLOWED_HEADERS_PARAM, "X-Requested-With,Content-Type,Accept,Origin");
		
		Server server = new Server(7070);
		server.setHandler(contextHandler);
		server.start();
	}
	
}

