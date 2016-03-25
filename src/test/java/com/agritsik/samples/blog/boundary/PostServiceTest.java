package com.agritsik.samples.blog.boundary;

import com.agritsik.samples.blog.Application;
import com.agritsik.samples.blog.entity.Post;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import javax.persistence.EntityManager;

import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class PostServiceTest {


    @InjectMocks
    PostService postService;

    @Mock
    EntityManager entityManager;

    @Mock
    RabbitTemplate rabbitTemplate;

    @Test
    public void testCreate() throws Exception {

        // arrange
        Post post = new Post("1st");

        // act
        postService.create(post);

        // assert
        verify(entityManager).persist(post);
        verify(rabbitTemplate).convertAndSend(Application.QUEUE_NAME, post.getTitle());

    }
}