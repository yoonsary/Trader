package com.example.transaction.repository;

import com.example.transaction.entity.Item;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemRepository extends CrudRepository<Item, Long> {
    @Override
    List<Item> findAll();

    List<Item> findBySellerId(Long sellerId);

    List<Item> findByBuyerId(Long buyerId);

    List<Item> findBySellerIdAndOnsaleTrue(Long sellerId);
}
