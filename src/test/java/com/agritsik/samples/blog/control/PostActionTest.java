package com.agritsik.samples.blog.control;

import com.agritsik.samples.blog.Application;
import com.agritsik.samples.blog.boundary.PostRepository;
import com.agritsik.samples.blog.boundary.PostRestClient;
import com.agritsik.samples.blog.entity.Post;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.domain.PageRequest;

import java.util.List;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;

@RunWith(MockitoJUnitRunner.class)
public class PostActionTest {


    @InjectMocks
    PostAction postAction;

    @Mock
    PostRepository postRepository;

    @Mock
    RabbitTemplate rabbitTemplate;

    @Mock
    PostRestClient postRestClient;

    @Test
    public void testCreate() throws Exception {

        // arrange
        Post post = new Post("1st");

        // act
        postAction.create(post);

        // assert
        verify(postRepository).save(post);
        verify(rabbitTemplate).convertAndSend(Application.QUEUE_NAME, post.getTitle());
        verify(postRestClient).getPost(anyLong());
    }

    @Test
    public void testFind() throws Exception {

        // arrange
        Long postId = 1L;

        // act
        postAction.find(postId);

        // assert
        verify(postRepository).findOne(postId);
        verifyZeroInteractions(rabbitTemplate);

    }

    @Test
    public void testFindAll() throws Exception {

        // act
        postAction.find(5, 10);

        // assert
        verify(postRepository).findAll(any(PageRequest.class));
        verifyZeroInteractions(rabbitTemplate);
    }

    @Test
    public void testByName() throws Exception {

        // arrange
        String name = "requested name";

        // act
        List<Post> posts = postAction.findByTitle(name);

        // assert
        verify(postRepository).findByTitle(name);
        verifyZeroInteractions(rabbitTemplate);


    }

    @Test
    public void testUpdate() throws Exception {

        // arrange
        Post post = new Post("1nd");

        // act
        postAction.update(post);

        // assert
        verify(postRepository).save(post);
        verify(rabbitTemplate).convertAndSend(Application.QUEUE_NAME, post.getTitle());

    }

    @Test
    public void testDelete() throws Exception {

        // arrange
        long postId = 1;

        // act
        postAction.delete(postId);

        // assert
        verify(postRepository).delete(postId);
        verifyZeroInteractions(rabbitTemplate);

    }
}