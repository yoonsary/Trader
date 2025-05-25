package com.example.transaction.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@ToString @NoArgsConstructor @AllArgsConstructor @Getter @Setter
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    private String name;
    private int price;
    private String path;
    private String description;
    private String seller;
    private Long sellerId;
    private String created_date;
    private boolean onsale;
    private Long buyerId;

    public Item(String name, int price, String path, String description, String seller, Long seller_id, String created_date, boolean onsale){
        this.name = name;
        this.price = price;
        this.path = path;
        this.description = description;
        this.seller = seller;
        this.sellerId = seller_id;
        this.created_date = created_date;
        this.onsale = onsale;
        this.buyerId = buyerId;
    }
}
