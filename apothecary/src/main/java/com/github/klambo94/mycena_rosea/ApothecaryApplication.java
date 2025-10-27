package com.github.klambo94.mycena_rosea;

import com.github.klambo94.mycena_rosea.domain.Type;
import com.github.klambo94.mycena_rosea.domain.dao.*;
import com.github.klambo94.mycena_rosea.repositories.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.List;
import java.util.Set;

@Slf4j
@SpringBootApplication
@EntityScan(basePackages = "com.github.klambo94.mycena_rosea.domain.dao")
@EnableJpaRepositories(basePackages = "com.github.klambo94.mycena_rosea.repositories")
public class ApothecaryApplication {

    final CharPropRepository charPropRepository;
    final TagRepository tagRepository;
    final ProductRepository productRepository;
    final ItemRepository itemRepository;
    final CharacteristicRepository characteristicRepository;

    public ApothecaryApplication(CharPropRepository charPropRepository, TagRepository tagRepository, ProductRepository productRepository, ItemRepository itemRepository, CharacteristicRepository characteristicRepository) {
        this.charPropRepository = charPropRepository;
        this.tagRepository = tagRepository;
        this.productRepository = productRepository;
        this.itemRepository = itemRepository;
        this.characteristicRepository = characteristicRepository;
    }

    public static void main(String[] args) {
        SpringApplication.run(ApothecaryApplication.class, args);
    }

}