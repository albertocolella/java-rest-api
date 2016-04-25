package com.albertocolella.rest_bootstrap.resources;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServlet;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.metadata.ClassMetadata;
import org.hibernate.persister.entity.AbstractEntityPersister;

import com.albertocolella.rest_bootstrap.model.Example;
import com.albertocolella.rest_bootstrap.util.HibernateUtil;

@Path("/content")
public class DefaultResource extends HttpServlet {
    
	/**
	 * 
	 */
	private static final long serialVersionUID = 7213156711136142908L;

	@GET
	@Path("/{model}")
	@Produces(MediaType.APPLICATION_JSON)
	public <T> Response fetchAll(@PathParam("model") String modelName){
		SessionFactory sessionFactory = HibernateUtil.getSessionFactory();  
        Session session = sessionFactory.openSession();
		session.beginTransaction();
		String entityName = getClassNameFromParam(modelName);
		String tableName= getClassTableName(entityName);
		try {
			Class<?> c = Class.forName(entityName);		
	        List<T> a = session.createQuery("from " + tableName).list();  
	        session.getTransaction().commit();
	        session.close();
	        if(a!=null && a.size()>0){
	        	GenericEntity<List<T>> entities = new GenericEntity<List<T>>(a) {};
	        	return Response.status(Response.Status.OK)
						.entity(entities)
						.build();
	        } else {
	        	return Response.status(Response.Status.NO_CONTENT)
	        			.build();
	        }
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return Response.status(Response.Status.NOT_FOUND)
        			.build();
		}
	}
	
	@GET
	@Path("/{model}/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public <T> Response fetchId(@PathParam("model") String modelName, @PathParam("id") Long id){
		SessionFactory sessionFactory = HibernateUtil.getSessionFactory();  
        Session session = sessionFactory.openSession();
		session.beginTransaction();
		String entityName = getClassNameFromParam(modelName);
		String tableName= getClassTableName(entityName);
		try {
			Class<?> c = Class.forName(entityName);
	        Query hql = session.createQuery("from " + tableName + " where id=:id");
	        hql.setParameter("id", id);
	        List<T> a = hql.list();
	        session.getTransaction().commit();
	        session.close();
	        if(a!=null && a.size()>0){
	        	GenericEntity<List<T>> entities = new GenericEntity<List<T>>(a) {};
	        	return Response.status(Response.Status.OK)
						.entity(entities)
						.build();
	        } else {
	        	return Response.status(Response.Status.NO_CONTENT)
	        			.build();
	        }
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return Response.status(Response.Status.NOT_FOUND)
        			.build();
		}
	}
	
	@POST
	@Path("/{model}")
	@Produces(MediaType.APPLICATION_JSON)
	public <T> Response insert(@PathParam("model") String modelName, T el){		
/*		SessionFactory sessionFactory = HibernateUtil.getSessionFactory();  
		Session session = sessionFactory.openSession();
		String entityName = getClassNameFromParam(modelName);
		
		Class<?> c;
		try {
			c = Class.forName(entityName);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    Constructor<?> ctor = c.getConstructors()[0];
	    Object object;
		try {
			object = ctor.newInstance(el);
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	   // c.getDeclaredMethods()[0].invoke(object,Object... MethodArgs);
		session.beginTransaction();
		session.save( (Class<entityName>) el );
		session.getTransaction().commit();
		session.close();*/
		return Response.status(Response.Status.CREATED)
				.build();
	}	
	
	@PUT
	@Path("/{model}/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public <T> Response update(T el){		
		SessionFactory sessionFactory = HibernateUtil.getSessionFactory();  
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		session.save( el );
		session.getTransaction().commit();
		session.close();
		return Response.status(Response.Status.OK)
				.build();
	}
	
	@DELETE
	@Path("/{model}/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public <T> Response delete(T el){
		SessionFactory sessionFactory = HibernateUtil.getSessionFactory();  
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		session.delete( el );
		session.getTransaction().commit();
		session.close();
		return Response.status(Response.Status.OK)
				.build();
	}
	
	protected String getClassTableName(String className){
		SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
		ClassMetadata classMetadata = sessionFactory.getClassMetadata(className);
		AbstractEntityPersister abstractEntityPersister = (AbstractEntityPersister) classMetadata;
	    String tableName = abstractEntityPersister.getTableName();
	    return tableName;
	}
	
	protected String getClassNameFromParam(String param){
		SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
		Map<String,ClassMetadata> classMetadata = sessionFactory.getAllClassMetadata();
		String modelName = null;
		for(String entityName : classMetadata.keySet()){
			String[] splitted = entityName.split(Pattern.quote("."));
			String className = splitted[splitted.length-1];
			if(!className.toLowerCase().equals(param.toLowerCase())){
				continue;
			}
			modelName = entityName;
			break;
	    }
	    return modelName;
	}

}
