package com.vinay.reportsapi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.vinay.reportsapi.entity.EligibilityDetails;

@Repository
public interface EligibilityRepository extends JpaRepository<EligibilityDetails, Integer> {
	@Query("select distinct(name) from EligibilityDetails")
	public List<String> getPlanNames();
	@Query("select distinct(planStatus) from EligibilityDetails")
	public List<String> getPlanStatus();
}
