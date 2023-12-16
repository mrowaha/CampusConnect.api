package com.campusconnect.ui.report.service;

import com.campusconnect.domain.report.dto.ReportDto;
import com.campusconnect.domain.report.entity.Report;
import com.campusconnect.domain.report.enums.ReportStatus;
import com.campusconnect.domain.report.respository.ReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ReportService {

    @Autowired
    private ReportRepository reportRepository;

    public List<ReportDto> viewAllReports() {
        return reportRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<ReportDto> viewReportsByName(String entityName) {
        // This method's implementation depends on how the entityName is related to the Report
        // For simplicity, it's assumed that Report has a method getEntityName()
        return reportRepository.findAll().stream()
                .filter(report -> report.getReportedEntity().toString().equals(entityName))
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public void resolveReport(UUID moderator, UUID reportId) {
        reportRepository.findById(reportId).ifPresent(report -> {
            report.setStatus(ReportStatus.RESOLVED);
            report.setResolverID(moderator);
            reportRepository.save(report);
        });
    }

    public void hideEntity(String entityName, UUID entityId) {
    }

    public ReportDto makeReport(ReportDto reportDto) {
        Report report = convertToEntity(reportDto);
        report = reportRepository.save(report);
        return convertToDto(report);
    }

    private ReportDto convertToDto(Report report) {
        return null;
    }

    private Report convertToEntity(ReportDto reportDto) {
        return null;
    }
}
