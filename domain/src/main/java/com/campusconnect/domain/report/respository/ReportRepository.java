package com.campusconnect.domain.report.respository;

import com.campusconnect.domain.report.entity.Report;
import com.campusconnect.domain.report.enums.ReportStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ReportRepository extends JpaRepository<Report, UUID> {

//    @Query("SELECT r from Report r WHERE r.id = :reportId")
//    Optional<Report> findById(@Param("reportID") UUID reportID);

    @Query("SELECT r from Report r WHERE r.status = :status")
    List<Report> findByStatus(@Param("status") ReportStatus status);
}
