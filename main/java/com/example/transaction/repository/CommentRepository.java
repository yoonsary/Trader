package com.example.transaction.repository;

import com.example.transaction.entity.Comment;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CommentRepository extends CrudRepository<Comment, Long> {

    @Override
    List<Comment> findAll();
}
