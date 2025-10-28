package com.github.klambo94.mycena_rosea.repositories;

import com.github.klambo94.mycena_rosea.domain.dao.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.stereotype.Repository;


@Repository
public interface StepRepository extends JpaRepository<Tag,Long> {
}
