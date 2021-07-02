package backend.service;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.ext.Provider;

@Provider
public class HeaderFilter implements ContainerResponseFilter {

    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) {
        responseContext.getHeaders().add("access-control-allow-origin", "*");
        responseContext.getHeaders().add("access-control-allow-headers", "access-control-allow-origin,content-type,authorization");
        responseContext.getHeaders().add("access-control-allow-methods", "GET, POST, PUT, DELETE");
    }
}

