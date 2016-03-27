package com.agritsik.samples.blog.control;

import com.agritsik.samples.blog.Application;
import com.agritsik.samples.blog.boundary.PostRepository;
import com.agritsik.samples.blog.entity.Post;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional // todo: why is it mandatory for spring?
public class PostAction {

    @Autowired
    PostRepository postRepository;

    @Autowired
    RabbitTemplate rabbitTemplate;

    public void create(Post post) {
        postRepository.save(post);
        rabbitTemplate.convertAndSend(Application.QUEUE_NAME, post.getTitle());
    }

    public Post find(long id) {
        return postRepository.findOne(id);
    }

    public Page<Post> find(int first, int maxResult) {
        return postRepository.findAll(new PageRequest(first, maxResult));
    }

    public Post update(Post post) {
        Post savedPost = postRepository.save(post);
        rabbitTemplate.convertAndSend(Application.QUEUE_NAME, post.getTitle());
        return savedPost;
    }

    public void delete(long id) {
        postRepository.delete(id);
    }

    public List<Post> findByTitle(String name) {
        return postRepository.findByTitle(name);
    }
}
