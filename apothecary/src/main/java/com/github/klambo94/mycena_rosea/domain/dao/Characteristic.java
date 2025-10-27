package com.github.klambo94.mycena_rosea.domain.dao;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

//@Getter
//@Setter
@Setter
@Getter
@Entity
@Table
public class Characteristic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "char_properties",
            joinColumns = @JoinColumn(name = "characteristic_id"),
            inverseJoinColumns = @JoinColumn(name = "char_prop_id"))
    private Set<CharProp> charProperties;

    public Characteristic() {
    }

    public Characteristic(String name, Set<CharProp> charProperties) {
        this.name = name;
        this.charProperties = charProperties;
    }

}
