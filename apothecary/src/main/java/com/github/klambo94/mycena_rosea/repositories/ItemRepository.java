package com.github.klambo94.mycena_rosea.repositories;

import com.github.klambo94.mycena_rosea.domain.dao.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {


    @Query("SELECT i FROM Item i LEFT JOIN FETCH i.tags")
    List<Item> findAllWithTags();

    @Query("SELECT i FROM Item i LEFT JOIN FETCH i.characteristics")
    List<Item> findAllWithCharacteristics();

    @Query("SELECT i FROM Item i LEFT JOIN FETCH i.characteristics c "
            + "LEFT JOIN FETCH c.charProperties")
    List<Item> findAllWithCharacteristicsAndProperties();


    @Query("SELECT i FROM Item i LEFT JOIN FETCH i.characteristics c "
            + "LEFT JOIN FETCH c.charProperties"
            + " LEFT JOIN FETCH i.tags")
    List<Item> findAllWithFullyHydrated();

    Item findItemByName(String name);
}
