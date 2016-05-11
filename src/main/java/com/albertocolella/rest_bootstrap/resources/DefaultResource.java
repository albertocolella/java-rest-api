package com.albertocolella.rest_bootstrap.resources;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
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
import javax.json.JsonException;
import javax.json.JsonObject;
//import org.json.JSONObject;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.metadata.ClassMetadata;
import org.hibernate.persister.entity.AbstractEntityPersister;

import com.albertocolella.rest_bootstrap.model.Page;
import com.albertocolella.rest_bootstrap.util.HibernateUtil;

import net.sf.json.JSONObject;

@Path("/{type}")
public abstract class DefaultResource extends HttpServlet {

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
	public Response insert(@PathParam("model") String modelName, JSONObject el) throws JsonException {
		SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
		Session session = sessionFactory.openSession();
		String entityName = getClassNameFromParam(modelName);
		Class<?> c;
		try {
			c = Class.forName(entityName);
			session.beginTransaction();
			session.save(entityName, JSONObject.toBean(el, c) );
			session.getTransaction().commit();
		} catch (ClassNotFoundException e) {
			// TODO
			e.printStackTrace();
		} finally {
			session.close();
		}
		return Response.status(Response.Status.CREATED)
				.build();
	}

	@PUT
	@Path("/{model}/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public <T> Response update(@PathParam("model") String modelName, @PathParam("id") Long id, JSONObject el)
		throws JsonException {
		SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
		Session session = sessionFactory.openSession();
		String entityName = getClassNameFromParam(modelName);
		Class<?> c;
		try {
			c = Class.forName(entityName);
			session.beginTransaction();
			Object b = session.get(entityName, id);
			// TODO check b exists
			Object j = JSONObject.toBean(el, c);
			Method method = c.getMethod("setId", Long.class);
			method.invoke(j, id);			 
			session.merge(entityName, j );
			session.getTransaction().commit();
		} catch (ClassNotFoundException e) {
			// TODO arg[0] is not a valid class name
			e.printStackTrace();
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			// TODO method setId not accessible
			 e.printStackTrace();		
		} catch (SecurityException e) {
			// TODO method setId not accessible
			 e.printStackTrace();	
		} catch (NoSuchMethodException e) {
			// TODO method setId does not exist
			 e.printStackTrace();	
		} finally {
			session.close();
		}
		return Response.status(Response.Status.OK)
				.build();
	}

	@DELETE
	@Path("/{model}/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response delete(@PathParam("model") String modelName, @PathParam("id") Long id){
		SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		String entityName = getClassNameFromParam(modelName);
		Class<?> c;
		try {
			c = Class.forName(entityName);
			session.beginTransaction();
			Object b = session.get(entityName, id);		 
			session.delete(entityName, b );
			session.getTransaction().commit();
		} catch (ClassNotFoundException e) {
			// TODO arg[0] is not a valid class name
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO method setId not accessible
			 e.printStackTrace();		
		} catch (SecurityException e) {
			// TODO method setId not accessible
			 e.printStackTrace();	
		} finally {
			session.close();
		}
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
