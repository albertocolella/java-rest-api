package com.albertocolella.rest_bootstrap.resources;

import javax.servlet.http.HttpServlet;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.albertocolella.rest_bootstrap.model.Example;

@Path("/")
public class ExampleResource extends HttpServlet {
	
	private static final long serialVersionUID = 8152538863688455668L;

	@GET
	@Path("hello")
	@Produces(MediaType.APPLICATION_JSON)
	public Response helloWorld() {
		Example e = new Example();
		e.setText("Hello World!");
		return Response.status(Response.Status.OK)
				.entity(e)
				.build();
	}
}
