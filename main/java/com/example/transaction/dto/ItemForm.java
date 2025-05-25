package com.example.transaction.dto;


import com.example.transaction.entity.Item;
import lombok.*;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @ToString
public class ItemForm {
    private Long id;
    private String name;
    private int price;
    private String path;
    private String description;
    private String seller;
    private Long seller_id;
    private String created_date;
    private boolean onsale;

    public Item toEntity() {
        if (id == null) {
            return new Item(name, price, path, description, seller, seller_id, created_date, false);
        } else {
            return new Item(id, name, price, path, description, seller, seller_id, created_date, false, null);
        }
    }

    public Item toEntity(String created_date){
        return new Item(name, price, path, description, seller, seller_id, created_date, false);
    }
}
