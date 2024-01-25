package ru.asherbakov.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class Document {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column(name = "id", unique = true)
    private String id;

    @Column(name = "file_name")
    private String fileName;
    @Column(name = "original_name")
    private String originalName;
    @Column(name = "create_date", nullable = false, columnDefinition = "DateTime")
    private LocalDateTime createDate;
    // TODO Заменить связью
    @Column(name = "username")
    private String username;
    @Column(name="enabled", nullable = false)
    private boolean enabled;

    @ManyToOne
    @JoinColumn(name = "document_type_id")
    private DocumentType documentType;
    @ManyToOne
    @JoinColumn(name = "person_case_id")
    private PersonCase personCase;
    @ManyToOne
    @JoinColumn(name = "appeal_purpose_id")
    private AppealPurpose appealPurpose;


    public Document(LocalDateTime createDate, String username, DocumentType documentType, PersonCase personCase, AppealPurpose appealPurpose, boolean enabled) {
        this.createDate = createDate;
        this.username = username;
        this.documentType = documentType;
        this.personCase = personCase;
        this.appealPurpose = appealPurpose;
        this.enabled = enabled;
    }

//    public Document(String originalName, LocalDateTime createDate, String username, DocumentType documentType, PersonCase personCase, AppealPurpose appealPurpose, boolean enabled) {
//        this.originalName = originalName;
//        this.createDate = createDate;
//        this.username = username;
//        this.documentType = documentType;
//        this.personCase = personCase;
//        this.appealPurpose = appealPurpose;
//    }
}
