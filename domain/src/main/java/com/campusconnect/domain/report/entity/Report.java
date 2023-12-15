package com.campusconnect.domain.report.entity;

import com.campusconnect.domain.report.enums.ReportStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@Table(name = "cc_report")
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;

    @Column(name = "title", nullable = false)
    @NonNull
    private String title;

    @Column(name = "description")
    private String descrption;

    // image

    // @Column(name = "reportedEntity", nullable = false)
    // @NonNull
    // private ReportableEntity reportedEntity;

    @Column(name = "reportedEntityID", nullable = false)
    @NonNull
    private UUID reportedEntityID;

    @Column(name = "reported_at", nullable = false)
    @NonNull
    private LocalDateTime reportedAt;

    @Column(name = "reporterID", nullable = false)
    @NonNull
    private UUID reporterID;

    @Column(name = "status")
    private ReportStatus status;

    @Column(name = "resolverID")
    private UUID resolverID;

    // Image
}
