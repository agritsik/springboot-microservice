package com.agritsik.samples.blog.control;

import com.agritsik.samples.blog.Application;
import com.agritsik.samples.blog.boundary.PostRepository;
import com.agritsik.samples.blog.entity.Post;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class PostActionTest {


    @InjectMocks
    PostAction postAction;

    @Mock
    PostRepository postRepository;

    @Mock
    RabbitTemplate rabbitTemplate;

    @Test
    public void testCreate() throws Exception {

        // arrange
        Post post = new Post("1st");

        // act
        postAction.create(post);

        // assert
        verify(postRepository).save(post);
        verify(rabbitTemplate).convertAndSend(Application.QUEUE_NAME, post.getTitle());

    }


}