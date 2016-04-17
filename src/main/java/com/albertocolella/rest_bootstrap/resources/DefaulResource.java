package com.albertocolella.rest_bootstrap.resources;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;

public class DefaulResource extends HttpServlet {
    
	public <T> Response fetchAll(){
		List<T> a = new ArrayList<>();
		a.add((T) new Object());
		return Response.status(Response.Status.OK)
				.entity(a)
				.build();
	}
	public <T> GenericType<T> fetchId(Type id){
		GenericType<T> el = new GenericType<T>(id);
		return el;
	}
	public <T> T insert(T el){		
		return el;
	}
	public <T> void delete(T el){

	}
	public <T> T update(T el){
		return el;
	}
}
