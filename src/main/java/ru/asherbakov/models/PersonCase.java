package ru.asherbakov.models;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class PersonCase {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column(name = "id", unique = true)
    private String id;

    @Column(name = "create_date", nullable = false)
    private LocalDate createDate;

    @Column(name = "first_name", nullable = false)
    private String firstName;
    @Column(name = "last_name", nullable = false)
    private String lastName;
    @Column(name = "middle_name")
    private String middleName;
    @Column(name = "social_number", nullable = false)
    private String socialNumber;
    @Column(name = "date_of_birthday")
    private LocalDate dateOfBirthday;
    @ManyToOne
    @JoinColumn(name = "status_id")
    private Status status;

    // Если удаляем дело - удаляем и записи по документам (orphanRemoval = true, cascade = CascadeType.PERSIST),
    // но по факту, дела мы не удаляем, а переводим в архив)))
    @ToString.Exclude
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "personCase", orphanRemoval = true, cascade = CascadeType.PERSIST)
    private Set<Document> documentSet = new HashSet<>();

    public PersonCase(LocalDate createDate, String firstName, String lastName, String middleName, String socialNumber, LocalDate dateOfBirthday, Status status) {
        this.createDate = createDate;
        this.socialNumber = socialNumber;
        this.firstName = firstName;
        this.lastName = lastName;
        this.middleName = middleName;
        this.dateOfBirthday = dateOfBirthday;
        this.status = status;
    }

    public String getFullName() {
        String personFullName = String.format("%s %s", this.getLastName(), this.getFirstName());
        if (this.getMiddleName() != null && !this.getMiddleName().isBlank()) {
            personFullName += " " + this.getMiddleName();
        }
        return personFullName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PersonCase that = (PersonCase) o;
        return id.equals(that.id) && createDate.equals(that.createDate) && firstName.equals(that.firstName) && lastName.equals(that.lastName) && Objects.equals(dateOfBirthday, that.dateOfBirthday) && status.equals(that.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, createDate, firstName, lastName, dateOfBirthday, status);
    }
}
