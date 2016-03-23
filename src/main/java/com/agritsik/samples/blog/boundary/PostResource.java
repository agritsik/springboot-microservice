package com.agritsik.samples.blog.boundary;

import com.agritsik.samples.blog.entity.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@RestController
@RequestMapping(value = "/resources/posts")
public class PostResource {

    @Autowired
    PostService postService;

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Void> create(@RequestBody Post post) throws URISyntaxException {
        System.out.println("Resource creates...");
        postService.create(post);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(post.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public Post find(@PathVariable long id) {
        return postService.find(id);
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<Post> find(@RequestParam("start") Integer first, @RequestParam("maxResult") Integer maxResult) {
        return postService.find(first, maxResult);
    }

    @RequestMapping(value = "{id}", method = RequestMethod.PUT)
    public ResponseEntity<Void> update(@PathVariable long id, @RequestBody Post post) {
        postService.update(post);
        return ResponseEntity.noContent().build();
    }

    @RequestMapping(value = "{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> delete(@PathVariable long id) {
        postService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
