package com.agritsik.samples.blog.boundary;

import com.agritsik.samples.blog.entity.Post;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Profile("default")
public class PostRestClient {

    public static final String URL = "http://jsonplaceholder.typicode.com/posts/";

    RestTemplate restTemplate = new RestTemplate();

    public Post getPost(long id){
        ResponseEntity<Post> forEntity = restTemplate.getForEntity(URL + "/{id}", Post.class, id);
        return forEntity.getBody();
    }

}
