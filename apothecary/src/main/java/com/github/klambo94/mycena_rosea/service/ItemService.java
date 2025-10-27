package com.github.klambo94.mycena_rosea.service;

import com.github.klambo94.mycena_rosea.domain.dao.Item;
import com.github.klambo94.mycena_rosea.repositories.ItemRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class ItemService {

    private final ItemRepository itemRepository;

    public ItemService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    public void saveItem(Item item) {
        log.info("Saving item {}", item);

        if(item == null) {
            throw new IllegalArgumentException("Item is null, unable to save object.");
        }
        itemRepository.save(item);
    }

    public Item getItemById(Long id) {
        if(id == null) {
            throw new IllegalArgumentException("Item id is null, unable to retrieve object.");
        }
        return itemRepository.findById(id).orElseThrow(IllegalArgumentException::new);
    }

    public Iterable<Item> getAllItems() {
        log.info("Fetching all items and fully hydrating");
        return itemRepository.findAllWithFullyHydrated();
    }

    public Iterable<Item> getAllItemsWithCharacteristics() {
        log.info("Fetching all items and with characteristics");
        return itemRepository.findAllWithCharacteristics();
    }

    public Item updateItem(Item item) {
        log.info("Updating item {}", item);

        if(item == null || item.getItemId() == null) {
            throw new IllegalArgumentException("Item is null, unable to update object.");
        }

        log.info("Fetching existing item with id: {}", item.getItemId());
        Optional<Item> existingItem = itemRepository.findById(item.getItemId());

        if(existingItem.isPresent()) {
            log.info("Item found, updating");
            Item existing = existingItem.get();
            BeanUtils.copyProperties(item, existing, "itemId");
            return itemRepository.save(existing);
        } else {
            log.info("Item not found, creating new one");
            return itemRepository.save(item);
        }
    }

    public void deleteItem(Long id) {
        log.info("Deleting item with id: {}", id);
        if(id == null) {
            throw new IllegalArgumentException("Item id is null, unable to delete object.");
        }
        itemRepository.deleteById(id);
        log.info("Item deleted");
    }
}
