package com.github.klambo94.mycena_rosea;

import com.github.klambo94.mycena_rosea.domain.dao.CharProp;
import com.github.klambo94.mycena_rosea.services.CharPropService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashSet;
import java.util.Set;

@Slf4j
@SpringBootTest
@Transactional
class CharPropServicesTest {

    @Autowired
    private CharPropService charPropService;
    
    static CharProp charProp;
    
    @BeforeAll
    static void setUp() {
        charProp = new CharProp();
        charProp.setName("Test Property");
        charProp.setValue("Test Value");
    }

    @Test
    void contextLoads() {
        // Verify that CharPropService was autowired successfully
        assert charPropService != null;
    }

    @Test
    void testSaveProperty() {
        charProp = charPropService.saveProperty(charProp);
        
        log.info("Saved property with id: {}", charProp.getId());
        assert charProp.getId() != null;
    }

    @Test
    void testSaveAllProperties() {
        CharProp prop1 = new CharProp("Property 1", "Value 1");
        CharProp prop2 = new CharProp("Property 2", "Value 2");
        CharProp prop3 = new CharProp("Property 3", "Value 3");
        
        Set<CharProp> properties = new HashSet<>(Set.of(prop1, prop2, prop3));
        
        Set<CharProp> savedProperties = charPropService.saveAllProperties(properties);
        
        assert savedProperties.size() == 3;
        savedProperties.forEach(prop -> {
            log.info("Saved property: {} with id: {}", prop.getName(), prop.getId());
            assert prop.getId() != null;
        });
    }

    @Test
    void testSavePropertyWithNullThrowsException() {
        try {
            charPropService.saveProperty(null);
            assert false : "Expected IllegalArgumentException";
        } catch (IllegalArgumentException e) {
            assert e.getMessage().contains("Property is null");
            log.info("Correctly threw exception: {}", e.getMessage());
        }
    }

    @Test
    void testSaveAllPropertiesWithEmptySetThrowsException() {
        try {
            charPropService.saveAllProperties(new HashSet<>());
            assert false : "Expected IllegalArgumentException";
        } catch (IllegalArgumentException e) {
            assert e.getMessage().contains("Properties are empty");
            log.info("Correctly threw exception: {}", e.getMessage());
        }
    }

    @Test
    void testFindPropertyById() {
        charProp = charPropService.saveProperty(charProp);
        
        CharProp fetchedProp = charPropService.findItemById(charProp.getId());
        
        assert fetchedProp != null;
        assert fetchedProp.getId().equals(charProp.getId());
        assert fetchedProp.getName().equals(charProp.getName());
        assert fetchedProp.getValue().equals(charProp.getValue());
        
        log.info("Fetched property: {}", fetchedProp.getName());
    }

    @Test
    void testFindPropertyByIdWithNullThrowsException() {
        try {
            charPropService.findItemById(null);
            assert false : "Expected IllegalArgumentException";
        } catch (IllegalArgumentException e) {
            assert e.getMessage().contains("Property id is null");
            log.info("Correctly threw exception: {}", e.getMessage());
        }
    }

    @Test
    void testFindPropertyByName() {
        charProp = charPropService.saveProperty(charProp);
        
        CharProp fetchedProp = charPropService.findItemByName(charProp.getName());
        
        assert fetchedProp != null;
        assert fetchedProp.getId().equals(charProp.getId());
        assert fetchedProp.getName().equals(charProp.getName());
        
        log.info("Fetched property by name: {}", fetchedProp.getName());
    }

    @Test
    void testUpdateProperty() {
        charProp = charPropService.saveProperty(charProp);
        
        charProp.setValue("Updated Value");
        charProp = charPropService.updateItem(charProp);
        
        assert charProp.getValue().equals("Updated Value");
        
        log.info("Updated property with id: {}", charProp.getId());
        log.info("Updated property with value: {}", charProp.getValue());
    }

    @Test
    void testUpdatePropertyCreatesNewIfNotFound() {
        CharProp newProp = new CharProp("New Property", "New Value");
        newProp.setId(999L); // Non-existent ID
        
        CharProp savedProp = charPropService.updateItem(newProp);
        
        assert savedProp.getId() != null;
        assert savedProp.getName().equals("New Property");
        
        log.info("Created new property during update with id: {}", savedProp.getId());
    }

    @Test
    void testUpdatePropertyWithNullThrowsException() {
        try {
            charPropService.updateItem(null);
            assert false : "Expected IllegalArgumentException";
        } catch (IllegalArgumentException e) {
            assert e.getMessage().contains("Property is null");
            log.info("Correctly threw exception: {}", e.getMessage());
        }
    }

    @Test
    void testDeleteProperty() {
        charProp = charPropService.saveProperty(charProp);
        Long id = charProp.getId();
        
        charPropService.deleteItem(id);
        
        CharProp fetchedProp = charPropService.findItemById(id);
        assert fetchedProp == null;
        
        log.info("Successfully deleted property with id: {}", id);
    }

    @Test
    void testDeletePropertyWithNullIdThrowsException() {
        try {
            charPropService.deleteItem(null);
            assert false : "Expected IllegalArgumentException";
        } catch (IllegalArgumentException e) {
            assert e.getMessage().contains("Item id is null");
            log.info("Correctly threw exception: {}", e.getMessage());
        }
    }
}
