package com.agritsik.samples.blog.boundary;

import com.agritsik.samples.blog.Application;
import com.agritsik.samples.blog.entity.Post;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(Application.class)
public class PostRepositoryIntegrationTest {

    @Autowired
    PostRepository postRepository;

    @Before
    public void setUp() throws Exception {
        for (int i = 0; i < 10; i++) {
            postRepository.save(new Post("post-" + i));
        }

    }

    @Test
    public void testFindByNameSuccess() throws Exception {

        // arrange
        String name = "post-1";

        // act
        List<Post> byName = postRepository.findByTitle(name);

        // assert
        assertEquals(1, byName.size());
        assertEquals(name, byName.get(0).getTitle());


    }
}
