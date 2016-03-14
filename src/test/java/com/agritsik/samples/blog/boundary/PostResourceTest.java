package com.agritsik.samples.blog.boundary;

import com.agritsik.samples.blog.Application;
import com.agritsik.samples.blog.TestContext;
import com.agritsik.samples.blog.entity.Post;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.logging.Logger;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(Application.class)
@WebIntegrationTest
public class PostResourceTest extends TestCase {

    private static final Logger LOGGER = Logger.getLogger(PostResourceTest.class.getName());
    public static final String TITLE = "My new post via REST!";
    public static final String TITLE_EDITED = "My edited post via REST!";


    private Client client;
    private WebTarget postsTarget;


    @Before
    public void setUp() throws Exception {
        this.client = ClientBuilder.newClient();
        this.postsTarget = client.target("http://localhost:8080").path("/resources/posts");
    }

    @Test
    public void test1Create() throws Exception {

        // create post
        Post post = new Post();
        post.setTitle(TITLE);

        Response response = this.postsTarget.request(MediaType.APPLICATION_JSON).post(Entity.json(post));

        // check result
        assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());
        assertNotNull(response.getLocation());

        TestContext.createdURL = response.getLocation();
    }

    @Test
    public void test2Find() throws Exception {

        // try to find post by location
        Post post = this.client.target(TestContext.createdURL).request(MediaType.APPLICATION_JSON).get(Post.class);

        System.out.println(post);
        assertNotNull(post);

    }

    @Test
    public void test3Update() throws Exception {

        // find post by location
        Post post = this.client.target(TestContext.createdURL).request(MediaType.APPLICATION_JSON).get(Post.class);
        post.setTitle(TITLE_EDITED);

        // try to update post
        Response response = this.postsTarget.path(String.valueOf(post.getId()))
                .request(MediaType.APPLICATION_JSON).put(Entity.json(post));

        // check result
        assertEquals(Response.Status.NO_CONTENT.getStatusCode(), response.getStatus());

        Post updatedPost = client.target(TestContext.createdURL).request(MediaType.APPLICATION_JSON).get(Post.class);
        assertEquals(TITLE_EDITED, updatedPost.getTitle());

    }

    @Test
    public void test4Delete() throws Exception {

        // try to delete post
        Response response = client.target(TestContext.createdURL).request(MediaType.APPLICATION_JSON).delete();

        // check result
        assertEquals(Response.Status.NO_CONTENT.getStatusCode(), response.getStatus());
        Post deletedPost = client.target(TestContext.createdURL).request(MediaType.APPLICATION_JSON).get(Post.class);

        assertNull(deletedPost);


    }

    @Test
    public void test5FindAll() throws Exception {

        // create posts
        for (int i = 0; i < 85; i++) {
            Post post = new Post();
            post.setTitle("Another post #" + i);
            this.postsTarget.request(MediaType.APPLICATION_JSON).post(Entity.json(post));
        }

        // try to find posts
        List<Post> posts = this.postsTarget.queryParam("start", 0).queryParam("maxResult", 10)
                .request(MediaType.APPLICATION_JSON).get(new GenericType<List<Post>>() {});

        // check result
        assertEquals(10, posts.size());
        assertEquals("Another post #0", posts.get(0).getTitle());

    }
}