package com.github.klambo94.mycena_rosea.services;

import com.github.klambo94.mycena_rosea.domain.dao.Tag;
import com.github.klambo94.mycena_rosea.repositories.TagRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
public class TagService {

    private final TagRepository tagRepository;

    public TagService(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    public Set<Tag> saveAllTags(Set<Tag> tags) {
        log.info("Saving all tags {}", tags);

        if(CollectionUtils.isEmpty(tags)) {
            throw new IllegalArgumentException("Tags are empty, unable to save object.");
        }
        return new HashSet<>(tagRepository.saveAll(tags));
    }

    public Tag saveTag(Tag tag) {
        log.info("Saving tag {}", tag);

        if(tag == null) {
            throw new IllegalArgumentException("Tag is null, unable to save object.");
        }
        return tagRepository.save(tag);
    }

    public Tag findTagById(Long id) {
        if(id == null) {
            throw new IllegalArgumentException("Tag id is null, unable to retrieve object.");
        }
        return tagRepository.findById(id).orElse(null);
    }

    public Tag findTagByName(String tagName) {
        log.info("Fetching tag with name: {}", tagName);
        return tagRepository.findTagByTagName(tagName);
    }

    public List<Tag> findAllTags() {
        log.info("Fetching all tags");
        return tagRepository.findAll();
    }

    public Tag updateTag(Tag tag) {
        log.info("Updating tag {}", tag);

        if(tag == null || tag.getTagName() == null) {
            throw new IllegalArgumentException("Tag is null, unable to update object.");
        }

        // Check if the tag exists by name (since name should be unique)
        Tag existingTag = tagRepository.findTagByTagName(tag.getTagName());

        if(existingTag != null) {
            log.info("Tag found by name '{}', updating with id: {}", existingTag.getTagName(), existingTag.getId());
            // Tag only has name, so if it exists, we just return it
            return existingTag;
        } else {
            log.info("Tag not found, creating new one");
            // Clear the ID to ensure JPA treats it as a new entity
            tag.setId(null);
            return tagRepository.save(tag);
        }
    }

    public void deleteTag(Long id) {
        log.info("Deleting tag with id: {}", id);
        if(id == null) {
            throw new IllegalArgumentException("Tag id is null, unable to delete object.");
        }
        tagRepository.deleteById(id);
        log.info("Tag deleted");
    }
}