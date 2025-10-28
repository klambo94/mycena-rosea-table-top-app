package com.github.klambo94.mycena_rosea.services;

import com.github.klambo94.mycena_rosea.domain.dao.Characteristic;
import com.github.klambo94.mycena_rosea.repositories.CharacteristicRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
public class CharacteristicService {

    private final CharacteristicRepository characteristicRepository;

    public CharacteristicService(CharacteristicRepository characteristicRepository) {
        this.characteristicRepository = characteristicRepository;
    }

    public Set<Characteristic> saveAllCharacteristics(Set<Characteristic> characteristics) {
        log.info("Saving all characteristics {}", characteristics);

        if(CollectionUtils.isEmpty(characteristics)) {
            throw new IllegalArgumentException("Characteristics are empty, unable to save object.");
        }
        return new HashSet<>(characteristicRepository.saveAll(characteristics));
    }

    public Characteristic saveCharacteristic(Characteristic characteristic) {
        log.info("Saving characteristic {}", characteristic);

        if(characteristic == null) {
            throw new IllegalArgumentException("Characteristic is null, unable to save object.");
        }
        return characteristicRepository.save(characteristic);
    }

    public Characteristic findItemById(Long id) {
        if(id == null) {
            throw new IllegalArgumentException("Characteristic id is null, unable to retrieve object.");
        }
        return characteristicRepository.findById(id).orElse(null);
    }

    public Characteristic findItemByName(String name) {
        log.info("Fetching characteristic with name: {}", name);
        return characteristicRepository.findCharacteristicByName(name);
    }

    public List<Characteristic> findAllCharacteristics() {
        log.info("Fetching all characteristics with properties");
        return characteristicRepository.findAllWithProperties();
    }

    public Characteristic updateItem(Characteristic characteristic) {
        log.info("Updating characteristic {}", characteristic);

        if(characteristic == null || characteristic.getName() == null) {
            throw new IllegalArgumentException("Characteristic is null, unable to update object.");
        }

        // Check if the characteristic exists by name (since name should be unique)
        Characteristic existingCharacteristic = characteristicRepository.findCharacteristicByName(characteristic.getName());

        if(existingCharacteristic != null) {
            log.info("Characteristic found by name '{}', updating with id: {}", existingCharacteristic.getName(), existingCharacteristic.getId());
            // Update the existing characteristic's properties
            existingCharacteristic.setCharProperties(characteristic.getCharProperties());
            return characteristicRepository.save(existingCharacteristic);
        } else {
            log.info("Characteristic not found, creating new one");
            // Clear the ID to ensure JPA treats it as a new entity
            characteristic.setId(null);
            return characteristicRepository.save(characteristic);
        }
    }

    public void deleteItem(Long id) {
        log.info("Deleting characteristic with id: {}", id);
        if(id == null) {
            throw new IllegalArgumentException("Item id is null, unable to delete object.");
        }
        characteristicRepository.deleteById(id);
        log.info("Characteristic deleted");
    }
}
