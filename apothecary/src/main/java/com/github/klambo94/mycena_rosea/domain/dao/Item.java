package com.github.klambo94.mycena_rosea.domain.dao;

import com.github.klambo94.mycena_rosea.domain.Type;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.Set;

@Setter
@Getter
@NoArgsConstructor
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="type_type",
        discriminatorType = DiscriminatorType.INTEGER)
public class Item {
    
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @PrimaryKeyJoinColumn
    Long itemId;

    @Column(unique = true)
    String name;
    @Column
    String description;

    @Enumerated(EnumType.STRING)
    Type itemType; //Should contain data from type enum
    @Column
    double price;
    @Column
    private double quantity;
    @ManyToMany
    Set<Characteristic> characteristics;

    @ManyToMany
    Set<Tag> tags;
}
