package com.agritsik.samples.blog.boundary;

import com.agritsik.samples.blog.control.PostAction;
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
    PostAction postAction;

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Void> create(@RequestBody Post post) throws URISyntaxException {
        postAction.create(post);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(post.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public Post find(@PathVariable long id) {
        return postAction.find(id);
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<Post> find(@RequestParam("start") Integer first, @RequestParam("maxResult") Integer maxResult) {
        return postAction.find(first, maxResult).getContent();
    }

    @RequestMapping(value = "{id}", method = RequestMethod.PUT)
    public ResponseEntity<Void> update(@PathVariable long id, @RequestBody Post post) {
        postAction.update(post);
        return ResponseEntity.noContent().build();
    }

    @RequestMapping(value = "{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> delete(@PathVariable long id) {
        postAction.delete(id);
        return ResponseEntity.noContent().build();
    }
}
