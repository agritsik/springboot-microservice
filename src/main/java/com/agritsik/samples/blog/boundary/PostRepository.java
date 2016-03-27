package com.agritsik.samples.blog.boundary;

import com.agritsik.samples.blog.entity.Post;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface PostRepository extends PagingAndSortingRepository<Post, Long> {
    List<Post> findByTitle(String name);
}
