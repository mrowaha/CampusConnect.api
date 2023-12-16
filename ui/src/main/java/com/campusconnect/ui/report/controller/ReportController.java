package com.campusconnect.ui.report.controller;

import com.campusconnect.domain.report.dto.ReportDto;
import com.campusconnect.ui.report.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/reports")
public class ReportController {
    @Autowired
    private ReportService reportService;

    @GetMapping
    public ResponseEntity<List<ReportDto>> viewAllReports() {
        return ResponseEntity.ok(reportService.viewAllReports());
    }
}
