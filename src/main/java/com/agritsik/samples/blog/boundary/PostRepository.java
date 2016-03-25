package com.agritsik.samples.blog.boundary;

import com.agritsik.samples.blog.entity.Post;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface PostRepository extends PagingAndSortingRepository<Post, Long> {
}
