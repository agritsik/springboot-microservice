package com.agritsik.samples.blog;

import com.agritsik.samples.blog.boundary.PostResource;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.stereotype.Component;

import javax.ws.rs.ApplicationPath;

@Component
@ApplicationPath("resources")
public class JAXRSConfiguration extends ResourceConfig {
    public JAXRSConfiguration() {
        register(PostResource.class);
    }
}
