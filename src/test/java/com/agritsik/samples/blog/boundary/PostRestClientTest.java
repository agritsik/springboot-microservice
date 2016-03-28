package com.agritsik.samples.blog.boundary;

import com.agritsik.samples.blog.entity.Post;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PostRestClientTest {

    public static final String TITLE = "some";

    @InjectMocks
    PostRestClient postRestClient;

    @Mock
    RestTemplate restTemplate;

    @Test
    public void testGetPost() throws Exception {

        // arrange
        ResponseEntity responseEntity = new ResponseEntity<>(new Post("some"), HttpStatus.OK);
        when(restTemplate.getForEntity(anyString(), any(), eq(1l))).thenReturn(responseEntity);

        // act
        Post post = postRestClient.getPost(1l);

        // assert
        verify(restTemplate).getForEntity(anyString(), any(), anyLong());
        assertEquals(TITLE, post.getTitle());


    }
}