package com.agritsik.samples.blog.boundary;

import com.agritsik.samples.blog.Application;
import com.agritsik.samples.blog.TestContext;
import com.agritsik.samples.blog.entity.Post;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(Application.class)
public class PostServiceIT {

    public static final String TITLE = "My first post!";
    public static final String TITLE_EDITED = "My edited first post!";


    @Autowired
    PostService postService;

    @Test
    public void test1Create() throws Exception {
        Post post = new Post();
        post.setTitle(TITLE);
        postService.create(post);

        assertNotNull(post.getId());
        TestContext.createdId = post.getId();

    }

    @Test
    public void test2Find() throws Exception {
        Post post = postService.find(TestContext.createdId);
        assertNotNull(post);
        assertEquals(TITLE, post.getTitle());
    }


    @Test
    public void test3Update() throws Exception {

        Post post = postService.find(TestContext.createdId);
        post.setTitle(TITLE_EDITED);
        postService.update(post);

        Post updatedPost = postService.find(TestContext.createdId);

        assertEquals(TITLE_EDITED, updatedPost.getTitle());

    }

    @Test
    public void test4Remove() throws Exception {
        postService.delete(TestContext.createdId);

        Post post = postService.find(TestContext.createdId);
        assertNull(post);
    }

    @Test
    public void test5Find1() throws Exception {

        // create multiple posts
        for (int i = 0; i < 85; i++) {
            Post post = new Post();
            post.setTitle("Another post #" + i);
            postService.create(post);
        }

        List<Post> posts;

        // check first page
        posts = postService.find(0, 10);
        assertEquals(10, posts.size());
        assertEquals("Another post #0", posts.get(0).getTitle());
        assertEquals("Another post #8", posts.get(8).getTitle());

        // check last page
        posts = postService.find(80, 10);
        assertEquals(5, posts.size());
        assertEquals("Another post #80", posts.get(0).getTitle());

    }
}