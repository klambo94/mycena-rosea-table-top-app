package com.github.klambo94.mycena_rosea.domain.dao;

import jakarta.persistence.*;

import java.util.List;
import java.util.Set;

@Entity
@DiscriminatorValue("1")
public class Product extends Item {

    @Column
    private double weight;
    @ManyToMany(cascade = CascadeType.ALL)
    private Set<Item> ingredients;
}
