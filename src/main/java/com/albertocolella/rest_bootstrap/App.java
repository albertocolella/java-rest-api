package com.albertocolella.rest_bootstrap;

/*
 * 
 * TODO authentication, users, roles, permissions
 * TODO logger
 * TODO handle empty resultsets
 * TODO web.xml conf
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
import java.util.Properties;
import java.io.InputStream;
import java.io.FileNotFoundException;

import com.albertocolella.rest_bootstrap.resources.ContentResource;

import io.swagger.jaxrs.config.BeanConfig;

public class App {

	public static void main(String[] args) throws Exception {
		ResourceConfig resourceConfig = new ResourceConfig();
		resourceConfig.register(new ContentResource());
		//resourceConfig.packages("org.glassfish.jersey.examples.jackson");
		resourceConfig.register(JacksonFeature.class);
		resourceConfig.register(io.swagger.jaxrs.listing.ApiListingResource.class);
		resourceConfig.register(io.swagger.jaxrs.listing.SwaggerSerializers.class);

		Properties prop = new Properties();
		String propFileName = "config.properties";
		InputStream inputStream = App.class
				.getClassLoader()
				.getResourceAsStream(propFileName);
		if (inputStream != null) {
			prop.load(inputStream);
		} else {
			throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
		}
		
		ServletContextHandler contextHandler = new ServletContextHandler(ServletContextHandler.SESSIONS);
		contextHandler.setContextPath("/api/v1");
		ServletHolder servletHolder = new ServletHolder(new ServletContainer(resourceConfig));
		contextHandler.addServlet(servletHolder, "/*");

		FilterHolder cors = contextHandler.addFilter(CrossOriginFilter.class, "/*", EnumSet.of(DispatcherType.REQUEST));
		cors.setInitParameter(CrossOriginFilter.ALLOWED_ORIGINS_PARAM, "*");
		cors.setInitParameter(CrossOriginFilter.ACCESS_CONTROL_ALLOW_ORIGIN_HEADER, "*");
		cors.setInitParameter(CrossOriginFilter.ALLOWED_METHODS_PARAM, "GET,POST,HEAD");
		cors.setInitParameter(CrossOriginFilter.ALLOWED_HEADERS_PARAM, "X-Requested-With,Content-Type,Accept,Origin");
		
		String version = prop.getProperty("version");
		String jettyPort = prop.getProperty("jetty.port");
        String jettyHost = prop.getProperty("jetty.host");
        
		BeanConfig beanConfig = new BeanConfig();
        beanConfig.setVersion(version);
        beanConfig.setSchemes(new String[]{"http"});
        beanConfig.setHost(jettyHost+":"+prop.getProperty("jetty.port"));
        beanConfig.setBasePath("/api/v1/docs");
        beanConfig.setResourcePackage("com.albertocolella.rest_bootstrap.resources");
        beanConfig.setScan(true);
        
        
        
		Server server = new Server(Integer.decode(jettyPort));
		server.setHandler(contextHandler);
		server.start();
	}
	
}

