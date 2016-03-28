package com.agritsik.samples.blog.boundary;

import com.agritsik.samples.blog.Application;
import com.agritsik.samples.blog.entity.Post;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(Application.class)
public class PostRestClientIntegrationTest {


    @Autowired PostRestClient postRestClient;

    @Test
    public void testGetPost() throws Exception {

        // arrange
        String title = "sunt aut facere repellat provident occaecati excepturi optio reprehenderit";

        // act
        Post post = postRestClient.getPost(1);

        // assert

        assertEquals(title, post.getTitle());
        System.out.println(post);


    }
}