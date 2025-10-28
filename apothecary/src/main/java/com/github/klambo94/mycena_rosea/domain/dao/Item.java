package com.github.klambo94.mycena_rosea.domain.dao;

import com.github.klambo94.mycena_rosea.domain.Type;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Setter
@Getter
@NoArgsConstructor
@Entity
@Table(name = "item")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name="dtype",
        discriminatorType = DiscriminatorType.INTEGER)
public class Item {
    
    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE)
    @PrimaryKeyJoinColumn
    Long id;

    @Column(unique = true)
    String name;
    @Column
    String description;

    @Enumerated(EnumType.STRING)
    Type itemType; //Should contain data from type enum
    @Column
    double price;
    @Column
    double quantity;
    @Column
    double weight;
    @Column
    private String imagePath;  // Path to the stored image file
    @Column
    private String imageName;
    @ManyToMany(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    Set<Characteristic> characteristics;

    @ManyToMany(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    Set<Tag> tags;

    public Item(String name, String description, Type itemType, double price, double quantity, double weight, String imagePath, String imageName, Set<Characteristic> characteristics, Set<Tag> tags) {
        this.name = name;
        this.description = description;
        this.itemType = itemType;
        this.price = price;
        this.quantity = quantity;
        this.weight = weight;
        this.imagePath = imagePath;
        this.imageName = imageName;
        this.characteristics = characteristics;
        this.tags = tags;
    }
}
