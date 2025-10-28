package com.github.klambo94.mycena_rosea.services;

import com.github.klambo94.mycena_rosea.domain.dao.CharProp;
import com.github.klambo94.mycena_rosea.repositories.CharPropRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Service
public class CharPropService {

    private final CharPropRepository charPropRepository;

    public CharPropService(CharPropRepository charPropRepository) {
        this.charPropRepository = charPropRepository;
    }

    public Set<CharProp> saveAllProperties(Set<CharProp> properties) {
        log.info("Saving all properties {}", properties);

        if(CollectionUtils.isEmpty(properties)) {
            throw new IllegalArgumentException("Properties are empty, unable to save object.");
        }
        return new HashSet<>(charPropRepository.saveAll(properties));
    }
    public CharProp saveProperty(CharProp property) {
        log.info("Saving property {}", property);

        if(property == null) {
            throw new IllegalArgumentException("Property is null, unable to save object.");
        }
        return charPropRepository.save(property);
    }

    public CharProp findItemById(Long id) {
        if(id == null) {
            throw new IllegalArgumentException("Property id is null, unable to retrieve object.");
        }
        return charPropRepository.findById(id).orElse(null);
    }

    public CharProp findItemByName(String name) {
        log.info("Fetching item with name: {}", name);
        return charPropRepository.findCharPropByName(name);
    }


    public CharProp updateItem(CharProp property) {
        log.info("Updating property {}", property);

        if(property == null || property.getName() == null) {
            throw new IllegalArgumentException("Property is null, unable to update object.");
        }

        // Check if the property exists by name (since name is unique)
        CharProp existingProperty = charPropRepository.findCharPropByName(property.getName());

        if(existingProperty != null) {
            log.info("Property found by name '{}', updating with id: {}", existingProperty.getName(), existingProperty.getId());
            // Copy new values to the existing entity, but preserve the id
            existingProperty.setValue(property.getValue());
            return charPropRepository.save(existingProperty);
        } else {
            log.info("Property not found, creating new one");
            // Clear the ID to ensure JPA treats it as a new entity
            property.setId(null);
            return charPropRepository.save(property);
        }
    }

    public void deleteItem(Long id) {
        log.info("Deleting item with id: {}", id);
        if(id == null) {
            throw new IllegalArgumentException("Item id is null, unable to delete object.");
        }
        charPropRepository.deleteById(id);
        log.info("Item deleted");
    }
}
