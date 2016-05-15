package com.albertocolella.rest_bootstrap.resources;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import javax.transaction.Transactional;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.glassfish.jersey.test.TestProperties;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.albertocolella.rest_bootstrap.model.Page;
import com.albertocolella.rest_bootstrap.util.HibernateUtil;

import net.sf.json.JSONObject;

@Transactional
public class ResourceTest extends JerseyTest {
	
	Session session = null;
	
    @Override
    protected Application configure() {
    	enable(TestProperties.LOG_TRAFFIC);
        enable(TestProperties.DUMP_ENTITY);
        ResourceConfig resourceConfig = new ResourceConfig(ContentResource.class);
        resourceConfig.packages("org.glassfish.jersey.examples.jackson");
		resourceConfig.register(JacksonFeature.class);
        return resourceConfig;
    }
    
    @Before
    public void openSession() {
    	SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        session = sessionFactory.openSession();
		session.beginTransaction();
    }
    
    @After
    public void closeSession() {
    	session.getTransaction().rollback();
        session.close();
    }
    
    private void insertNewPage(){    	
		Page p = new Page();
		p.setTitle("prova");
		session.save(p);		
    }
    
    private int countPages(){
    	Query query = session.createQuery(
    	        "select count(*) from Page");
    	Long count = (Long)query.uniqueResult();
    	return count.intValue();
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
    	int pages = countPages();
    	Page ex = new Page();
    	ex.setTitle(text);
        Response output = target("/content/page").request().post(Entity.entity(ex, MediaType.APPLICATION_JSON));
        assertEquals("should return status 201", 201, output.getStatus());
        assertEquals("should have added a Page", pages+1, countPages());
       
    }
}
