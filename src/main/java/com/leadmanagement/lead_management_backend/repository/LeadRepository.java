package com.leadmanagement.lead_management_backend.repository;

import com.leadmanagement.lead_management_backend.entity.Lead;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface LeadRepository extends JpaRepository<Lead, Integer> {
    List<Lead> findByUserId(Integer userId);
}
