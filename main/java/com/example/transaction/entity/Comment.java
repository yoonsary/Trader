package com.example.transaction.entity;

import jakarta.persistence.*;
import lombok.*;


@Entity
@ToString @NoArgsConstructor @AllArgsConstructor
@Getter @Setter
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    private Long user_id;
    private Long item_id;
    private String content;

    @ManyToOne
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private Users user; // 연관 관계 필드 추가

    public Comment(Long user_id, Long item_id, String content){
        this.user_id = user_id;
        this.item_id = item_id;
        this.content = content;
    }
}
