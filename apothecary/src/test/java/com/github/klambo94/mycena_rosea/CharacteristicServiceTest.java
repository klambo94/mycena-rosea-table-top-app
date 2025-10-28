package com.github.klambo94.mycena_rosea;

import com.github.klambo94.mycena_rosea.domain.dao.CharProp;
import com.github.klambo94.mycena_rosea.domain.dao.Characteristic;
import com.github.klambo94.mycena_rosea.services.CharacteristicService;
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
class CharacteristicServiceTest {

    @Autowired
    private CharacteristicService characteristicService;

    static Characteristic characteristic;

    @BeforeAll
    static void setUp() {
        CharProp prop1 = new CharProp("Test Prop 1", "Test Value 1");
        CharProp prop2 = new CharProp("Test Prop 2", "Test Value 2");

        characteristic = new Characteristic();
        characteristic.setName("Test Characteristic");
        characteristic.setCharProperties(new HashSet<>(Set.of(prop1, prop2)));
    }

    @Test
    void contextLoads() {
        // Verify that CharacteristicService was autowired successfully
        assert characteristicService != null;
    }

    @Test
    void testSaveCharacteristic() {
        characteristic = characteristicService.saveCharacteristic(characteristic);

        log.info("Saved characteristic with id: {}", characteristic.getId());
        assert characteristic.getId() != null;
    }

    @Test
    void testSaveAllCharacteristics() {
        CharProp prop1 = new CharProp("Prop A", "Value A");
        CharProp prop2 = new CharProp("Prop B", "Value B");
        CharProp prop3 = new CharProp("Prop C", "Value C");

        Characteristic char1 = new Characteristic("Characteristic 1", new HashSet<>(Set.of(prop1)));
        Characteristic char2 = new Characteristic("Characteristic 2", new HashSet<>(Set.of(prop2)));
        Characteristic char3 = new Characteristic("Characteristic 3", new HashSet<>(Set.of(prop3)));

        Set<Characteristic> characteristics = new HashSet<>(Set.of(char1, char2, char3));

        Set<Characteristic> savedCharacteristics = characteristicService.saveAllCharacteristics(characteristics);

        assert savedCharacteristics.size() == 3;
        savedCharacteristics.forEach(ch -> {
            log.info("Saved characteristic: {} with id: {}", ch.getName(), ch.getId());
            assert ch.getId() != null;
        });
    }

    @Test
    void testSaveCharacteristicWithNullThrowsException() {
        try {
            characteristicService.saveCharacteristic(null);
            assert false : "Expected IllegalArgumentException";
        } catch (IllegalArgumentException e) {
            assert e.getMessage().contains("Characteristic is null");
            log.info("Correctly threw exception: {}", e.getMessage());
        }
    }

    @Test
    void testSaveAllCharacteristicsWithEmptySetThrowsException() {
        try {
            characteristicService.saveAllCharacteristics(new HashSet<>());
            assert false : "Expected IllegalArgumentException";
        } catch (IllegalArgumentException e) {
            assert e.getMessage().contains("Characteristics are empty");
            log.info("Correctly threw exception: {}", e.getMessage());
        }
    }

    @Test
    void testFindCharacteristicById() {
        characteristic = characteristicService.saveCharacteristic(characteristic);

        Characteristic fetchedChar = characteristicService.findItemById(characteristic.getId());

        assert fetchedChar != null;
        assert fetchedChar.getId().equals(characteristic.getId());
        assert fetchedChar.getName().equals(characteristic.getName());
        assert fetchedChar.getCharProperties().size() == characteristic.getCharProperties().size();

        log.info("Fetched characteristic: {}", fetchedChar.getName());
    }

    @Test
    void testFindCharacteristicByIdWithNullThrowsException() {
        try {
            characteristicService.findItemById(null);
            assert false : "Expected IllegalArgumentException";
        } catch (IllegalArgumentException e) {
            assert e.getMessage().contains("Characteristic id is null");
            log.info("Correctly threw exception: {}", e.getMessage());
        }
    }

    @Test
    void testFindCharacteristicByName() {
        characteristic = characteristicService.saveCharacteristic(characteristic);

        Characteristic fetchedChar = characteristicService.findItemByName(characteristic.getName());

        assert fetchedChar != null;
        assert fetchedChar.getId().equals(characteristic.getId());
        assert fetchedChar.getName().equals(characteristic.getName());

        log.info("Fetched characteristic by name: {}", fetchedChar.getName());
    }

    @Test
    void testFindAllCharacteristics() {
        log.info("Testing fetch all characteristics");
        characteristic = characteristicService.saveCharacteristic(characteristic);

        Characteristic char2 = new Characteristic();
        char2.setName("Test Characteristic 2");
        char2.setCharProperties(new HashSet<>(Set.of(new CharProp("Prop X", "Value X"))));
        char2 = characteristicService.saveCharacteristic(char2);
        log.info("Saved characteristic with id: {}", char2.getId());

        Characteristic char3 = new Characteristic();
        char3.setName("Test Characteristic 3");
        char3.setCharProperties(new HashSet<>(Set.of(new CharProp("Prop Y", "Value Y"))));
        char3 = characteristicService.saveCharacteristic(char3);
        log.info("Saved characteristic with id: {}", char3.getId());

        Iterable<Characteristic> characteristics = characteristicService.findAllCharacteristics();

        assert characteristics.iterator().hasNext();
        characteristics.forEach(c -> log.info("Characteristic: {}", c.getName()));
    }

    @Test
    void testUpdateCharacteristic() {
        characteristic = characteristicService.saveCharacteristic(characteristic);

        CharProp newProp = new CharProp("New Prop", "New Value");
        characteristic.getCharProperties().add(newProp);

        characteristic = characteristicService.updateItem(characteristic);

        assert characteristic.getCharProperties().size() == 3;

        log.info("Updated characteristic with id: {}", characteristic.getId());
        log.info("Updated characteristic now has {} properties", characteristic.getCharProperties().size());
    }

    @Test
    void testUpdateCharacteristicCreatesNewIfNotFound() {
        CharProp prop = new CharProp("Unique Prop", "Unique Value");
        Characteristic newChar = new Characteristic("New Characteristic", new HashSet<>(Set.of(prop)));

        Characteristic savedChar = characteristicService.updateItem(newChar);

        assert savedChar.getId() != null;
        assert savedChar.getName().equals("New Characteristic");

        log.info("Created new characteristic during update with id: {}", savedChar.getId());
    }

    @Test
    void testUpdateCharacteristicWithNullThrowsException() {
        try {
            characteristicService.updateItem(null);
            assert false : "Expected IllegalArgumentException";
        } catch (IllegalArgumentException e) {
            assert e.getMessage().contains("Characteristic is null");
            log.info("Correctly threw exception: {}", e.getMessage());
        }
    }

    @Test
    void testUpdateCharacteristicWithNullNameThrowsException() {
        Characteristic charWithoutName = new Characteristic();
        charWithoutName.setCharProperties(new HashSet<>(Set.of(new CharProp("Prop", "Value"))));

        try {
            characteristicService.updateItem(charWithoutName);
            assert false : "Expected IllegalArgumentException";
        } catch (IllegalArgumentException e) {
            assert e.getMessage().contains("Characteristic is null");
            log.info("Correctly threw exception: {}", e.getMessage());
        }
    }

    @Test
    void testDeleteCharacteristic() {
        characteristic = characteristicService.saveCharacteristic(characteristic);
        Long id = characteristic.getId();

        characteristicService.deleteItem(id);

        Characteristic fetchedChar = characteristicService.findItemById(id);
        assert fetchedChar == null;

        log.info("Successfully deleted characteristic with id: {}", id);
    }

    @Test
    void testDeleteCharacteristicWithNullIdThrowsException() {
        try {
            characteristicService.deleteItem(null);
            assert false : "Expected IllegalArgumentException";
        } catch (IllegalArgumentException e) {
            assert e.getMessage().contains("Item id is null");
            log.info("Correctly threw exception: {}", e.getMessage());
        }
    }
}