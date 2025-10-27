package com.github.klambo94.mycena_rosea;

import com.github.klambo94.mycena_rosea.domain.Type;
import com.github.klambo94.mycena_rosea.domain.dao.CharProp;
import com.github.klambo94.mycena_rosea.domain.dao.Characteristic;
import com.github.klambo94.mycena_rosea.domain.dao.Item;
import com.github.klambo94.mycena_rosea.domain.dao.Tag;
import com.github.klambo94.mycena_rosea.repositories.ItemRepository;
import com.github.klambo94.mycena_rosea.service.ItemService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.Set;

@Slf4j
@SpringBootTest
@Transactional
class ItemServicesTest {

    @Autowired
    private ItemService itemService;


    @MockitoBean
    private ItemRepository itemRepository;

    @Test
    void contextLoads() {
        // Verify that ItemService was autowired successfully
        assert itemService != null;
    }

    @Test void testFetchAllItems() {
        log.info("Testing fetch all items");
        Iterable<Item> items = itemService.getAllItems();

        assert items != null;
        assert items.iterator().hasNext();
        assert items.iterator().next() != null;
        assert items.iterator().next().getItemId() != null;
        assert items.iterator().next().getName() != null;
        assert items.iterator().next().getDescription() != null;
        assert items.iterator().next().getItemType() != null;
        assert items.iterator().next().getQuantity() >= 1;
        assert items.iterator().next().getPrice() >= 0;
        assert items.iterator().next().getCharacteristics() != null;
        assert items.iterator().next().getTags() != null;



    }

    @Test void testFetchItemById() {

    }

    @Test void testFetchItemByName() {

    }

    @Test void testSaveItem() {

    }

    @Test void testDeleteItem() {

    }

    @Test void testUpdateItem() {

    }

    @Test void testFetchAllTypes() {

    }
}