package com.agritsik.samples.blog.integration;

import com.agritsik.samples.blog.Application;
import com.agritsik.samples.blog.entity.Post;
import junit.framework.TestCase;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(Application.class)
@WebIntegrationTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class PostResourceIT extends TestCase {

    @Autowired
    TestContext testContext;

    private static final Logger LOGGER = Logger.getLogger(PostResourceIT.class.getName());

    public static final String URL = "http://localhost:8080/resources/posts/";
    public static final String TITLE = "My new post via REST!";
    public static final String TITLE_EDITED = "My edited post via REST!";

    TestRestTemplate template = new TestRestTemplate();

    @Test
    public void test1Create() throws Exception {

        // create post
        Post post = new Post();
        post.setTitle(TITLE);

        ResponseEntity<Void> response = template.postForEntity(URL, post, Void.class);

        // check result
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getHeaders().getLocation());

        testContext.setCreatedURL(response.getHeaders().getLocation());
    }


    @Test
    public void test2Find() throws Exception {

        // try to find post by location
        Post post = template.getForEntity(testContext.getCreatedURL(), Post.class).getBody();

        System.out.println(post);
        assertNotNull(post);

    }

    @Test
    public void test3Update() throws Exception {

        // find post by location
        Post post = template.getForEntity(testContext.getCreatedURL(), Post.class).getBody();
        post.setTitle(TITLE_EDITED);

        // try to update post
        template.put(testContext.getCreatedURL(), post);

        // check result // todo: how to check void methods?
//        assertEquals(Response.Status.NO_CONTENT.getStatusCode(), response.getStatus());

        Post updatedPost = template.getForEntity(testContext.getCreatedURL(), Post.class).getBody();
        assertEquals(TITLE_EDITED, updatedPost.getTitle());

    }

    @Test
    public void test4Delete() throws Exception {

        // try to delete post
        template.delete(testContext.getCreatedURL());

        // check result // todo: how to check void methods?
//        assertEquals(Response.Status.NO_CONTENT.getStatusCode(), response.getStatus());
        Post deletedPost = template.getForEntity(testContext.getCreatedURL(), Post.class).getBody();

        assertNull(deletedPost);


    }

    @Test
    public void test5FindAll() throws Exception {

        // create posts
        for (int i = 0; i < 85; i++) {
            Post post = new Post();
            post.setTitle("Another post #" + i);
            template.postForEntity(URL, post, Void.class);
        }

        // try to find posts
        ResponseEntity<Post[]> entity = template.getForEntity(URL + "?start={start}&maxResult={max}",
                Post[].class, 0, 10);
        List<Post> posts = Arrays.asList(entity.getBody());

        // check result
        assertEquals(10, posts.size());
        assertEquals("Another post #0", posts.get(0).getTitle());

    }
}