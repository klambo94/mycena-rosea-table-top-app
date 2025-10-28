package com.github.klambo94.mycena_rosea.domain.dao;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@Entity
@Table
public class Step {

    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE)
    @PrimaryKeyJoinColumn
    private Long id;

    @Column private double stepNumber;
    @Column private String instruction;
    
    @Column private String imagePath;  // Path to the stored image file
    @Column private String imageName;
    public Step(double stepNumber, String instruction) {
        this.stepNumber = stepNumber;
        this.instruction = instruction;
    }
    
    public Step(double stepNumber, String instruction, String imagePath, String imageName) {
        this.stepNumber = stepNumber;
        this.instruction = instruction;
        this.imagePath = imagePath;
        this.imageName = imageName;
    }
}
