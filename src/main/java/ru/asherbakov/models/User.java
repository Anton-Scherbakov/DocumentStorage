package ru.asherbakov.models;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.lang.NonNull;
import ru.asherbakov.controllers.StartController;

import javax.persistence.*;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NonNull
    @Column(unique = true)
    private String username;
    @NonNull
    private String password;
    private String firstName;
    private String lastName;
    private String middleName;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "STRUCTURAL_DIVISION_ID", nullable = false)
    @Getter private StructuralDivision structuralDivision;
    private byte active;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "LOCATION_ID", nullable = false)
    @Getter
    private Location location;
    @ManyToOne
    @JoinColumn(name = "ROLE_ID")
    private Role role;

    public User(@NonNull String username, @NonNull String password, String firstName, String lastName, String middleName, Location location, StructuralDivision structuralDivision, byte active) {
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.middleName = middleName;
        this.location = location;
        this.structuralDivision = structuralDivision;
        this.active = active;
    }

    public String getFullName() {
        String userFullName = String.format("%s %s", this.getLastName(), this.getFirstName());
        if (this.getMiddleName() != null && !this.getMiddleName().isBlank()) {
            userFullName += " " + this.getMiddleName();
        }
        return userFullName;
    }
}
