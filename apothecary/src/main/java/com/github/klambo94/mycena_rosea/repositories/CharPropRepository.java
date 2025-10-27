package com.github.klambo94.mycena_rosea.repositories;

import com.github.klambo94.mycena_rosea.domain.dao.CharProp;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.stereotype.Repository;

@Repository
public interface CharPropRepository extends JpaRepositoryImplementation<CharProp,Long> {
}
