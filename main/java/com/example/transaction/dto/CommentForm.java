package com.example.transaction.dto;


import com.example.transaction.entity.Comment;
import lombok.*;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @ToString
public class CommentForm {
    private Long user_id;
    private Long item_id;
    private String content;

    public Comment toEntity(){
        return new Comment(user_id, item_id, content);
    }
}
