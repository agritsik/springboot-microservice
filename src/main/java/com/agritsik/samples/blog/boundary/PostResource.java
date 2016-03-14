package com.agritsik.samples.blog.boundary;

import com.agritsik.samples.blog.entity.Post;
import org.springframework.stereotype.Component;

import javax.ejb.EJB;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.List;

@Component
@Path("/posts")
public class PostResource {

    @Context
    UriInfo uriInfo;

    @EJB
    PostService postService;

    @POST
    public Response create(Post post){
        postService.create(post);

        URI uri = uriInfo.getAbsolutePathBuilder().path(String.valueOf(post.getId())).build();
        return Response.created(uri).build();
    }

    @GET
    @Path("{id}")
    public Post find(@PathParam("id") long id){
        return postService.find(id);
    }


    @GET
    public List<Post> find(@QueryParam("first") int first, @QueryParam("maxResult") int maxResult){
        return postService.find(first, maxResult);
    }


    @PUT
    @Path("{id}")
    public Response update(@PathParam("id") long id, Post post){
        postService.update(post);
        return Response.status(Response.Status.NO_CONTENT).build();
    }

    @DELETE
    @Path("{id}")
    public Response delete(@PathParam("id") long id){
        postService.delete(id);
        return Response.status(Response.Status.NO_CONTENT).build();
    }

}
