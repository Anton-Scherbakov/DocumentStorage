package ru.asherbakov.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import ru.asherbakov.service.ConvertDataService;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class CorrectPersonData {
    @Autowired
    private transient ConvertDataService convertDataService;
    @Id
    @Column(name = "social_number", length = 14)
    @NotNull
    private String socialNumber;
    @Column(name = "last_name", length = 50, nullable = false)
    private String lastName;
    @Column(name = "first_name", length = 50, nullable = false)
    private String firstName;
    @Column(name = "middle_name", length = 50)
    private String middleName;
    @Column(name = "date_of_birthday")
    private LocalDate dateOfBirthday;

    public CorrectPersonData(String socialNumber, String lastName, String firstName, String middleName, LocalDate dateOfBirthday) {
        this.socialNumber = socialNumber;
        this.firstName = firstName;
        this.lastName = lastName;
        this.middleName = middleName;
        this.dateOfBirthday = dateOfBirthday;
    }
}
