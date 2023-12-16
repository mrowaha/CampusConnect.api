package com.campusconnect.ui.report.controller;

import com.campusconnect.domain.report.dto.ReportDto;
import com.campusconnect.domain.security.RequiredScope;
import com.campusconnect.domain.security.SecurityScope;
import com.campusconnect.domain.user.entity.User;
import com.campusconnect.ui.report.service.ReportService;
import com.campusconnect.ui.utils.UserUtilities;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/reports")
public class ReportController {
    @Autowired
    private ReportService reportService;
    private UserUtilities userUtilities;

    @GetMapping
    @RequiredScope(scope = SecurityScope.NONE) // Test only - should be moderator
    public ResponseEntity<List<ReportDto>> viewAllReports() {
        return ResponseEntity.ok(reportService.viewAllReports());
    }

    @GetMapping("/byName/{entityName}")
    @RequiredScope(scope = SecurityScope.NONE) // Test only - should be moderator
    public ResponseEntity<List<ReportDto>> viewReportsByName(@PathVariable String entityName) {
        return ResponseEntity.ok(reportService.viewReportsByName(entityName));
    }

    @PostMapping("/resolve")
    @RequiredScope(scope = SecurityScope.NONE) // Test only - should be moderator
//    public ResponseEntity<?> resolveReport(@RequestParam UUID moderator, @RequestParam UUID reportId) {
    public ResponseEntity<?> resolveReport(
            Authentication authentication,
            @RequestParam UUID reportId
    ) {
        User user = userUtilities.getUserFromAuth(authentication);
        UUID moderator = user.getUserId();
        reportService.resolveReport(moderator, reportId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/hideEntity")
    @RequiredScope(scope = SecurityScope.NONE) // Test only - should be moderator
    public ResponseEntity<?> hideEntity(@RequestParam String entityName, @RequestParam UUID entityId) {
        reportService.hideEntity(entityName, entityId);
        return ResponseEntity.ok().build();
    }

    @PostMapping
    @RequiredScope(scope = SecurityScope.NONE) // Test only - should be moderator
    public ResponseEntity<ReportDto> makeReport(@RequestBody ReportDto reportEntity) {
        return ResponseEntity.ok(reportService.makeReport(reportEntity));
    }
}