
package com.github.klambo94.mycena_rosea;

import com.github.klambo94.mycena_rosea.domain.Type;
import com.github.klambo94.mycena_rosea.domain.dao.CharProp;
import com.github.klambo94.mycena_rosea.domain.dao.Characteristic;
import com.github.klambo94.mycena_rosea.domain.dao.Item;
import com.github.klambo94.mycena_rosea.domain.dao.Tag;
import com.github.klambo94.mycena_rosea.services.ItemService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashSet;
import java.util.Set;

@Slf4j
@SpringBootTest
@Transactional
class ItemServicesTest {

    @Autowired
    private ItemService itemService;

    private Item item;

    @BeforeEach
    void setUp() {
        item = new Item();
        item.setName("Test Item");
        item.setDescription("Test Item Description");
        item.setItemType(Type.POTION);
        item.setPrice(10.0);
        item.setQuantity(2);

        CharProp prop = new CharProp("Test Prop", "Test Value");
        CharProp prop2 = new CharProp("Test Prop 2", "Test Value 2");
        CharProp prop3 = new CharProp("Test Prop 3", "Test Value 3");
        CharProp prop4 = new CharProp("Test Prop 4", "Test Value 4");

        Characteristic characteristic = new Characteristic("test char name",
                new HashSet<>(Set.of(prop, prop2)));
        Characteristic characteristic2 = new Characteristic("test char 2 name",
                new HashSet<>(Set.of(prop3, prop4)));
        item.setCharacteristics(new HashSet<>(Set.of(characteristic, characteristic2)));

        Tag tag = new Tag("test tag");
        Tag tag2 = new Tag("Tag 2");
        item.setTags(new HashSet<>(Set.of(tag, tag2)));
    }

    @Test
    void contextLoads() {
        // Verify that ItemService was autowired successfully
        assert itemService != null;
    }

    @Test
    void testSaveItem() {
        item = itemService.saveItem(item);

        log.info("Saved item with id: {}", item.getId());
        assert item.getId() != null;
    }

    @Test
    void testSaveAllItems() {
        Item item1 = new Item();
        item1.setName("Item 1");
        item1.setPrice(5.0);
        item1.setItemType(Type.POTION);

        Item item2 = new Item();
        item2.setName("Item 2");
        item2.setPrice(15.0);
        item2.setItemType(Type.INGREDIENT);

        Item item3 = new Item();
        item3.setName("Item 3");
        item3.setPrice(25.0);
        item3.setItemType(Type.POTION);

        Set<Item> items = new HashSet<>(Set.of(item1, item2, item3));

        Set<Item> savedItems = itemService.saveAllItems(items);

        assert savedItems.size() == 3;
        savedItems.forEach(i -> {
            log.info("Saved item: {} with id: {}", i.getName(), i.getId());
            assert i.getId() != null;
        });
    }

    @Test
    void testSaveItemWithNullThrowsException() {
        try {
            itemService.saveItem(null);
            assert false : "Expected IllegalArgumentException";
        } catch (IllegalArgumentException e) {
            assert e.getMessage().contains("Item is null");
            log.info("Correctly threw exception: {}", e.getMessage());
        }
    }

    @Test
    void testSaveAllItemsWithEmptySetThrowsException() {
        try {
            itemService.saveAllItems(new HashSet<>());
            assert false : "Expected IllegalArgumentException";
        } catch (IllegalArgumentException e) {
            assert e.getMessage().contains("Items is empty");
            log.info("Correctly threw exception: {}", e.getMessage());
        }
    }

    @Test
    void testFetchItemById() {
        item = itemService.saveItem(item);

        Item fetchedItem = itemService.findItemById(item.getId());

        assert fetchedItem != null;
        assert fetchedItem.getId().equals(item.getId());
        assert fetchedItem.getName().equals(item.getName());
        assert fetchedItem.getPrice() == item.getPrice();

        log.info("Fetched item: {}", fetchedItem.getName());
    }

    @Test
    void testFetchItemByIdWithNullThrowsException() {
        try {
            itemService.findItemById(null);
            assert false : "Expected IllegalArgumentException";
        } catch (IllegalArgumentException e) {
            assert e.getMessage().contains("Item id is null");
            log.info("Correctly threw exception: {}", e.getMessage());
        }
    }

    @Test
    void testFetchItemByName() {
        item = itemService.saveItem(item);

        Item fetchedItem = itemService.findItemByName(item.getName());

        assert fetchedItem != null;
        assert fetchedItem.getId().equals(item.getId());
        assert fetchedItem.getName().equals(item.getName());

        log.info("Fetched item by name: {}", fetchedItem.getName());
    }

    @Test
    void testFetchAllItems() {
        log.info("Testing fetch all items");
        item = itemService.saveItem(item);

        Item item2 = new Item();
        item2.setName("Test Item 2");
        item2.setPrice(20.0);
        item2.setItemType(Type.INGREDIENT);
        item2 = itemService.saveItem(item2);
        log.info("Saved item with id: {}", item2.getId());

        Item item3 = new Item();
        item3.setName("Test Item 3");
        item3.setPrice(30.0);
        item3.setItemType(Type.POTION);
        item3 = itemService.saveItem(item3);
        log.info("Saved item with id: {}", item3.getId());

        Iterable<Item> items = itemService.findAllItems();

        assert items.iterator().hasNext();
        items.forEach(i -> log.info("Item: {} - Price: {}", i.getName(), i.getPrice()));
    }

    @Test
    void testFetchAllItemsWithCharacteristics() {
        log.info("Testing fetch all items with characteristics");
        item = itemService.saveItem(item);

        Iterable<Item> items = itemService.findAllItemsWithCharacteristics();

        assert items.iterator().hasNext();
        items.forEach(i -> {
            log.info("Item: {} with {} characteristics", i.getName(),
                    i.getCharacteristics() != null ? i.getCharacteristics().size() : 0);
        });
    }

    @Test
    void testUpdateItem() {
        item = itemService.saveItem(item);

        item.setName("Updated Item");
        item.setPrice(50.0);
        item.setDescription("Updated Item Description");

        item = itemService.updateItem(item);

        assert item.getName().equals("Updated Item");
        assert item.getPrice() == 50.0;
        assert item.getDescription().equals("Updated Item Description");

        log.info("Updated item with id: {}", item.getId());
        log.info("Updated item with name: {}", item.getName());
        log.info("Updated item with price: {}", item.getPrice());
    }

    @Test
    void testUpdateItemCreatesNewIfNotFound() {
        Item newItem = new Item();
        newItem.setName("Brand New Item");
        newItem.setPrice(100.0);
        newItem.setItemType(Type.POTION);

        Item savedItem = itemService.updateItem(newItem);

        assert savedItem.getId() != null;
        assert savedItem.getName().equals("Brand New Item");
        assert savedItem.getPrice() == 100.0;

        log.info("Created new item during update with id: {}", savedItem.getId());
    }

    @Test
    void testUpdateItemWithNullThrowsException() {
        try {
            itemService.updateItem(null);
            assert false : "Expected IllegalArgumentException";
        } catch (IllegalArgumentException e) {
            assert e.getMessage().contains("Item is null");
            log.info("Correctly threw exception: {}", e.getMessage());
        }
    }

    @Test
    void testUpdateItemWithNullIdThrowsException() {
        Item itemWithoutId = new Item();
        itemWithoutId.setName("Item Without ID");
        itemWithoutId.setPrice(10.0);

        try {
            itemService.updateItem(itemWithoutId);
            assert false : "Expected IllegalArgumentException";
        } catch (IllegalArgumentException e) {
            assert e.getMessage().contains("Item is null");
            log.info("Correctly threw exception: {}", e.getMessage());
        }
    }

    @Test
    void testDeleteItem() {
        item = itemService.saveItem(item);
        Long id = item.getId();

        itemService.deleteItem(id);

        Item fetchedItem = itemService.findItemById(id);
        assert fetchedItem == null;

        log.info("Successfully deleted item with id: {}", id);
    }

    @Test
    void testDeleteItemWithNullIdThrowsException() {
        try {
            itemService.deleteItem(null);
            assert false : "Expected IllegalArgumentException";
        } catch (IllegalArgumentException e) {
            assert e.getMessage().contains("Item id is null");
            log.info("Correctly threw exception: {}", e.getMessage());
        }
    }
}