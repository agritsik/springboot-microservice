package com.agritsik.samples.blog.boundary;

import com.agritsik.samples.blog.entity.Post;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

@RunWith(MockitoJUnitRunner.class)
public class PostResourceTest {

    private MockMvc mvc;

    @InjectMocks
    PostResource postResource;

    @Mock
    PostService postService;

    @Before
    public void setUp() throws Exception {
        this.mvc = standaloneSetup(postResource)
                .alwaysDo(print()) // always print the result
                .build();
    }


    @Test
    public void testCreate() throws Exception {

        // arrange
        String postJson = new ObjectMapper().writeValueAsString(new Post("1st"));

        // act && assert
        this.mvc.perform(post("/resources/posts")
                .content(postJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().string(""));

        verify(postService).create(any(Post.class));
    }

    @Test
    public void testFind() throws Exception {
        // arrange
        when(postService.find(anyLong())).thenReturn(new Post("2nd"));

        // act && assert
        this.mvc.perform(get("/resources/posts/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(content().string(containsString("id")));

        verify(postService).find(anyLong());
    }
}
