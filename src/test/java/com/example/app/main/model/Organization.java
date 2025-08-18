package com.example.app.main.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;

import static jakarta.persistence.GenerationType.IDENTITY;

@Data
@Entity
public class Organization {

    @Id @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private String name;

    private String region;
}
