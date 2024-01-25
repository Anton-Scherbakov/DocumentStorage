package ru.asherbakov.models;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
public class StructuralDivision {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String address;
    @Column(nullable = false, length = 10)
    private String code;
    @Column(nullable = false, length = 150)
    private String name;
    @Column(name = "phone_number", length = 20)
    private String phoneNumber;
    @Column(name = "post_index", length = 6)
    private String postIndex;
    @ToString.Exclude
    @OneToMany(mappedBy = "structuralDivision")
    private Set<User> users = new HashSet<>();
}
