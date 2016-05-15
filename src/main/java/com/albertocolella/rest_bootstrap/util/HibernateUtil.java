package com.albertocolella.rest_bootstrap.util;

import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

public class HibernateUtil {

	private static final long serialVersionUID = 8152538863688455668L;
	protected static SessionFactory sessionFactory;  
	// protected static final StandardServiceRegistry serviceRegistry;
	protected static StandardServiceRegistry serviceRegistry = null;

    static {  
    	try {
    		StandardServiceRegistryBuilder serviceRegistryBuilder = new StandardServiceRegistryBuilder();
    		serviceRegistryBuilder.configure(); // configures settings from hibernate.cfg.xml
    		serviceRegistry = serviceRegistryBuilder.build();
		
			sessionFactory = new MetadataSources( serviceRegistry ).buildMetadata().buildSessionFactory();
		}
		catch (Exception e) {
			// The registry would be destroyed by the SessionFactory, but we had trouble building the SessionFactory
			// so destroy it manually.
			e.printStackTrace();
			StandardServiceRegistryBuilder.destroy( serviceRegistry );
			sessionFactory = null;
		}
	}
      
    public static SessionFactory getSessionFactory() {  
        return sessionFactory;  
    } 
    
}
