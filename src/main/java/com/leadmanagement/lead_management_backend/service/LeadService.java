package com.leadmanagement.lead_management_backend.service;

import com.leadmanagement.lead_management_backend.dto.LeadDTO;
import com.leadmanagement.lead_management_backend.entity.Lead;
import com.leadmanagement.lead_management_backend.entity.User;
import com.leadmanagement.lead_management_backend.repository.LeadRepository;
import com.leadmanagement.lead_management_backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LeadService {

    @Autowired
    private LeadRepository leadRepository;

    @Autowired
    private UserRepository userRepository;

    public LeadDTO createLead(LeadDTO leadDTO) {
        Lead lead = new Lead();
        lead.setFullName(leadDTO.getFullName());
        lead.setEmail(leadDTO.getEmail());
        lead.setPhone(leadDTO.getPhone());
        lead.setCompanyName(leadDTO.getCompanyName());
        lead.setNotes(leadDTO.getNotes());
        lead.setCreatedAt(leadDTO.getCreatedAt() != null ? leadDTO.getCreatedAt() : LocalDateTime.now());

        lead = leadRepository.save(lead);
        leadDTO.setId(lead.getId());
        leadDTO.setCreatedAt(lead.getCreatedAt());
        return leadDTO;
    }

    public List<LeadDTO> getLeads() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
        List<Lead> leads = leadRepository.findByUserId(user.getId());
        return leads.stream().map(lead -> {
            LeadDTO dto = new LeadDTO();
            dto.setId(lead.getId());
            dto.setFullName(lead.getFullName());
            dto.setEmail(lead.getEmail());
            dto.setPhone(lead.getPhone());
            dto.setCompanyName(lead.getCompanyName());
            dto.setNotes(lead.getNotes());
            dto.setCreatedAt(lead.getCreatedAt());
            return dto;
        }).collect(Collectors.toList());
    }

    public void deleteLeads(List<Long> leadIds) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
        List<Lead> leads = leadRepository.findByUserId(user.getId());
        List<Lead> leadsToDelete = leads.stream()
                .filter(lead -> leadIds.contains(lead.getId()))
                .collect(Collectors.toList());
        leadRepository.deleteAll(leadsToDelete);
    }
}