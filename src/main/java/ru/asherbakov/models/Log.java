package ru.asherbakov.models;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import ru.asherbakov.enums.Action;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class Log {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column(name = "id", unique = true)
    private String id;
    @Column(columnDefinition = "DATETIME")
    private LocalDateTime date;
    private String username;
    @Column(name = "user_role")
    private String userRole;
    @Column(name = "user_full_name")
    private String userFullName;

    @Column(name = "person_case_id")
    private String personCaseId;
    @Column(name = "social_number")
    private String socialNumber;
    @Column(name = "document_id")
    private String documentId;
    @Column(name = "document_type")
    private String documentType;
    private String action;
    @Column(columnDefinition = "TEXT")
    private String message;

    public Log(LocalDateTime date, String username, String userRole, String userFullName,
               String personCaseId, String socialNumber, String documentId, String documentType, Action action, String message) {
        this.date = date;
        this.username = username;
        this.userRole = userRole;
        this.userFullName = userFullName;
        this.personCaseId = personCaseId;
        this.socialNumber = socialNumber;
        this.documentId = documentId;
        this.documentType = documentType;
        this.action = action.name();
        this.message = message;
    }
}
