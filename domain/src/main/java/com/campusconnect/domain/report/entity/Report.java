package com.campusconnect.domain.report.entity;

import com.campusconnect.domain.report.enums.ReportStatus;
import com.campusconnect.domain.report.enums.ReportableEntities;
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
    @Column(name = "report_id")
    private UUID id;

    @Column(name = "title", nullable = false)
    @NonNull
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "image_url")
    private String image;

    @Column(name = "reported_entity")
    @Enumerated(EnumType.STRING)
    private ReportableEntities reportedEntity;

    @Column(name = "reported_entity_id", nullable = false)
    @NonNull
    private UUID reportedEntityID;

    @Column(name = "reported_at", nullable = false)
    @NonNull
    private LocalDateTime reportedAt;

    @Column(name = "reporter_id", nullable = false)
    @NonNull
    private UUID reporterID;

    @Column(name = "status")
    private ReportStatus status;

    @Column(name = "resolver_id")
    private UUID resolverID;
}
