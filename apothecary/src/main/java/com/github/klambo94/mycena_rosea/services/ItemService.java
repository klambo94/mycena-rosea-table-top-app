package com.github.klambo94.mycena_rosea.services;

import com.github.klambo94.mycena_rosea.domain.dao.Item;
import com.github.klambo94.mycena_rosea.repositories.ItemRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;

@Slf4j
@Service
public class ItemService {

    private final ItemRepository itemRepository;

    public ItemService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    public Set<Item> saveAllItems(Set<Item> items) {
        log.info("Saving all items {}", items);

        if(CollectionUtils.isEmpty(items)) {
            throw new IllegalArgumentException("Items is empty, unable to save object.");
        }
        return new HashSet<>(itemRepository.saveAll(items));
    }
    public Item saveItem(Item item) {
        log.info("Saving item {}", item);

        if(item == null) {
            throw new IllegalArgumentException("Item is null, unable to save object.");
        }
        return itemRepository.save(item);
    }

    public Item findItemById(Long id) {
        if(id == null) {
            throw new IllegalArgumentException("Item id is null, unable to retrieve object.");
        }
        return itemRepository.findById(id).orElse(null);
    }

    public Item findItemByName(String name) {
        log.info("Fetching item with name: {}", name);
        return itemRepository.findItemByName(name);
    }

    public Iterable<Item> findAllItems() {
        log.info("Fetching all items and fully hydrating");
        return itemRepository.findAllWithFullyHydrated();
    }

    public Iterable<Item> findAllItemsWithCharacteristics() {
        log.info("Fetching all items and with characteristics");
        return itemRepository.findAllWithCharacteristics();
    }

    public Item updateItem(Item item) {
        log.info("Updating item {}", item);

        if(item == null || item.getId() == null) {
            throw new IllegalArgumentException("Item is null, unable to update object.");
        }

        log.info("Fetching existing item with id: {}", item.getId());
        Optional<Item> existingItem = itemRepository.findById(item.getId());

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
