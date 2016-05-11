package com.albertocolella.rest_bootstrap.resources;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.mapping.Map;
import org.hibernate.metadata.ClassMetadata;
import org.hibernate.persister.entity.AbstractEntityPersister;

import com.albertocolella.rest_bootstrap.model.Page;
import com.albertocolella.rest_bootstrap.util.HibernateUtil;

@Path("/hello")
public class ExampleResource extends DefaultResource {
	
	private static final long serialVersionUID = 8152538863688455668L;

	@GET
	@Path("world")
	@Produces(MediaType.APPLICATION_JSON)
	// public List<Example> fetchAllB() {
	public Response fetchAll() {
		SessionFactory sessionFactory = HibernateUtil.getSessionFactory();  
        Session session = sessionFactory.openSession();
		session.beginTransaction();
		ClassMetadata classMetadata = sessionFactory.getClassMetadata(Page.class.getName());
		AbstractEntityPersister abstractEntityPersister = (AbstractEntityPersister) classMetadata;
	    String tableName = abstractEntityPersister.getTableName();
        List<Page> examples = session.createQuery("from Example").list();  
        session.getTransaction().commit();
        session.close();
        if(examples!=null && examples.size()>0){
        	GenericEntity<List<Page>> entities = new GenericEntity<List<Page>>(examples) {};
		return Response.status(Response.Status.OK)
				.entity(entities)
				.build();
        } else {
        	return Response.status(Response.Status.NO_CONTENT)
        			.build();
        }
	}
	
	@POST
	@Path("world")
	@Produces(MediaType.APPLICATION_JSON)
	public Response insert(Page e) {
		SessionFactory sessionFactory = HibernateUtil.getSessionFactory();  
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		session.save( e );
		session.getTransaction().commit();
		session.close();
		return Response.status(Response.Status.CREATED)
				.build();
	}
}
