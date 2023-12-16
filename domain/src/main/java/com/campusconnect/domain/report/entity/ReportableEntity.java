package com.campusconnect.domain.report.entity;

import com.campusconnect.domain.report.dto.ReportDto;
import com.campusconnect.domain.report.enums.ReportableEntities;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "cc_reportable_entity")
public class ReportableEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "reportable_entity_id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "is_disabled")
    private boolean isDisabled;

    @Column(name = "reported_entity")
    private ReportableEntities reportedEntity;

    public String generateURL() {
        return "https://www.example.com/report/" + id;
    }

    public void disable() {
        this.isDisabled = true;
    }

    public void enable() {
        this.isDisabled = false;
    }

    public boolean isDisabled() {
        return this.isDisabled;
    }

    public String getEntityName() {
        return reportedEntity.toString();
    }

    public UUID getEntityID() {
        return this.id;
    }
}
