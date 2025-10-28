package com.github.klambo94.mycena_rosea.repositories;

import com.github.klambo94.mycena_rosea.domain.dao.Characteristic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CharacteristicRepository extends JpaRepository<Characteristic,Long> {
    @Query("SELECT c FROM Characteristic c LEFT JOIN FETCH c.charProperties")
    List<Characteristic> findAllWithProperties();
    Characteristic findCharacteristicByName(String name);
}
