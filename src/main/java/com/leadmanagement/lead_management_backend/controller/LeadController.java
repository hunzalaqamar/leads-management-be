package com.leadmanagement.lead_management_backend.controller;

import com.leadmanagement.lead_management_backend.dto.ApiResponseDTO;
import com.leadmanagement.lead_management_backend.dto.LeadDTO;
import com.leadmanagement.lead_management_backend.service.LeadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/leads")
public class LeadController {

    @Autowired
    private LeadService leadService;

    @PostMapping
    public ResponseEntity<ApiResponseDTO<Integer>> createLead(@RequestBody LeadDTO leadDTO) {
        try {
            LeadDTO createdLead = leadService.createLead(leadDTO);
            return ResponseEntity.ok(new ApiResponseDTO<>(true, "Lead created successfully", createdLead.getId()));
        } catch (Exception e) {
            return ResponseEntity.status(400).body(new ApiResponseDTO<>(false, "Failed to create lead: " + e.getMessage(), null));
        }
    }

    @GetMapping
    public ResponseEntity<ApiResponseDTO<List<LeadDTO>>> getLeads() {
        try {
            List<LeadDTO> leads = leadService.getLeads();
            return ResponseEntity.ok(new ApiResponseDTO<>(true, "Leads retrieved successfully", leads));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ApiResponseDTO<>(false, "Failed to retrieve leads: " + e.getMessage(), null));
        }
    }

    @DeleteMapping
    public ResponseEntity<ApiResponseDTO<String>> deleteLeads(@RequestBody List<Long> leadIds) {
        try {
            leadService.deleteLeads(leadIds);
            return ResponseEntity.ok(new ApiResponseDTO<>(true, "Leads deleted successfully", null));
        } catch (Exception e) {
            return ResponseEntity.status(400).body(new ApiResponseDTO<>(false, "Failed to delete leads: " + e.getMessage(), null));
        }
    }
}