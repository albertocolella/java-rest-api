package com.albertocolella.rest_bootstrap.resources;


import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.albertocolella.rest_bootstrap.auth.Credentials;
import com.albertocolella.rest_bootstrap.util.HibernateUtil;
import com.albertocolella.rest_bootstrap.model.User;

@Path("/user")
public class UserResource extends DefaultResource {


	/**
	 * 
	 */
	private static final long serialVersionUID = 6092814414858031951L;

	@POST
	@Path("login")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
    public Response authenticateUser(Credentials credentials) {

        String username = credentials.getUsername();
        String password = credentials.getPassword();

		try {
            authenticate(username, password);
            String token = issueToken(username);
            return Response.ok(token).build();
        } catch (Exception e) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }      
    }

    private void authenticate(String username, String password) throws Exception {
        // Authenticate against a database, LDAP, file or whatever
        // Throw an Exception if the credentials are invalid
    	SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        Session session = sessionFactory.openSession();
		session.beginTransaction();
		String entityName = getClassNameFromParam("User");
		String tableName= getClassTableName(entityName);
    	Query hql = session.createQuery("from " + tableName + " where username=:un and password=:pwd");
        hql.setString("un", username);
        hql.setString("pwd", password);
        List<User> a = hql.list();
        session.getTransaction().commit();
        session.close();
        
    }

    private String issueToken(String username) {
		// Issue a token (can be a random String persisted to a database or a JWT token)
        // The issued token must be associated to a user
        // Return the issued token
    }
}