package com.agritsik.samples.blog.boundary;

import com.agritsik.samples.blog.entity.Post;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
@Transactional // todo: why is it mandatory for spring?
public class PostService {

    @PersistenceContext
    EntityManager entityManager;

    public void create(Post post){
        entityManager.persist(post);
    }

    public Post find(long id){
        return entityManager.find(Post.class, id);
    }

    public List<Post> find(int first, int maxResult){
        return entityManager.createNamedQuery(Post.FIND_ALL)
                .setFirstResult(first).setMaxResults(maxResult).getResultList();
    }

    public Post update(Post post){
        return entityManager.merge(post);
    }

    public void delete(long id){
        Post post = entityManager.find(Post.class, id);
        entityManager.remove(post);
    }

}
