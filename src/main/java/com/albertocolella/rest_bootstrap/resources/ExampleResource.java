package com.albertocolella.rest_bootstrap.resources;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.albertocolella.rest_bootstrap.model.Example;
import com.albertocolella.rest_bootstrap.util.HibernateUtil;

@Path("/hello")
public class ExampleResource extends DefaulResource {
	
	private static final long serialVersionUID = 8152538863688455668L;

	@GET
	@Path("world")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Example> fetchAllB() {
		SessionFactory sessionFactory = HibernateUtil.getSessionFactory();  
        Session session = sessionFactory.openSession();
		session.beginTransaction();
        List<Example> examples = session.createQuery("from Example").list();  
        session.getTransaction().commit();
        session.close();  /**/
        return examples;
       /* if(examples!=null && examples.size()>0){
		return Response.status(Response.Status.OK)
				.entity(examples)
				.build();
        } else {
        	return Response.status(Response.Status.NO_CONTENT)
        			.build();
        }*/
	}
}
