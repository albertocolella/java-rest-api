package com.albertocolella.rest_bootstrap.resources;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.glassfish.jersey.test.TestProperties;
import org.junit.Test;

import com.albertocolella.rest_bootstrap.model.Page;

import net.sf.json.JSONObject;

public class ResourceTest extends JerseyTest {
	
    @Override
    protected Application configure() {
    	enable(TestProperties.LOG_TRAFFIC);
        enable(TestProperties.DUMP_ENTITY);
        ResourceConfig resourceConfig = new ResourceConfig(ContentResource.class);
        resourceConfig.packages("org.glassfish.jersey.examples.jackson");
		resourceConfig.register(JacksonFeature.class);
        return resourceConfig;
    }
    
    @Test
    public void testFetchAll() {
        Response output = target("/content/page").request().get();
        assertEquals("should return status 200", 200, output.getStatus());
        assertNotNull("Should return list", output.getEntity());
    }
    
    @Test
    public void insert() {
    	String text = "this is a test";
    	Page ex = new Page();
    	ex.setTitle(text);
        Response output = target("/content/page").request().post(Entity.entity(ex, MediaType.APPLICATION_JSON));
        assertEquals("should return status 201", 201, output.getStatus());
       
    }
}
