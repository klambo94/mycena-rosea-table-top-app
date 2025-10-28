package com.github.klambo94.mycena_rosea.domain.dao;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

//@Getter
//@Setter
//@AllArgsConstructor
//@NoArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "char_prop")
public class CharProp {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    @Column(nullable = false, unique = true)
    String name;

    @Column(nullable = false)
    String value;

    public CharProp() {
    }

    public CharProp(String name, String value) {
        this.name = name;
        this.value = value;
    }

}
