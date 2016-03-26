package com.agritsik.samples.blog.boundary;

import com.agritsik.samples.blog.control.PostAction;
import com.agritsik.samples.blog.entity.Post;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

@RunWith(MockitoJUnitRunner.class)
public class PostResourceTest {

    public static final String POST_RESOURCE = "/resources/posts/";
    public static final Long POST_ID = 1L;

    private MockMvc mvc;

    @InjectMocks
    PostResource postResource;

    @Mock
    PostAction postAction;

    @Before
    public void setUp() throws Exception {
        this.mvc = standaloneSetup(postResource)
                // .alwaysDo(print()) // always print the result
                .build();
    }


    @Test
    public void testCreate() throws Exception {

        // arrange
        String postJson = new ObjectMapper().writeValueAsString(new Post("1st"));

        // act && assert
        this.mvc.perform(post(POST_RESOURCE)
                .content(postJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().string(""));

        verify(postAction).create(any(Post.class));
    }

    @Test
    public void testFind() throws Exception {
        // arrange
        when(postAction.find(anyLong())).thenReturn(new Post("2nd"));

        // act && assert
        this.mvc.perform(get(POST_RESOURCE + POST_ID))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(content().string(containsString("2nd")));

        verify(postAction).find(anyLong());
    }

    @Test
    public void testFindAll() throws Exception {


        // arrange
        Page mockedPage = Mockito.mock(Page.class, CALLS_REAL_METHODS);
        when(postAction.find(anyInt(), anyInt())).thenReturn(mockedPage);

        // act && assert
        this.mvc.perform(get(POST_RESOURCE + "?start={start}&maxResult={maxResult}", 2, 10))
                .andExpect(status().isOk());

    }

    @Test
    public void testUpdate() throws Exception {

        // arrange
        String postJson = new ObjectMapper().writeValueAsString(new Post("3rd"));

        // act && assert
        this.mvc.perform(put(POST_RESOURCE + POST_ID)
                .content(postJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andExpect(content().string(""));

        verify(postAction).update(any(Post.class));

    }

    @Test
    public void testDelete() throws Exception {

        // act && verify
        this.mvc.perform(delete(POST_RESOURCE + POST_ID))
                .andExpect(status().isNoContent())
                .andExpect(content().string(""));

        verify(postAction).delete(anyLong());

    }
}
