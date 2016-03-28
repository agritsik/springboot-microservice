package component;


import com.agritsik.samples.blog.Application;
import com.agritsik.samples.blog.MockedConfiguration;
import com.agritsik.samples.blog.TestContext;
import com.agritsik.samples.blog.boundary.PostRepository;
import com.agritsik.samples.blog.boundary.PostRestClient;
import com.agritsik.samples.blog.entity.Post;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.stream.IntStream;

import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {Application.class, MockedConfiguration.class})
@WebAppConfiguration
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@ActiveProfiles("mocked")
public class PostComponentTest {

    public static final String TITLE = "1st";
    public static final String TITLE_EDITED = "NEW TITLE";

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    PostRepository postRepository;

    @Autowired
    RabbitTemplate rabbitTemplate;

    @Autowired
    RabbitAdmin rabbitAdmin;

    @Autowired
    PostRestClient postRestClient;


    private MockMvc mvc;

    @Before
    public void setUp() throws Exception {
        this.mvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext)
                .alwaysDo(print())
                .build();

        rabbitAdmin.purgeQueue(Application.QUEUE_NAME, false);
    }

    @Test
    public void test1Create() throws Exception {

        // arrange
        String postJson = new ObjectMapper().writeValueAsString(new Post(TITLE));

        // act && assert
        TestContext.createdLocationString = this.mvc.perform(post("/resources/posts")
                .content(postJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().string(""))
                .andExpect(header().string("Location", containsString("/resources/posts/1")))
                .andReturn().getResponse().getHeader("Location");


        // check repo
        TestContext.createdId = Long.valueOf(TestContext.createdLocationString.substring(TestContext.createdLocationString.length() - 1));
        Post post = postRepository.findOne(TestContext.createdId);
        assertNotNull(post);
        assertEquals(TITLE, post.getTitle());

        // check bus
        Object message = rabbitTemplate.receiveAndConvert(Application.QUEUE_NAME);
        assertEquals(TITLE, message.toString());

        Post post1 = postRestClient.getPost(1);
        assertEquals("yep", post1.getTitle());

    }

    @Test
    public void test2Find() throws Exception {

        // act && assert
        this.mvc.perform(get(TestContext.createdLocationString))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(content().string(containsString(TITLE)));

    }

    @Test
    public void test3Update() throws Exception {

        // arrange

        Post post = new Post(TestContext.createdId, TITLE_EDITED);
        String postJson = new ObjectMapper().writeValueAsString(post);

        // act && assert
        this.mvc.perform(put(TestContext.createdLocationString).content(postJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andExpect(content().string(""));

        // check repo
        Post savedPost = postRepository.findOne(TestContext.createdId);
        assertEquals(TITLE_EDITED, savedPost.getTitle());

        // check bus
        Object message = rabbitTemplate.receiveAndConvert(Application.QUEUE_NAME);
        assertEquals(TITLE_EDITED, message.toString());

    }

    @Test
    public void test4Delete() throws Exception {

        // act && assert
        this.mvc.perform(delete(TestContext.createdLocationString))
                .andExpect(status().isNoContent())
                .andExpect(content().string(""));

    }

    @Test
    public void test5FindAll() throws Exception {

        // arrange
        IntStream.range(0, 85).forEach(i -> {
            try {

                String postJson = new ObjectMapper().writeValueAsString(new Post(String.format("%s-%d", TITLE, i)));
                this.mvc.perform(post("/resources/posts")
                        .content(postJson)
                        .contentType(MediaType.APPLICATION_JSON));

            } catch (Exception e) {
                System.out.println(e.getMessage());
            }

        });

        // act && assert
        this.mvc.perform(get("/resources/posts" + "?start={start}&maxResult={max}", 0, 10))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(String.format("%s-%d", TITLE, 9))));

    }
}
