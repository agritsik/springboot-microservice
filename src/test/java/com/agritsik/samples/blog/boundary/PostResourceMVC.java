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

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

@RunWith(MockitoJUnitRunner.class)
public class PostResourceMVC {


    private MockMvc mvc;

    @InjectMocks
    PostResource postResource;

    @Mock
    PostService postService;

    @Before
    public void setUp() throws Exception {
        this.mvc = standaloneSetup(postResource).build();
    }


    @Test
    public void testCreate() throws Exception {

        // arrange
        Post post = new Post();
        post.setTitle("1st");

        // act && assert
        MvcResult mvcResult = this.mvc.perform(post("/resources/posts")
                .content(new ObjectMapper().writeValueAsString(post))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andDo(print()).andReturn();


        verify(postService).create(any(Post.class));

    }

    @Test
    public void testFind() throws Exception {
        // arrange
        Post post = new Post();
        post.setTitle("2nd");

        when(postService.find(anyLong())).thenReturn(post);

        // act && assert
        this.mvc.perform(get("/resources/posts/1"))
                .andExpect(status().isOk())
                .andDo(print());

        verify(postService).find(anyLong());

    }
}
