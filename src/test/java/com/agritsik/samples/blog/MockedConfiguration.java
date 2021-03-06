package com.agritsik.samples.blog;

import com.agritsik.samples.blog.boundary.PostRestClient;
import com.agritsik.samples.blog.entity.Post;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.when;

@Configuration
@Profile("mock")
public class MockedConfiguration {

    @Bean
    public PostRestClient getPostRestClient() {
        PostRestClient mock = Mockito.mock(PostRestClient.class);
        when(mock.getPost(anyLong())).thenReturn(new Post("yep"));
        return mock;
    }


}
