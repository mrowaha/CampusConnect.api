package com.campusconnect.ui.user.controller;

import com.campusconnect.domain.user.dto.ReportDto;
import com.campusconnect.ui.common.controller.SecureController;
import com.campusconnect.ui.user.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reports")
public class ReportController extends SecureController {

    private final ReportService reportService;

    @Autowired
    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    // Create a report
    @PostMapping
    public ResponseEntity<ReportDto> createReport(@RequestBody ReportDto reportDto) {
        return ResponseEntity.ok(reportDto);
    }

    // View reports
    @GetMapping
    public ResponseEntity<List<ReportDto>> getAllReports() {
        return ResponseEntity.ok(reportService.viewAllReports());
    }
}
