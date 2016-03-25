package com.agritsik.samples.blog.integration;

import com.agritsik.samples.blog.Application;
import com.agritsik.samples.blog.control.PostAction;
import com.agritsik.samples.blog.entity.Post;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.data.domain.Page;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(Application.class)
public class PostServiceIT {

    public static final String TITLE = "My first post!";
    public static final String TITLE_EDITED = "My edited first post!";


    @Autowired
    TestContext testContext;

    @Autowired
    PostAction postAction;

    @Autowired
    RabbitTemplate rabbitTemplate;

    @Autowired
    RabbitAdmin rabbitAdmin;

    @Before
    public void setUp() throws Exception {
        rabbitAdmin.purgeQueue(Application.QUEUE_NAME, true);
    }

    @Test
    public void test1Create() throws Exception {

        // arrange
        Post post = new Post();
        post.setTitle(TITLE);

        // act
        postAction.create(post);

        // assert
        assertNotNull(post.getId());
        testContext.setCreatedId(post.getId());

        Object r = rabbitTemplate.receiveAndConvert(Application.QUEUE_NAME);
        assertEquals(TITLE, r.toString());

    }

    @Test
    public void test2Find() throws Exception {
        Post post = postAction.find(testContext.getCreatedId());
        assertNotNull(post);
        assertEquals(TITLE, post.getTitle());
    }


    @Test
    public void test3Update() throws Exception {

        Post post = postAction.find(testContext.getCreatedId());
        post.setTitle(TITLE_EDITED);
        postAction.update(post);

        Post updatedPost = postAction.find(testContext.getCreatedId());

        assertEquals(TITLE_EDITED, updatedPost.getTitle());

    }

    @Test
    public void test4Remove() throws Exception {
        postAction.delete(testContext.getCreatedId());

        Post post = postAction.find(testContext.getCreatedId());
        assertNull(post);
    }

    @Test
    public void test5Find1() throws Exception {

        // create multiple posts
        for (int i = 0; i < 85; i++) {
            Post post = new Post();
            post.setTitle("Another post #" + i);
            postAction.create(post);
        }


        // check first page
        Page<Post> posts = postAction.find(0, 10);
        assertEquals(10, posts.getContent().size());
        assertEquals("Another post #0", posts.getContent().get(0).getTitle());
        assertEquals("Another post #8", posts.getContent().get(8).getTitle());

        // check last page
        posts = postAction.find(8, 10);
        assertEquals(5, posts.getContent().size());
        assertEquals("Another post #80", posts.getContent().get(0).getTitle());

    }
}