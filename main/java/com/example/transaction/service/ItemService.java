package com.example.transaction.service;

import com.example.transaction.dto.ItemForm;
import com.example.transaction.entity.Item;
import com.example.transaction.entity.Users;
import com.example.transaction.repository.ItemRepository;
import com.example.transaction.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.User;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class ItemService {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    // 생성자 주입
    public ItemService(ItemRepository itemRepository, UserRepository userRepository){
        this.itemRepository = itemRepository;
        this.userRepository = userRepository;
    }

    // 아이템 저장
    public void saveNewItem(ItemForm itemForm){
        Date today = new Date();
        SimpleDateFormat date = new SimpleDateFormat("yyyy/MM/dd");

        Item item = itemForm.toEntity(date.format(today));
        itemRepository.save(item);
    }

    // 아이템 탐색
    public Item searchItem(Long id){
        return itemRepository.findById(id).orElse(null);
    }

    // 아이템 form 업데이트
    public Item updateItem(ItemForm itemForm){
        Item item = itemForm.toEntity();

        log.info(item.getName());
        log.info(item.getDescription());

        Item target = itemRepository.findById(item.getId()).orElse(null);

        if (target != null) {
            itemRepository.save(item);
        }
        return target;
    }


    // 아이템 삭제
    public void deleteItem(Long id, RedirectAttributes rttr){
        Item target = itemRepository.findById(id).orElse(null);

        if (target != null) {
            itemRepository.deleteById(id);
            rttr.addFlashAttribute("msg", "삭제되었습니다.");
        }
    }

    // 아이템 가공
    public Map<String, Object> processItem(Long itemId, String dataKey, Long dataValue){
        Item item = itemRepository.findById(itemId).orElse(null);
        if(item == null) return null;

        Map<String, Object> itemData = new HashMap<>();
        itemData.put("id", item.getId());
        itemData.put("name", item.getName());
        itemData.put("price", item.getPrice());
        itemData.put("description", item.getDescription());
        itemData.put("seller", item.getSeller());
        itemData.put("seller_id", item.getSellerId());
        itemData.put("created_date", item.getCreated_date());
        itemData.put("onsale", item.isOnsale());
        itemData.put(dataKey, item.getSellerId() == dataValue);
        return itemData;
    }


    // 아이템 판매 상태 업데이트
    public void setSellItem(Long id,Boolean isOnSale){
        Item item = itemRepository.findById(id).orElse(null);
        // 판매 상태로 변경
        item.setOnsale(isOnSale);
        itemRepository.save(item);
    }

    // 아이템 구매
    public void buyItem(Long id, Users user){
        Item item = itemRepository.findById(id).orElse(null);

        if (user.getBalance() < item.getPrice()) {
            throw new IllegalArgumentException("마일리지가 부족합니다.");
        }

        // 구매 처리
        user.subtractBalance(item.getPrice());
        userRepository.save(user);

        // 판매자에게 마일리지 지급
        Users seller = userRepository.findById(item.getSellerId()).orElse(null);
        if (seller != null) {
            seller.addBalance(item.getPrice());
            userRepository.save(seller);
        }

        // 아이템 상태 업데이트
        item.setOnsale(false);
        item.setBuyerId(user.getId());  // 구매자 ID 저장
        itemRepository.save(item);
    }
}
