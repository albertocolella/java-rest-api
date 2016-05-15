package com.albertocolella.rest_bootstrap.resources;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import javax.ws.rs.core.Application;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.glassfish.jersey.test.TestProperties;
import org.junit.Test;
import org.mockito.Mockito;

public class ResourceMockTest extends JerseyTest {
	
   /* @Override
    protected Application configure() {
    	Object setCallHandler = Mockito.mock(SetCallHandler.class);
    	return new SetApplication(setCallHandler);
    }
    
	
    @Test
    public void mockedAddCall() throws InvalidRequestException {
    	final String value = "MyTest1";
    	final boolean returnValue = true;
    	when(setCallHandler.add(value)).thenReturn(returnValue);
     
    	final Response responseWrapper = target("set/add/" + value).request(MediaType.APPLICATION_JSON_TYPE).get();
     
    	assertEquals(Response.Status.OK.getStatusCode(), responseWrapper.getStatus());
    	assertEquals(returnValue, responseWrapper.readEntity(BOOLEAN_RETURN_TYPE));
    }*/
}
