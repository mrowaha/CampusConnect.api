package com.campusconnect.domain.report.entity;

public interface ReportableEntity {

    String generateURL();
    
    void disable();
    
    void enable();
    
    boolean isDisabled();
    
    String getEntityName();
    
    String getEntityID();
    
    Report makeReport(ReportableEntity reportEntity);
}
