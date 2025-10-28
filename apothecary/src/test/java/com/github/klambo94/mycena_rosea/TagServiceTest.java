package com.github.klambo94.mycena_rosea;

import com.github.klambo94.mycena_rosea.domain.dao.Tag;
import com.github.klambo94.mycena_rosea.services.TagService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@SpringBootTest
@Transactional
class TagServiceTest {

    @Autowired
    private TagService tagService;

    static Tag tag;

    @BeforeAll
    static void setUp() {
        tag = new Tag();
        tag.setTagName("Test Tag");
    }

    @Test
    void contextLoads() {
        // Verify that TagService was autowired successfully
        assert tagService != null;
    }

    @Test
    void testSaveTag() {
        tag = tagService.saveTag(tag);

        log.info("Saved tag with id: {}", tag.getId());
        assert tag.getId() != null;
    }

    @Test
    void testSaveAllTags() {
        Tag tag1 = new Tag("Tag 1");
        Tag tag2 = new Tag("Tag 2");
        Tag tag3 = new Tag("Tag 3");

        Set<Tag> tags = new HashSet<>(Set.of(tag1, tag2, tag3));

        Set<Tag> savedTags = tagService.saveAllTags(tags);

        assert savedTags.size() == 3;
        savedTags.forEach(t -> {
            log.info("Saved tag: {} with id: {}", t.getTagName(), t.getId());
            assert t.getId() != null;
        });
    }

    @Test
    void testSaveTagWithNullThrowsException() {
        try {
            tagService.saveTag(null);
            assert false : "Expected IllegalArgumentException";
        } catch (IllegalArgumentException e) {
            assert e.getMessage().contains("Tag is null");
            log.info("Correctly threw exception: {}", e.getMessage());
        }
    }

    @Test
    void testSaveAllTagsWithEmptySetThrowsException() {
        try {
            tagService.saveAllTags(new HashSet<>());
            assert false : "Expected IllegalArgumentException";
        } catch (IllegalArgumentException e) {
            assert e.getMessage().contains("Tags are empty");
            log.info("Correctly threw exception: {}", e.getMessage());
        }
    }

    @Test
    void testFindTagById() {
        tag = tagService.saveTag(tag);

        Tag fetchedTag = tagService.findTagById(tag.getId());

        assert fetchedTag != null;
        assert fetchedTag.getId().equals(tag.getId());
        assert fetchedTag.getTagName().equals(tag.getTagName());

        log.info("Fetched tag: {}", fetchedTag.getTagName());
    }

    @Test
    void testFindTagByIdWithNullThrowsException() {
        try {
            tagService.findTagById(null);
            assert false : "Expected IllegalArgumentException";
        } catch (IllegalArgumentException e) {
            assert e.getMessage().contains("Tag id is null");
            log.info("Correctly threw exception: {}", e.getMessage());
        }
    }

    @Test
    void testFindTagByName() {
        tag = tagService.saveTag(tag);

        Tag fetchedTag = tagService.findTagByName(tag.getTagName());

        assert fetchedTag != null;
        assert fetchedTag.getId().equals(tag.getId());
        assert fetchedTag.getTagName().equals(tag.getTagName());

        log.info("Fetched tag by name: {}", fetchedTag.getTagName());
    }

    @Test
    void testFindAllTags() {
        log.info("Testing fetch all tags");
        tag = tagService.saveTag(tag);

        Tag tag2 = new Tag("Test Tag 2");
        tag2 = tagService.saveTag(tag2);
        log.info("Saved tag with id: {}", tag2.getId());

        Tag tag3 = new Tag("Test Tag 3");
        tag3 = tagService.saveTag(tag3);
        log.info("Saved tag with id: {}", tag3.getId());

        List<Tag> tags = tagService.findAllTags();

        assert !tags.isEmpty();
        tags.forEach(t -> log.info("Tag: {}", t.getTagName()));
    }

    @Test
    void testUpdateTag() {
        tag = tagService.saveTag(tag);

        // Since Tag only has tagName, updating with same name should return existing
        Tag updatedTag = tagService.updateTag(tag);

        assert updatedTag.getId().equals(tag.getId());
        assert updatedTag.getTagName().equals(tag.getTagName());

        log.info("Updated tag with id: {}", updatedTag.getId());
    }

    @Test
    void testUpdateTagCreatesNewIfNotFound() {
        Tag newTag = new Tag("New Unique Tag");

        Tag savedTag = tagService.updateTag(newTag);

        assert savedTag.getId() != null;
        assert savedTag.getTagName().equals("New Unique Tag");

        log.info("Created new tag during update with id: {}", savedTag.getId());
    }

    @Test
    void testUpdateTagReturnsExistingIfNameMatches() {
        // Save a tag first
        Tag existingTag = new Tag("Existing Tag");
        existingTag = tagService.saveTag(existingTag);
        log.info("Saved existing tag with id: {}", existingTag.getId());

        // Try to update with a new tag object but same name
        Tag duplicateTag = new Tag("Existing Tag");
        duplicateTag.setId(999L); // Different ID

        Tag result = tagService.updateTag(duplicateTag);

        // Should return the existing tag, not create a new one
        assert result.getId().equals(existingTag.getId());
        assert result.getTagName().equals("Existing Tag");

        log.info("Returned existing tag instead of creating duplicate with id: {}", result.getId());
    }

    @Test
    void testUpdateTagWithNullThrowsException() {
        try {
            tagService.updateTag(null);
            assert false : "Expected IllegalArgumentException";
        } catch (IllegalArgumentException e) {
            assert e.getMessage().contains("Tag is null");
            log.info("Correctly threw exception: {}", e.getMessage());
        }
    }

    @Test
    void testUpdateTagWithNullNameThrowsException() {
        Tag tagWithoutName = new Tag();

        try {
            tagService.updateTag(tagWithoutName);
            assert false : "Expected IllegalArgumentException";
        } catch (IllegalArgumentException e) {
            assert e.getMessage().contains("Tag is null");
            log.info("Correctly threw exception: {}", e.getMessage());
        }
    }

    @Test
    void testDeleteTag() {
        tag = tagService.saveTag(tag);
        Long id = tag.getId();

        tagService.deleteTag(id);

        Tag fetchedTag = tagService.findTagById(id);
        assert fetchedTag == null;

        log.info("Successfully deleted tag with id: {}", id);
    }

    @Test
    void testDeleteTagWithNullIdThrowsException() {
        try {
            tagService.deleteTag(null);
            assert false : "Expected IllegalArgumentException";
        } catch (IllegalArgumentException e) {
            assert e.getMessage().contains("Tag id is null");
            log.info("Correctly threw exception: {}", e.getMessage());
        }
    }
}